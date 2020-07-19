package com.coding.controller.admin;

import com.coding.common.CategoryLevelEnum;
import com.coding.common.ServiceResultEnum;
import com.coding.entity.GoodsCategory;
import com.coding.service.GoodsCategoryService;
import com.coding.util.PageUtil;
import com.coding.util.Result;
import com.coding.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-12 15:58
 **/
@Controller
@RequestMapping("/admin")
public class GoodsCategoryController {

	@Resource
	private GoodsCategoryService goodsCategoryService;

	@GetMapping("/categories")
	public String categoriesPage(HttpServletRequest request, @RequestParam("categoryLevel") Byte categoryLevel, @RequestParam("parentId") Long parentId, @RequestParam("backParentId") Long backParentId) {
		if (categoryLevel == null || categoryLevel < 1 || categoryLevel > 3) {
			return "error/error_5xx";
		}
		request.setAttribute("path", "newbee_mall_category");
		request.setAttribute("parentId", parentId);
		request.setAttribute("backParentId", backParentId);
		request.setAttribute("categoryLevel", categoryLevel);
		return "admin/category";
	}

	/**
	 * 商品类目列表
	 */
	@RequestMapping(value = "/categories/list", method = RequestMethod.GET)
	@ResponseBody
	public Result list(@RequestParam Map<String, Object> params) {
		if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit")) || StringUtils.isEmpty(params.get("categoryLevel")) || StringUtils.isEmpty(params.get("parentId"))) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		PageUtil pageUtil = new PageUtil(params);
		return ResultGenerator.genSuccessResult(goodsCategoryService.getCategoriesPage(pageUtil));
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/categories/save", method = RequestMethod.POST)
	@ResponseBody
	public Result save(@RequestBody GoodsCategory goodsCategory) {
		if (Objects.isNull(goodsCategory.getCategoryLevel())
				|| StringUtils.isEmpty(goodsCategory.getCategoryName())
				|| Objects.isNull(goodsCategory.getParentId())
				|| Objects.isNull(goodsCategory.getCategoryRank())) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		String result = goodsCategoryService.saveCategory(goodsCategory);
		if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(result);
		}
	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/categories/update", method = RequestMethod.POST)
	@ResponseBody
	public Result update(@RequestBody GoodsCategory goodsCategory) {
		if (Objects.isNull(goodsCategory.getCategoryId())
				|| Objects.isNull(goodsCategory.getCategoryLevel())
				|| StringUtils.isEmpty(goodsCategory.getCategoryName())
				|| Objects.isNull(goodsCategory.getParentId())
				|| Objects.isNull(goodsCategory.getCategoryRank())) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		String result = goodsCategoryService.updateGoodsCategory(goodsCategory);
		if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(result);
		}
	}

	@RequestMapping(value = "/categories/delete", method = RequestMethod.POST)
	@ResponseBody
	public Result delete(@RequestBody Integer[] ids) {
		if (ids.length < 1) {
			return ResultGenerator.genFailResult("参数异常");
		}
		if (goodsCategoryService.deleteBatch(ids)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult("删除失败");
		}
	}

	/**
	 * 详情
	 */
	@GetMapping("/categories/info/{id}")
	@ResponseBody
	public Result info(@PathVariable("id") Long id) {
		GoodsCategory goodsCategory = goodsCategoryService.getGoodsCategoryById(id);
		if (goodsCategory == null) {
			return ResultGenerator.genFailResult("未查询到数据");
		}
		return ResultGenerator.genSuccessResult(goodsCategory);
	}

	/*--------------------------------------三级联动测试--------------------------------------*/

	@GetMapping("/coupling-test")
	public String couplingTest(HttpServletRequest request) {
		request.setAttribute("path", "coupling-test");
		//查询所有一级分类
		List<GoodsCategory> firstLevelCategories = goodsCategoryService.selectByLevelAndParentIdsAndNumber(
				Collections.singletonList(0L), CategoryLevelEnum.LEVEL_ONE.getLevel());
		if (!CollectionUtils.isEmpty(firstLevelCategories)) {
			//查询一级分类列表中第一个实体的所有二级分类
			List<GoodsCategory> secondLevelCategories = goodsCategoryService.selectByLevelAndParentIdsAndNumber(
					Collections.singletonList(firstLevelCategories.get(0).getCategoryId()),
					CategoryLevelEnum.LEVEL_TWO.getLevel());
			if (!CollectionUtils.isEmpty(secondLevelCategories)) {
				//查询二级分类列表中第一个实体的所有三级分类
				List<GoodsCategory> thirdLevelCategories = goodsCategoryService.selectByLevelAndParentIdsAndNumber(
						Collections.singletonList(secondLevelCategories.get(0).getCategoryId()),
						CategoryLevelEnum.LEVEL_THREE.getLevel());
				request.setAttribute("firstLevelCategories", firstLevelCategories);
				request.setAttribute("secondLevelCategories", secondLevelCategories);
				request.setAttribute("thirdLevelCategories", thirdLevelCategories);
				return "admin/coupling-test";
			}
		}
		return "error/error_5xx";
	}

	/**
	 * @Description: 该接口的功能就是根据前端传过来的分类 id 来获取该分类下的下级分类数据，如果该分类是一级分类，
	 * 则返回该一级分类下的所有二级分类列表数据和第一个二级分类下的所有三级分类数据，如果该分类是二级分类，则返回
	 * 该二级分类下的所有三级分类列表数据（最大只有三级分类）
	 * @Param: [categoryId]
	 * @return: com.coding.util.Result
	 */
	@RequestMapping(value = "/categories/listForSelect", method = RequestMethod.GET)
	@ResponseBody
	public Result listForSelect(@RequestParam("categoryId") Long categoryId) {
		if (categoryId == null || categoryId < 1) {
			return ResultGenerator.genFailResult("缺少参数");
		}
		GoodsCategory category = goodsCategoryService.getGoodsCategoryById(categoryId);
		if (category == null || category.getCategoryLevel() == CategoryLevelEnum.LEVEL_THREE.getLevel()) {
			return ResultGenerator.genFailResult("参数异常");
		}
		Map categoryResult = new HashMap(2);
		//检验是否是一级分类
		if (category.getCategoryLevel() == CategoryLevelEnum.LEVEL_ONE.getLevel()) {
			//如果是一级分类则返回当前一级分类下的所有二级分类，以及二级分类列表中第一条数据下的所有三级分类列表
			List<GoodsCategory> secondLevelCategories = goodsCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId),
					CategoryLevelEnum.LEVEL_TWO.getLevel());
			if (!CollectionUtils.isEmpty(secondLevelCategories)) {
				List<GoodsCategory> thirdLevelCategories = goodsCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()),
						CategoryLevelEnum.LEVEL_THREE.getLevel());
				categoryResult.put("secondLevelCategories", secondLevelCategories);
				categoryResult.put("thirdLevelCategories", thirdLevelCategories);
			}
		}
		//检验是否是二级分类
		if (category.getCategoryLevel() == CategoryLevelEnum.LEVEL_TWO.getLevel()) {
			List<GoodsCategory> thirdLevelCategories = goodsCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId),
					CategoryLevelEnum.LEVEL_THREE.getLevel());
			categoryResult.put("thirdLevelCategories", thirdLevelCategories);
		}
		return ResultGenerator.genSuccessResult(categoryResult);
	}

}
