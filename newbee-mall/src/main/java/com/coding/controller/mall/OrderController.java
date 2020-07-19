package com.coding.controller.mall;

import com.coding.common.Constants;
import com.coding.common.MallException;
import com.coding.common.ServiceResultEnum;
import com.coding.controller.vo.MallOrderDetailVO;
import com.coding.controller.vo.MallShoppingCartItemVO;
import com.coding.controller.vo.MallUserVO;
import com.coding.entity.MallOrder;
import com.coding.service.MallOrderService;
import com.coding.service.MallShoppingCartService;
import com.coding.util.PageResult;
import com.coding.util.PageUtil;
import com.coding.util.Result;
import com.coding.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-18 00:16
 **/
@Controller
public class OrderController {
	@Resource
	private MallShoppingCartService mallShoppingCartService;
	@Resource
	private MallOrderService mallOrderService;

	@GetMapping("/orders/{orderNo}")
	public String orderDetailPage(HttpServletRequest request, @PathVariable("orderNo") String orderNo, HttpSession httpSession) {
		MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
		//获取订单信息
		MallOrderDetailVO orderDetailVO = mallOrderService.getOrderDetailByOrderNo(orderNo, user.getUserId());
		if (orderDetailVO == null) {
			return "error/error_5xx";
		}
		request.setAttribute("orderDetailVO", orderDetailVO);
		return "mall/order-detail";
	}

	/**
	 * @Description: 想要将数据通过 Thymeleaf 语法渲染到前端页面上，首先需要将数据获取并转发到对应的模板页面中，
	 * 需要在 Controller 方法中将查询到的数据放入 request 域中
	 * @Param: [params, request, session]
	 * @return: java.lang.String
	 */
	@GetMapping("/orders")
	public String orderListPage(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpSession session) {
		MallUserVO user = (MallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
		params.put("userId", user.getUserId());
		if (StringUtils.isEmpty(params.get("page"))) {
			params.put("page", 1);
		}
		params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
		PageUtil pageUtil = new PageUtil(params);
		PageResult orderPageResult = mallOrderService.getMyOrders(pageUtil);
		request.setAttribute("orderPageResult", orderPageResult);
		request.setAttribute("path", "orders");
		return "mall/my-orders";
	}

	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession httpSession) {
		MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
		List<MallShoppingCartItemVO> myShoppingCartItems = mallShoppingCartService.getMyShoppingCartItems(user.getUserId());
		if (StringUtils.isEmpty(user.getAddress().trim())) {
			//无收货地址
			MallException.fail(ServiceResultEnum.NULL_ADDRESS_ERROR.getResult());
		}
		if (CollectionUtils.isEmpty(myShoppingCartItems)) {
			//购物车中无数据则跳转至错误页
			MallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
		}
		//保存订单并返回订单号
		String saveOrderResult = mallOrderService.saveOrder(user, myShoppingCartItems);
		//跳转到订单详情页
		return "redirect:/orders/" + saveOrderResult;
	}


	@PutMapping("/orders/{orderNo}/cancel")
	@ResponseBody
	public Result cancelOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
		MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
		String cancelOrderResult =mallOrderService.cancelOrder(orderNo,user.getUserId());
		if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(cancelOrderResult);
		}
	}

	@PutMapping("/orders/{orderNo}/finish")
	@ResponseBody
	public Result finishOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
		MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
		String finishOrderResult = mallOrderService.finishOrder(orderNo,user.getUserId());
		if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(finishOrderResult);
		}
	}

	@GetMapping("/selectPayType")
	public String selectPayType(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession) {
		MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
		MallOrder mallOrder=mallOrderService.getNewBeeMallOrderByOrderNo(orderNo);
		request.setAttribute("orderNo", orderNo);
		request.setAttribute("totalPrice", mallOrder.getTotalPrice());
		return "mall/pay-select";
	}

	@GetMapping("/payPage")
	public String payOrder(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession, @RequestParam("payType") int payType) {
		MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
		MallOrder mallOrder=mallOrderService.getNewBeeMallOrderByOrderNo(orderNo);
		request.setAttribute("orderNo", orderNo);
		request.setAttribute("totalPrice", mallOrder.getTotalPrice());
		if(payType==1){
			return "mall/alipay";
		} else {
			return "mall/wxpay";
		}
	}



	@GetMapping("/paySuccess")
	@ResponseBody
	public Result paySuccess(@RequestParam("orderNo") String orderNo, @RequestParam("payType") int payType) {
		String payResult = mallOrderService.paySuccess(orderNo,payType);
		if (ServiceResultEnum.SUCCESS.getResult().equals(payResult)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(payResult);
		}
	}
}

