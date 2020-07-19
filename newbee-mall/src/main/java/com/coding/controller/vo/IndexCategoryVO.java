package com.coding.controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 首页分类数据VO
 * @author: Black Tom
 * @create: 2020-07-15 15:29
 **/
@Data
public class IndexCategoryVO implements Serializable {

	private Long categoryId;

	private Byte categoryLevel;

	private String categoryName;

	private List<SecondLevelCategoryVO> secondLevelCategoryVOS;
}
