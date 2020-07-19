package com.coding.service;

import com.coding.controller.vo.IndexCategoryVO;
import com.coding.controller.vo.SearchPageCategoryVO;
import com.coding.entity.GoodsCategory;
import com.coding.util.PageResult;
import com.coding.util.PageUtil;

import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-12 15:59
 **/
public interface GoodsCategoryService {

	/**
	 * @Description: 根据categoryLevel, parentId对未删除category进行查询，并按照categoryRank降序显示当前页
	 * @Param: [pageUtil]
	 * @return: com.coding.util.PageResult
	 */
	public PageResult getCategoriesPage(PageUtil pageUtil);

	String saveCategory(GoodsCategory goodsCategory);

	String updateGoodsCategory(GoodsCategory goodsCategory);

	GoodsCategory getGoodsCategoryById(Long id);

	Boolean deleteBatch(Integer[] ids);

	/**
	 * 根据parentId和level获取分类列表
	 *
	 * @param parentIds
	 * @param categoryLevel
	 * @return
	 */
	List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);

	//获取首页分类数据
	List<IndexCategoryVO> getCategoriesForIndex();

	SearchPageCategoryVO getCategoriesForSearch(Long categoryId);
}
