package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {
    /**
     * 插入购买明细，过滤重复
     * 注意，有多个参数的时候，需要给出@Param("xxx"),xxx匹配mapper中的对应参数，因为多个参数java默认会转化成args0,args1……,
     * 所以要给一个参数别名
     * @param secKillId
     * @param userPhone
     * @return 插入的结果集数量
     */
    int     insertSuccessKilled(@Param("secKillId") long secKillId, @Param("userPhone") long userPhone);

    /**
     * 根据ID查询SuccessKilled并且携带seckill对象实体
     * 注意，有多个参数的时候，需要给出@Param("xxx"),xxx匹配mapper中的对应参数，因为多个参数java默认会转化成args0,args1……,
     * 所以要给一个参数别名
     * @param secKillId
     * @param userPhone
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("secKillId") long secKillId,@Param("userPhone") long userPhone);


}
