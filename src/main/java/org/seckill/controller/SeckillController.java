package org.seckill.controller;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

//所有的url路径应该是：/模块/资源/{id或其他}/细分  如 /seckill/list

@Controller
@RequestMapping(value = "/seckill")
public class SeckillController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    /**
     * 获取列表页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        //list.jsp+参数model 就组成了ModelAndView
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        //相当于匹配list.jsp
        return "list";
    }
    @RequestMapping(value = "/allseckill", method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<List<Seckill>> allseckill() {
        List<Seckill> list = seckillService.getSeckillList();
        return new SeckillResult<List<Seckill>>(true, list);
    }

    /**
     * 获取详情页
     *
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable(value = "seckillId") Long seckillId, Model model) {
        //注意@PathVariable(value = "seckillId")是取出url地址中的{seckillId},当然也可以不用写，不过按照规范，最好写上
        if (seckillId == null) {
            return "forward:/seckill/list";
        }
        Seckill seckill = seckillService.getSeckillById(seckillId);
        if (seckill == null) {
            return "redirect:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    @RequestMapping(value = "/{seckillId}/seckilldetail", method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Seckill> seckilldetail(@PathVariable(value = "seckillId") Long seckillId, Model model) {
        //注意@PathVariable(value = "seckillId")是取出url地址中的{seckillId},当然也可以不用写，不过按照规范，最好写上
        if (seckillId == null) {

            return new SeckillResult<Seckill>(false,"seckill请求参数为空");
        }
        Seckill seckill = seckillService.getSeckillById(seckillId);
        if (seckill == null) {
            return new SeckillResult<Seckill>(false,seckill);
        }
        return new SeckillResult<Seckill>(true,seckill);
    }

    /**
     * 获取秒杀的Url
     *
     * @param seckillId
     * @return
     */
    @RequestMapping(value = "/{seckillId}/exporse",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody //这句话表示返回的json数据，不再是jsp+model的ModelAndView
    public SeckillResult<Exposer> exporse(@PathVariable(value = "seckillId") Long seckillId) {
        SeckillResult<Exposer> seckillResult;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            seckillResult = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            seckillResult = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return seckillResult;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable(value = "seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5, //value可以省略
                                                   @CookieValue(value = "userPhone", required = false) Long phone) //读取cookie，不是必须的
    {
        SeckillResult<SeckillExecution> seckillResult;
        SeckillExecution seckillExecution;
        if (phone == null) {
            seckillResult = new SeckillResult<SeckillExecution>(false, "未注册");
        }

        try {
            seckillExecution = seckillService.executeSeckill(seckillId, phone, md5);
        } catch (RepeatKillException e) {
            logger.error(e.getMessage(), e);
            seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
        } catch (SeckillCloseException e) {
            logger.error(e.getMessage(), e);
            seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.END);
        } catch (SeckillException e) {
            logger.error(e.getMessage(), e);
            seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);

        }
        seckillResult = new SeckillResult<SeckillExecution>(true, seckillExecution);
        return seckillResult;
    }

    /**
     * 获取系统的当前时间
     * @return
     */
    @RequestMapping(value = "/time/now",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Long> time() {
        Long time = (new Date()).getTime();
        return new SeckillResult<Long>(true, time);
    }
}
