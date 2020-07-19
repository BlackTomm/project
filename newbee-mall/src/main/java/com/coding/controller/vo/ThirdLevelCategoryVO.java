package com.coding.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 首页分类数据VO(第三级)
 * @author: Black Tom
 * @create: 2020-07-15 15:36
 **/
@Data
public class ThirdLevelCategoryVO implements Serializable {

	private Long categoryId;

	private Byte categoryLevel;

	private String categoryName;
}
