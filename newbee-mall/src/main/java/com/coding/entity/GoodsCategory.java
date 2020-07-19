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
 * @create: 2020-07-12 15:44
 **/
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_newbee_mall_goods_category")
public class GoodsCategory {

	@TableId(value = "category_id",type = IdType.AUTO)
	private Long categoryId;

	@TableField("category_level")
	private Byte categoryLevel;

	@TableField("parent_id")
	private Long parentId;

	@TableField("category_name")
	private String categoryName;

	@TableField("category_rank")
	private Integer categoryRank;

	@TableField("is_deleted")
	private int isDeleted;

	@TableField("create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@TableField("create_user")
	private Integer createUser;

	@TableField("update_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	@TableField("update_user")
	private Integer updateUser;


}
