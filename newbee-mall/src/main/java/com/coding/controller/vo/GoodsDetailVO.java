package com.coding.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-16 18:41
 **/
@Data
public class GoodsDetailVO implements Serializable {

	private Long goodsId;

	private String goodsName;

	private String goodsIntro;

	private String goodsCoverImg;

	private String[] goodsCarouselList;

	private Integer sellingPrice;

	private Integer originalPrice;

	private String goodsDetailContent;
}
