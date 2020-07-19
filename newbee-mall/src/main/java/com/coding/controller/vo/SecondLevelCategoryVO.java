package com.coding.controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 首页分类数据VO(第二级)
 * @author: Black Tom
 * @create: 2020-07-15 15:33
 **/
@Data
public class SecondLevelCategoryVO implements Serializable {
	private Long categoryId;

	private Long parentId;

	private Byte categoryLevel;

	private String categoryName;

	private List<ThirdLevelCategoryVO> thirdLevelCategoryVOS;
}
