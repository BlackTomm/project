package com.coding.controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: 订单列表页面VO
 * @author: Black Tom
 * @create: 2020-07-18 19:56
 **/
@Data
public class MallOrderListVO implements Serializable {
	private Long orderId;

	private String orderNo;

	private Integer totalPrice;

	private Byte payType;

	private Byte orderStatus;

	private String orderStatusString;

	private String userAddress;

	private Date createTime;

	private List<MallOrderItemVO> mallOrderItemVOS;
}
