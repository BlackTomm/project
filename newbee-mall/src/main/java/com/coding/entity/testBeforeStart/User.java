package com.coding.entity.testBeforeStart;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-06 23:05
 **/
@Data
@Builder
@TableName(value = "tb_user")
public class User {
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	@TableField("name")
	private String userName;
	@TableField("password")
	private String password;
}
