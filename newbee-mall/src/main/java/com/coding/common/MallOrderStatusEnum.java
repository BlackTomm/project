package com.coding.common;

/**
 * @description: 订单状态:0.待支付 1.已支付 2.配货完成 3:出库成功 4.交易成功 -1.手动关闭 -2.超时关闭 -3.商家关闭
 * @author: Black Tom
 * @create: 2020-07-18 09:48
 **/
public enum MallOrderStatusEnum {

	DEFAULT(-9, "ERROR"),
	ORDER_PRE_PAY(0, "待支付"),
	ORDER_PAID(1, "已支付"),
	ORDER_PACKAGED(2, "配货完成"),
	ORDER_EXPRESS(3, "出库成功"),
	ORDER_SUCCESS(4, "交易成功"),
	ORDER_CLOSED_BY_MALLUSER(-1, "手动关闭"),
	ORDER_CLOSED_BY_EXPIRED(-2, "超时关闭"),
	ORDER_CLOSED_BY_MERCHANT(-3, "商家关闭");

	private int orderStatus;

	private String name;

	MallOrderStatusEnum(int orderStatus, String name) {
		this.orderStatus = orderStatus;
		this.name = name;
	}

	public static MallOrderStatusEnum getMallOrderStatusEnumByStatus(int orderStatus) {
		for (MallOrderStatusEnum mallOrderStatusEnum : MallOrderStatusEnum.values()) {
			if (mallOrderStatusEnum.getOrderStatus() == orderStatus) {
				return mallOrderStatusEnum;
			}
		}
		return DEFAULT;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
