package com.coding.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-18 02:19
 **/
@Data
public class MallOrderItemVO implements Serializable {

	private Long goodsId;

	private Integer goodsCount;

	private String goodsName;

	private String goodsCoverImg;

	private Integer sellingPrice;
}
