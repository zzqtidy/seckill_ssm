package org.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"
})

public class SeckillServiceImplTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckills = seckillService.getSeckillList();
        logger.info("seckills={}",seckills);
    }

    @Test
    public void getSeckillById() throws Exception {
        long seckillId = 1000L;
        Seckill seckill = seckillService.getSeckillById(seckillId);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}",exposer);
        /**
         * exposed=true, md5='8bd2377478993cae4198f432c27d85a6', seckillId=1000, now=0, start=0, end=0
         */
    }

    @Test
    public void executeSeckill() throws Exception {
        long id = 1000;
        long userPhone=13467876546L;
        String md5 = "8bd2377478993cae4198f432c27d85a6";
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(id, userPhone, md5);
            logger.info("seckillExecution={}",seckillExecution.toString());
        }
        catch (Exception ex){
            logger.info("异常："+ex.getMessage());
        }

    }

    @Test
    public void executeSeckillByProcedure() throws Exception{
        long id = 1000;
        long userPhone=13467876546L;
        String md5 = "8bd2377478993cae4198f432c27d85a6";
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckillByProcedure(id, userPhone, md5);
            logger.info("seckillExecution={}",seckillExecution);
        }
        catch (Exception ex){
            logger.info("异常："+ex.getMessage());
        }
    }

}