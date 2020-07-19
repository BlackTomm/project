package com.coding.service;

import com.coding.controller.vo.MallOrderDetailVO;
import com.coding.controller.vo.MallOrderItemVO;
import com.coding.controller.vo.MallShoppingCartItemVO;
import com.coding.controller.vo.MallUserVO;
import com.coding.entity.MallOrder;
import com.coding.util.PageResult;
import com.coding.util.PageUtil;

import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-18 00:22
 **/
public interface MallOrderService {

	String saveOrder(MallUserVO user, List<MallShoppingCartItemVO> myShoppingCartItems);

	MallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

	/**
	* @Description: 我的订单列表
	* @Param: [pageUtil]
	* @return: com.coding.util.PageResult
	*/
	PageResult getMyOrders(PageUtil pageUtil);

	String cancelOrder(String orderNo, Long userId);

	String finishOrder(String orderNo, Long userId);

	MallOrder getNewBeeMallOrderByOrderNo(String orderNo);

	String paySuccess(String orderNo, int payType);

	/**
	* @Description: 后台分页
	* @Param: [pageUtil]
	* @return: com.coding.util.PageResult
	*/
	PageResult getNewBeeMallOrdersPage(PageUtil pageUtil);

	/**
	* @Description: 订单信息修改
	* @Param: [mallOrder]
	* @return: java.lang.String
	*/
	String updateOrderInfo(MallOrder mallOrder);

	List<MallOrderItemVO> getOrderItems(Long OrderId);

	String checkDone(Long[] ids);

	String checkOut(Long[] ids);

	String closeOrder(Long[] ids);
}
