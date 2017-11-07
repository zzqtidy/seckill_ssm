package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
/**
 * 配置spring和junit的整合，为了junit启动时加载springIOC容器
 * spring-test,junit
 */
//junit启动时加载springIOC容器
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring容器的配置文件
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledDaoTest {
    //注入Dao的实现类依赖,@Resource是spring提供的依赖注入的注解(相当于在springIOC容器中拿出对象，该对象实例已经被IOC初始化)
    @Resource
    private SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() throws Exception {
        int count=successKilledDao.insertSuccessKilled(1000L,15308170400L);
        System.out.println("成功插入的条数："+count);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000L, 15308170400L);
        System.out.println(successKilled);
    }

}