package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class RedisDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JedisPool jedisPool;
    private final String KEY_PREV="seckill:";
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip,int port) {
        this.jedisPool = new JedisPool(ip, port);
    }

    public Seckill  getSeckill(long secKillId) {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = KEY_PREV+secKillId;
                //jedis内部没有实现序列化
                //get --> byte[] --> 反序列化 --> object(seckill)
                //利用protostuff可以很高效的进行序列化,相比java自带的序列化，空间节省5~9倍，速度大概快2个数量级
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {
                    //redis中拿到了这个对象，进行反序列化
                    Seckill seckill = schema.newMessage();
                    ProtobufIOUtil.mergeFrom(bytes, seckill, schema);
                    return  seckill;
                }
            }
            finally {
                jedis.close();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
        }
        return null;
    }

    public String putSeckill(Seckill seckill) {
        //set object -> bytes[]
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = KEY_PREV + seckill.getSeckillId();
                byte[] bytes = ProtobufIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int timeout = 60 * 60; //一个小时
                jedis.setex(key.getBytes(), timeout, bytes);
            } finally {
                jedis.close();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
        }
        return null;
    }
}
