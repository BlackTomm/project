package com.coding.controller.vo;

import com.coding.entity.GoodsCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 搜索页面分类数据VO
 * @author: Black Tom
 * @create: 2020-07-16 15:19
 **/
@Data
public class SearchPageCategoryVO implements Serializable {

	private String currentCategoryName;

	private String firstLevelCategoryName;

	private String secondLevelCategoryName;

	private List<GoodsCategory> secondLevelCategoryList;

	private List<GoodsCategory> thirdLevelCategoryList;


}
