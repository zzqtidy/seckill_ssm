package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SeckillDao {

    /**
     * 减库存
     * 注意，有多个参数的时候，需要给出@Param("xxx"),xxx匹配mapper中的对应参数，因为多个参数java默认会转化成args0,args1……,
     * 所以要给一个参数别名
     * @param seckillId
     * @param killTime
     * @return 更新记录的行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 获取单个秒杀商品
     *
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 根据偏移量查询秒商品列表
     * 注意，有多个参数的时候，需要给出@Param("xxx"),xxx匹配mapper中的对应参数，因为多个参数java默认会转化成args0,args1……,
     * 所以要给一个参数别名
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 通过存储过程执行秒杀，添加用户并且建库存。
     * @param paramMap
     */
    void killByProcedure(Map<String, Object> paramMap);
}
