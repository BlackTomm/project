package com.coding.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coding.common.CategoryLevelEnum;
import com.coding.common.Constants;
import com.coding.common.ServiceResultEnum;
import com.coding.controller.vo.IndexCategoryVO;
import com.coding.controller.vo.SearchPageCategoryVO;
import com.coding.controller.vo.SecondLevelCategoryVO;
import com.coding.controller.vo.ThirdLevelCategoryVO;
import com.coding.entity.GoodsCategory;
import com.coding.mapper.GoodsCategoryMapper;
import com.coding.service.GoodsCategoryService;
import com.coding.util.BeanUtil;
import com.coding.util.PageResult;
import com.coding.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-12 16:00
 **/
@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService {

	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;

	@Override
	public PageResult getCategoriesPage(PageUtil pageUtil) {
		List<GoodsCategory> goodsCategories = goodsCategoryMapper.findGoodsCategoryList(pageUtil);
		int total = goodsCategoryMapper.getTotalGoodsCategories(pageUtil);
		PageResult pageResult = new PageResult(goodsCategories, total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;
	}

	@Override
	public String saveCategory(GoodsCategory goodsCategory) {
		GoodsCategory temp = goodsCategoryMapper.selectByLevelAndName(goodsCategory.getCategoryLevel(), goodsCategory.getCategoryName());
		if (temp != null) {
			return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
		}
		if (goodsCategoryMapper.insert(goodsCategory) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public String updateGoodsCategory(GoodsCategory goodsCategory) {
		//更新前查看是否存在
		GoodsCategory temp = goodsCategoryMapper.selectByPrimaryKey(goodsCategory.getCategoryId());
		if (temp == null) {
			return ServiceResultEnum.DATA_NOT_EXIST.getResult();
		}
		GoodsCategory temp2 = goodsCategoryMapper.selectByLevelAndName(goodsCategory.getCategoryLevel(), goodsCategory.getCategoryName());
		if (temp2 != null && !temp.getCategoryId().equals(goodsCategory.getCategoryId())) {
			//存在id不同的相同分类名，不能继续修改
			return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
		}
		goodsCategory.setUpdateTime(new Date());
		if (goodsCategoryMapper.updateById(goodsCategory) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public GoodsCategory getGoodsCategoryById(Long id) {
		return goodsCategoryMapper.selectByPrimaryKey(id);
	}

	@Override
	public Boolean deleteBatch(Integer[] ids) {
		if (ids.length < 1) {
			return false;
		}
		List<Integer> idList = new ArrayList<>(Arrays.asList(ids));
		int updateNumbers = idList.stream().mapToInt(id -> goodsCategoryMapper.update(
				new GoodsCategory().setIsDeleted(1).setUpdateTime(new Date()),
				new QueryWrapper<GoodsCategory>()
						.lambda().eq(GoodsCategory::getCategoryId, id))).sum();
		return updateNumbers == ids.length;
	}

	@Override
	public List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel) {
		return goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(parentIds, categoryLevel, 0);//0代表查询所有
	}

	/**
	 * @Description: 获取各级分类下指定数目的子类，相比后台的三级级联响应的最大区别在；先指定数目的一级分类，
	 * 再查询对应一级子类下指定数目的二级子类下，依次扩散
	 * <p>
	 * 商城首页是将所有三级分类数据读取并渲染到页面上，之后通过 js 代码来实现二级分类和三级分类数据的显示，
	 * 所以这里就将所有的数据都读取出来并封装成一个对象返回给视图层
	 * @Param: []
	 * @return: java.util.List<com.coding.controller.vo.IndexCategoryVO>
	 */
	@Override
	public List<IndexCategoryVO> getCategoriesForIndex() {
		List<IndexCategoryVO> indexCategoryVOS = new ArrayList<>();
		//获取固定数量的一级分类数据
		List<GoodsCategory> firstLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(
				Collections.singletonList(0L),
				CategoryLevelEnum.LEVEL_ONE.getLevel(),
				Constants.INDEX_CATEGORY_NUMBER);
		if (!CollectionUtils.isEmpty(firstLevelCategories)) {
			List<Long> firstLevelCategoryIds = firstLevelCategories.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());
			//获取二级分类的数据
			List<GoodsCategory> secondLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(firstLevelCategoryIds, CategoryLevelEnum.LEVEL_TWO.getLevel(), 0);
			if (!CollectionUtils.isEmpty(secondLevelCategories)) {
				List<Long> secondLevelCategoryIds = secondLevelCategories.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());
				//获取三级分类的数据
				List<GoodsCategory> thirdLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(secondLevelCategoryIds, CategoryLevelEnum.LEVEL_THREE.getLevel(), 0);
				if (!CollectionUtils.isEmpty(thirdLevelCategories)) {
					//根据 parentId 将 thirdLevelCategories 分组,以二级分类id作为key值
					Map<Long, List<GoodsCategory>> thirdLevelCategoryMap = thirdLevelCategories.stream().collect(groupingBy(GoodsCategory::getParentId));

					//处理二级分类
					List<SecondLevelCategoryVO> secondLevelCategoryVOS = new ArrayList<>();
					for (GoodsCategory secondCategory : secondLevelCategories) {
						SecondLevelCategoryVO secondLevelCategoryVO = new SecondLevelCategoryVO();
						BeanUtil.copyProperties(secondCategory, secondLevelCategoryVO);
						if (thirdLevelCategoryMap.containsKey(secondCategory.getCategoryId())) {
							List<GoodsCategory> tempGoodsCategories = thirdLevelCategoryMap.get(secondCategory.getCategoryId());
							secondLevelCategoryVO.setThirdLevelCategoryVOS(BeanUtil.copyList(tempGoodsCategories, ThirdLevelCategoryVO.class));
							secondLevelCategoryVOS.add(secondLevelCategoryVO);
						}
					}

					//处理一级分类
					if (!CollectionUtils.isEmpty(secondLevelCategoryVOS)) {
						//根据 parentId 将 secondLevelCategories 分组
						Map<Long, List<SecondLevelCategoryVO>> secondLevelCategoryVOMap = secondLevelCategoryVOS.stream()
								.collect(groupingBy(SecondLevelCategoryVO::getParentId));
						for (GoodsCategory firstCategory : firstLevelCategories) {
							IndexCategoryVO indexCategoryVO = new IndexCategoryVO();
							BeanUtil.copyProperties(firstCategory, indexCategoryVO);
							//如果该一级分类下有数据则放入 newBeeMallIndexCategoryVOS 对象中
							if (secondLevelCategoryVOMap.containsKey(firstCategory.getCategoryId())) {
								List<SecondLevelCategoryVO> tempGoodsCategories = secondLevelCategoryVOMap.get(firstCategory.getCategoryId());
								indexCategoryVO.setSecondLevelCategoryVOS(tempGoodsCategories);
								indexCategoryVOS.add(indexCategoryVO);
							}
						}
					}
				}
			}
			return indexCategoryVOS;
		} else {
			return null;
		}
	}

	@Override
	public SearchPageCategoryVO getCategoriesForSearch(Long categoryId) {
		SearchPageCategoryVO searchPageCategoryVO = new SearchPageCategoryVO();
		GoodsCategory thirdLevelGoodsCategory = goodsCategoryMapper.selectByPrimaryKey(categoryId);
		//判断是否是三级分类
		if (thirdLevelGoodsCategory != null && thirdLevelGoodsCategory.getCategoryLevel() == CategoryLevelEnum.LEVEL_THREE.getLevel()) {
			//获取二级分类
			GoodsCategory secondLevelGoodsCategory = goodsCategoryMapper.selectByPrimaryKey(thirdLevelGoodsCategory.getParentId());
			if (secondLevelGoodsCategory != null && secondLevelGoodsCategory.getCategoryLevel() == CategoryLevelEnum.LEVEL_TWO.getLevel()){
				List<GoodsCategory> thirdLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(
						Collections.singletonList(secondLevelGoodsCategory.getCategoryId()),
								CategoryLevelEnum.LEVEL_THREE.getLevel(),
								Constants.SEARCH_CATEGORY_NUMMBER);
				searchPageCategoryVO.setCurrentCategoryName(thirdLevelGoodsCategory.getCategoryName());
				searchPageCategoryVO.setSecondLevelCategoryName(secondLevelGoodsCategory.getCategoryName());
				searchPageCategoryVO.setThirdLevelCategoryList(thirdLevelCategories);
				return searchPageCategoryVO;
			}
		}
		return null;
	}
}
