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
 * @description: 首页配置
 * @author: Black Tom
 * @create: 2020-07-15 19:28
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("tb_newbee_mall_index_config")
public class IndexConfig {
	@TableId(value = "config_id",type = IdType.AUTO)
	private Long configId;

	@TableField("config_name")
	private String configName;

	@TableField("config_type")
	private Byte configType;

	@TableField(value = "goods_id")
	private Long goodsId;

	@TableField("redirect_url")
	private String redirectUrl;

	@TableField("config_rank")
	private Integer configRank;

	@TableField("is_deleted")
	private int isDeleted;

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
