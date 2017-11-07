package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 配置spring和junit的整合，为了junit启动时加载springIOC容器
 * spring-test,junit
 */

@RunWith(SpringJUnit4ClassRunner.class)//junit启动时加载springIOC容器
//告诉junit spring容器的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    //注入Dao的实现类依赖,@Resource是spring提供的依赖注入的注解
    @Resource
    private SeckillDao seckillDao;
    @Test
    public void reduceNumber() throws Exception {
        long id=1100L;
        Date date = new Date();
        int successCount = seckillDao.reduceNumber(id, date);
        System.out.println("成功更新的条数："+successCount);
    }

    @Test
    public void getById() throws Exception {
        long id=1000L;
        Seckill seckill = seckillDao.getById(id);
        System.out.println(seckill);
    }

    @Test
    public void queryAll() throws Exception {
        List<Seckill> seckills = seckillDao.queryAll(0,100);
        for (Seckill seckill:seckills) {
            System.out.println(seckill);
        }
    }

}