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
 * @create: 2020-07-10 17:00
 **/
@Data
@Builder
	@NoArgsConstructor
	@AllArgsConstructor
@TableName("tb_newbee_mall_carousel")
@Accessors(chain = true)
public class Carousel {
	@TableId(value = "carousel_id",type = IdType.AUTO)
	private Integer carouselId;

	@TableField("carousel_url")
	private String carouselUrl;

	@TableField("redirect_url")
	private String redirectUrl;

	@TableField("carousel_rank")
	private Integer carouselRank;

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
