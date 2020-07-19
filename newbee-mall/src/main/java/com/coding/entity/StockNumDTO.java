package com.coding.entity;

import lombok.Data;

/**
 * @description: 库存修改所需实体
 * @author: Black Tom
 * @create: 2020-07-18 01:19
 **/
@Data
public class StockNumDTO {

	private Long goodsId;

	private Integer goodsCount;
}
