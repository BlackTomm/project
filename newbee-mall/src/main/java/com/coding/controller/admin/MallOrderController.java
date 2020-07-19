package com.coding.controller.admin;

import com.coding.common.ServiceResultEnum;
import com.coding.controller.vo.MallOrderItemVO;
import com.coding.entity.MallOrder;
import com.coding.service.MallOrderService;
import com.coding.util.PageUtil;
import com.coding.util.Result;
import com.coding.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @description: 后台订单列表
 * @author: Black Tom
 * @create: 2020-07-19 18:23
 **/

@Controller
@RequestMapping("/admin")
public class MallOrderController {

	@Resource
	private MallOrderService mallOrderService;

	@GetMapping("/orders")
	public String ordersPage(HttpServletRequest request) {
		request.setAttribute("path", "orders");
		return "admin/mall-order";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/orders/list", method = RequestMethod.GET)
	@ResponseBody
	public Result list(@RequestParam Map<String, Object> params) {
		if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		PageUtil pageUtil = new PageUtil(params);
		return ResultGenerator.genSuccessResult(mallOrderService.getNewBeeMallOrdersPage(pageUtil));
	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/orders/update", method = RequestMethod.POST)
	@ResponseBody
	public Result update(@RequestBody MallOrder mallOrder) {
		if (Objects.isNull(mallOrder.getTotalPrice())
				|| Objects.isNull(mallOrder.getOrderId())
				|| mallOrder.getOrderId() < 1
				|| mallOrder.getTotalPrice() < 1
				|| StringUtils.isEmpty(mallOrder.getUserAddress())) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		String result = mallOrderService.updateOrderInfo(mallOrder);
		if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(result);
		}
	}


	/**
	 * 详情
	 */
	@GetMapping("/order-items/{id}")
	@ResponseBody
	public Result info(@PathVariable("id") Long id) {
		List<MallOrderItemVO> orderItems = mallOrderService.getOrderItems(id);
		if (!CollectionUtils.isEmpty(orderItems)) {
			return ResultGenerator.genSuccessResult(orderItems);
		}
		return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
	}

	/**
	 * 配货
	 */
	@RequestMapping(value = "/orders/checkDone", method = RequestMethod.POST)
	@ResponseBody
	public Result checkDone(@RequestBody Long[] ids) {
		if (ids.length < 1) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		String result =mallOrderService.checkDone(ids);
		if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(result);
		}
	}

	/**
	 * 出库
	 */
	@RequestMapping(value = "/orders/checkOut", method = RequestMethod.POST)
	@ResponseBody
	public Result checkOut(@RequestBody Long[] ids) {
		if (ids.length < 1) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		String result = mallOrderService.checkOut(ids);
		if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(result);
		}
	}

	/**
	 * 关闭订单
	 */
	@RequestMapping(value = "/orders/close", method = RequestMethod.POST)
	@ResponseBody
	public Result closeOrder(@RequestBody Long[] ids) {
		if (ids.length < 1) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		String result = mallOrderService.closeOrder(ids);
		if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(result);
		}
	}
}
