package com.coding.controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: 订单详情页页面VO
 * @author: Black Tom
 * @create: 2020-07-18 02:17
 **/
@Data
public class MallOrderDetailVO implements Serializable {

	private String orderNo;

	private Integer totalPrice;

	private Byte payStatus;

	//订单状态
	private String payStatusString;

	private Byte payType;

	//支付方式
	private String payTypeString;

	private Date payTime;

	//订单状态对应的文案
	private Byte orderStatus;

	private String orderStatusString;

	private String userAddress;

	private Date createTime;

	//订单项列表
	private List<MallOrderItemVO> mallOrderItemVOS;
}
