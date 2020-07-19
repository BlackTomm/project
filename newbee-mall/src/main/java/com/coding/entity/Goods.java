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
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-13 14:54
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("tb_newbee_mall_goods_info")
public class Goods {
	@TableId(value = "goods_id",type = IdType.AUTO)
	private Long goodsId;

	@TableField("goods_name")
	private String goodsName;

	@TableField("goods_intro")
	private String goodsIntro;

	@TableField("goods_category_id")
	private Long goodsCategoryId;

	@TableField("goods_cover_img")
	private String goodsCoverImg;

	@TableField("goods_carousel")
	private String goodsCarousel;

	@TableField("goods_detail_content")
	private String goodsDetailContent;

	@TableField("original_price")
	private Integer originalPrice;

	@TableField("selling_price")
	private Integer sellingPrice;

	@TableField("stock_num")
	private Integer stockNum;

	@TableField("tag")
	private String tag;

	@TableField("goods_sell_status")
	private Byte goodsSellStatus;

	@TableField("create_user")
	private Integer createUser;

	@TableField("create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@TableField("update_user")
	private Integer updateUser;

	@TableField("update_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
}
