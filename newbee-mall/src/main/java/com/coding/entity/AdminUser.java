package com.coding.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static org.apache.ibatis.type.JdbcType.VARCHAR;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-08 22:27
 **/
@TableName(value = "newbeemall_admin_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class AdminUser {

	@TableId("admin_user_id")
	private Integer adminUserId;

	@TableField(value = "login_user_name", jdbcType = VARCHAR)
	private String loginUserName;

	@TableField(value = "login_user_password", jdbcType = VARCHAR)
	private String loginPassword;

	@TableField("nick_name")
	private String nickName;

	@TableField("locked")
	private Byte locked;

}
