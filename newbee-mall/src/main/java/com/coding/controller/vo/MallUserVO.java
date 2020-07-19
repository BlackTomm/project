package com.coding.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-16 01:02
 **/
@Data
public class MallUserVO implements Serializable {

	private Long userId;

	private String nickName;

	private String loginName;

	private String introduceSign;

	private String address;

	private int shopCartItemCount;
}
