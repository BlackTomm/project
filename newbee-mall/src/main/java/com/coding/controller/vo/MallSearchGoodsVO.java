package com.coding.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-16 01:42
 **/
@Data
public class MallSearchGoodsVO implements Serializable {

	private Long goodsId;

	private String goodsName;

	private String goodsIntro;

	private String goodsCoverImg;

	private Integer sellingPrice;
}
