package com.seckill.web;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.dto.SeckillResult;
import com.seckill.entity.Seckill;
import com.seckill.enums.SeckillStateEnum;
import com.seckill.exception.RepeatSeckillException;
import com.seckill.exception.SeckillClosedException;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;

/**
 * @description: 秒杀页面控制
 * @author: Black Tom
 * @create: 2020-06-24 10:38
 **/
@Controller
@RequestMapping(value = "/seckill")
public class SeckillController {
	@Autowired
	private SeckillService seckillService;

	/* el表达式等都是包装类型，用Long类型可以减少装箱/拆箱；
		https://www.cnblogs.com/softidea/p/3789237.html*/
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getList(Long seckillId, Model model) {
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list", list);
		return "list";
	}

	@RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
		if (seckillId == null) {
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getById(seckillId);
		if (seckill == null) {
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}

	/**
	 * @Description: POST提交秒杀是否成功信息，并将JSON反序列化为Java对象
	 * @ResponseBody注解作用： Spring automatically deserializes the JSON into a Java type
	 * assuming an appropriate one is specified. By default, the type we annotate with the
	 * @RequestBody annotation must correspond to the JSON sent from our client-side controller
	 * https://www.baeldung.com/spring-request-response-body
	 * @Param: [seckillId]
	 * @return: seckill.dto.SeckillResult<seckill.dto.Exposer>
	 */
	@RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST,
			produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
		SeckillResult<Exposer> result;
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			System.out.println("seckillId: " + seckillId);
			result = new SeckillResult<>(true, exposer);
		} catch (Exception e) {
			e.printStackTrace();
			result = new SeckillResult<>(false, e.getMessage());
		}
		System.out.println("返回结果： " + result.getData());
		return result;
	}

	@RequestMapping(value = "/{seckillId}/{md5}/execution",method = RequestMethod.POST,
			produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId, @PathVariable("md5") String md5, @CookieValue(value = "killPhone", required = false) Long userPhone) {
		SeckillResult<SeckillExecution> result;
		System.out.println("执行秒杀：" + md5);
		if (userPhone == null) {
			return new SeckillResult<>(false, "未注册");
		}
		try {
			SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
			return new SeckillResult<>(true, execution);
		} catch (RepeatSeckillException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
			System.out.println("请勿重复参与秒杀");
			return new SeckillResult<>(true, execution);
		} catch (SeckillClosedException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
			System.out.println("秒杀已结束");
			return new SeckillResult<>(true, execution);
		}catch (SeckillException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
			System.out.println("内部错误");
			return new SeckillResult<>(true, execution);
		}
	}

	//获取系统时间
	@RequestMapping(value = "/time/now",method = RequestMethod.GET)
	@ResponseBody
	public SeckillResult<Long> time(){
		Date now= new Date();
		System.out.println(new SeckillResult<>(true,now.getTime()));
		return new SeckillResult<>(true,now.getTime());
	}
}
