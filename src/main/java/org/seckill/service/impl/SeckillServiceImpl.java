package org.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 目前Spring提供的注解（将该类初始化并放入到Spring的容器中）有：
 *
 * @Component 所有的组件，既不知道属于哪种组件
 * @Controller 用于控制器组件
 * @Serveice 用于Service组件
 * @Dao 用于Dao组件
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    //日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //由于seckillDao和successKilledDao在之前已经由Mybatis初始化并注入到SpringIOC容器中，所以下面用Spring的@Autowired自动加载即可
    //既加载srping容器中已经存在的对象时用@Autowired
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;
    //MD5的盐值字符串，用于混淆MD值
    private final String slat = "fjsahjkhgsfhu^8y8reywtyw389hr&^*&%giosiyghfdshuigsdiofgh";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 10000);
    }

    public Seckill getSeckillById(long seckillId) {
        return seckillDao.getById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //优化点 缓存优化
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            seckill = getSeckillById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        //转化成特定字符串的过程
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws RepeatKillException, SeckillCloseException, SeckillException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill date rewrite");
        }
        try {
            //先进行记录购买明细
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            //唯一性：seckillId, userPhone
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                //执行秒杀：减库存
                int updateCount = seckillDao.reduceNumber(seckillId, new Date());
                //没有更新条数，表示秒杀已经结束
                if (updateCount <= 0) {
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }

            }
        }
        //在Exception之前显示的抛出对应的详细异常
        catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有的检查类/编译期的异常全部转化为运行期异常，便于后期事务回滚
            throw new SeckillException("inner error" + e.getMessage());
        }
    }

    public SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill date rewrite");
        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        //此处的放入项必须和SeckillDao.xml中killByProcedure中对应的参数一致
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        //执行存储过程之后，result被赋值
        try {
            seckillDao.killByProcedure(map);
            //获取map中的result的值，通过第三方的MapUtil来取值
            int result = MapUtils.getInteger(map, "result", -3);
            if (result == 1) {
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
            } else {
                return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
