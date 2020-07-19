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
 * @create: 2020-07-16 00:53
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("tb_newbee_mall_user")
public class MallUser {
	@TableId(value = "user_id",type = IdType.AUTO)
	private Long userId;

	@TableField("nick_name")
	private String nickName;

	@TableField("login_name")
	private String loginName;

	@TableField("password_md5")
	private String passwordMd5;

	@TableField("introduce_sign")
	private String introduceSign;

	@TableField("address")
	private String address;

	@TableField("is_deleted")
	private Byte isDeleted;

	@TableField("locked_flag")
	private Byte lockedFlag;

	@TableField("create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

}