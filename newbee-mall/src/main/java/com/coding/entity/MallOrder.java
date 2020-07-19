package com.coding.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-18 01:37
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("tb_newbee_mall_order")
public class MallOrder {

	@TableId(value = "order_id",type = IdType.AUTO)
	private Long orderId;

	@TableField("order_no")
	private String orderNo;

	@TableField("user_id")
	private Long userId;

	@TableField("total_price")
	private Integer totalPrice;

	@TableField("pay_status")
	private Byte payStatus;

	@TableField("pay_type")
	private Byte payType;

	@TableField("pay_time")
	private Date payTime;

	/**
	 * @Description: 订单状态：0.待支付; 1.已支付; 2.配货完成; 3.出库成功; 4.交易成功; -1.手动关闭; -2.超时关闭; -3.商家关闭
	 */
	@TableField("order_status")
	private Byte orderStatus;

	@TableField("extra_info")
	private String extraInfo;

	@TableField("user_address")
	private String userAddress;

	@TableField("is_deleted")
	private Byte isDeleted;

	@TableField("create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@TableField("update_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
}
