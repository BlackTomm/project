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
 * @create: 2020-07-18 01:51
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("tb_newbee_mall_order_item")
public class MallOrderItem {

	@TableId(value = "order_item_id",type = IdType.AUTO)
	private Long orderItemId;

	@TableField("order_id")
	private Long orderId;

	@TableField("goods_id")
	private Long goodsId;

	@TableField("goods_name")
	private String goodsName;

	@TableField("goods_cover_img")
	private String goodsCoverImg;

	@TableField("selling_price")
	private Integer sellingPrice;

	@TableField("goods_count")
	private Integer goodsCount;

	@TableField("create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
}
