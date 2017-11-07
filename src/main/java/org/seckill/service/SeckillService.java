package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口，一定要站在"使用者"的角度
 */
public interface SeckillService {
    /**
     * 查询所有秒杀的记录
     * @return List<Seckill>
     */
    List<Seckill> getSeckillList();

    /**
     * 根据seckillId查询秒杀记录
     * @param seckillId
     * @return Seckill
     */
    Seckill getSeckillById(long seckillId);

    /**
     * 秒杀开启时输出秒杀地址，否则输出系统时间和秒杀时间
     * @param seckillId
     * @return Exposer
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     * return SeckillExecution
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
    throws RepeatKillException,SeckillCloseException,SeckillException;
    /**
     * 执行秒杀操作(存储过程)
     * @param seckillId
     * @param userPhone
     * @param md5
     * return SeckillExecution
     */
    SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5);
}
