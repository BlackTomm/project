package com.coding.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-16 21:08
 **/
@Data
public class MallShoppingCartItemVO implements Serializable {

	private Long cartItemId;

	private Long goodsId;

	private Integer goodsCount;

	private String goodsName;

	private String goodsCoverImg;

	private Integer sellingPrice;
}
