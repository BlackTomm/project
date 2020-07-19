package com.coding.controller.admin;

import com.coding.common.CategoryLevelEnum;
import com.coding.common.Constants;
import com.coding.common.ServiceResultEnum;
import com.coding.entity.Goods;
import com.coding.entity.GoodsCategory;
import com.coding.service.GoodsCategoryService;
import com.coding.service.GoodsService;
import com.coding.util.PageUtil;
import com.coding.util.Result;
import com.coding.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-13 13:42
 **/
@Controller
@RequestMapping("/admin")
public class GoodsController {
	@Resource
	private GoodsCategoryService goodsCategoryService;
	@Resource
	private GoodsService goodsService;

	@GetMapping("/goods")
	public String goodPage(HttpServletRequest request) {
		request.setAttribute("path", "newbee_mall_goods");
		return "admin/goods";
	}

	@RequestMapping(value = "/goods/list", method = RequestMethod.GET)
	@ResponseBody
	public Result list(@RequestParam Map<String, Object> params) {
		if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		PageUtil pageUtil = new PageUtil(params);
		return ResultGenerator.genSuccessResult(goodsService.getNewBeeMallGoodsPage(pageUtil));
	}

	@GetMapping(value = "/goods/edit")
	public String edit(HttpServletRequest request) {
		request.setAttribute("path", "edit");
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
				request.setAttribute("path", "goods-edit");
				return "admin/goods_edit";
			}
		}
		return "error/error_5xx";
	}

	@GetMapping("/goods/edit/{goodsId}")
	public String edit(HttpServletRequest request, @PathVariable("goodsId") Long goodsId) {
		request.setAttribute("path", "edit");
		Goods goods = goodsService.getNewBeeMallGoodsById(goodsId);
		if (goods == null) {
			return "error/error_404";
		}
		//查看该商品是否已经分类
		if (goods.getGoodsCategoryId() > 0) {
			if (goods.getGoodsCategoryId() != null || goods.getGoodsCategoryId() > 0) {
				//由三级到一级逐级查询
				GoodsCategory currentGoodsCategory = goodsCategoryService.getGoodsCategoryById(goods.getGoodsCategoryId());
				//查询当前分类是否为三级分类,不为三级分类则是错误数据
				if (currentGoodsCategory != null && currentGoodsCategory.getCategoryLevel() == CategoryLevelEnum.LEVEL_THREE.getLevel()) {
					List<GoodsCategory> thirdLevelCategories = goodsCategoryService.selectByLevelAndParentIdsAndNumber(
							Collections.singletonList(currentGoodsCategory.getParentId()),
							CategoryLevelEnum.LEVEL_THREE.getLevel());
					GoodsCategory secondCategory = goodsCategoryService.getGoodsCategoryById(currentGoodsCategory.getCategoryId());
					if (secondCategory != null) {
						List<GoodsCategory> secondLevelCategories = goodsCategoryService.selectByLevelAndParentIdsAndNumber(
								Collections.singletonList(secondCategory.getParentId()),
								CategoryLevelEnum.LEVEL_TWO.getLevel());
						GoodsCategory firstCategory = goodsCategoryService.getGoodsCategoryById(secondCategory.getCategoryId());
						if (firstCategory != null) {
							List<GoodsCategory> firstLevelCategories = goodsCategoryService.selectByLevelAndParentIdsAndNumber(
									Collections.singletonList(0L),
									CategoryLevelEnum.LEVEL_ONE.getLevel());
							//所有分类数据都得到之后放到request对象中供前端读取
							request.setAttribute("firstLevelCategories", firstLevelCategories);
							request.setAttribute("secondLevelCategories", secondLevelCategories);
							request.setAttribute("thirdLevelCategories", thirdLevelCategories);
							request.setAttribute("firstLevelCategoryId", firstCategory.getCategoryId());
							request.setAttribute("secondLevelCategoryId", secondCategory.getCategoryId());
							request.setAttribute("thirdLevelCategoryId", currentGoodsCategory.getCategoryId());
						}
					}
				}
			}
		}

		//商品表中关联的分类 id 为 0，这种情况表示分类数据并未关联
		if (goods.getGoodsCategoryId() == 0) {
			//查询所有的一级分类
			List<GoodsCategory> firstLevelCategories = goodsCategoryService.selectByLevelAndParentIdsAndNumber(
					Collections.singletonList(0L),
					CategoryLevelEnum.LEVEL_ONE.getLevel());
			if (!CollectionUtils.isEmpty(firstLevelCategories)) {
				//查询一级分类列表中第一个实体的所有二级分类
				List<GoodsCategory> secondLevelCategories = goodsCategoryService.selectByLevelAndParentIdsAndNumber(
						Collections.singletonList(
								firstLevelCategories.get(0).getCategoryId()),
						CategoryLevelEnum.LEVEL_TWO.getLevel());
				if (!CollectionUtils.isEmpty(secondLevelCategories)) {
					//查询二级分类列表中第一个实体的所有三级分类
					List<GoodsCategory> thirdLevelCategories = goodsCategoryService.selectByLevelAndParentIdsAndNumber(
							Collections.singletonList(
									secondLevelCategories.get(0).getCategoryId()),
							CategoryLevelEnum.LEVEL_THREE.getLevel());
					request.setAttribute("firstLevelCategories", firstLevelCategories);
					request.setAttribute("secondLevelCategories", secondLevelCategories);
					request.setAttribute("thirdLevelCategories", thirdLevelCategories);
				}
			}
		}
		request.setAttribute("goods", goods);
		request.setAttribute("path", "goods-edit");
		return "admin/goods_edit";
	}

	@RequestMapping(value = "/goods/update", method = RequestMethod.POST)
	@ResponseBody
	public Result update(@RequestBody Goods goods) {
		if (Objects.isNull(goods.getGoodsId())
				|| StringUtils.isEmpty(goods.getGoodsName())
				|| StringUtils.isEmpty(goods.getGoodsIntro())
				|| StringUtils.isEmpty(goods.getTag())
				|| Objects.isNull(goods.getOriginalPrice())
				|| Objects.isNull(goods.getSellingPrice())
				|| Objects.isNull(goods.getGoodsCategoryId())
				|| Objects.isNull(goods.getStockNum())
				|| Objects.isNull(goods.getGoodsSellStatus())
				|| StringUtils.isEmpty(goods.getGoodsCoverImg())
				|| StringUtils.isEmpty(goods.getGoodsDetailContent())) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		String result = goodsService.updateNewBeeMallGoods(goods);
		if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(result);
		}
	}

	@RequestMapping(value = "/goods/save", method = RequestMethod.POST)
	@ResponseBody
	public Result save(@RequestBody Goods goods) {
		if (StringUtils.isEmpty(goods.getGoodsName())
				|| StringUtils.isEmpty(goods.getGoodsIntro())
				|| StringUtils.isEmpty(goods.getTag())
				|| Objects.isNull(goods.getGoodsCategoryId())
				|| Objects.isNull(goods.getOriginalPrice())
				|| Objects.isNull(goods.getSellingPrice())
				|| Objects.isNull(goods.getStockNum())
				|| Objects.isNull(goods.getGoodsSellStatus())
				|| StringUtils.isEmpty(goods.getGoodsCoverImg())
				|| StringUtils.isEmpty(goods.getGoodsDetailContent())) {
			return ResultGenerator.genFailResult("参数异常");
		}
		String result = goodsService.saveNewBeeMallGoods(goods);
		if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
			return ResultGenerator.genSuccessResult();
		}
		return ResultGenerator.genFailResult(result);
	}

	@RequestMapping(value = "/goods/status/{sellStatus}",method =RequestMethod.PUT)
	@ResponseBody
	public Result delete(@RequestBody Long[] ids, @PathVariable("sellStatus") byte sellStatus){
		if(ids.length<1){
			return ResultGenerator.genFailResult("参数异常");
		}
		if(sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN){
			return ResultGenerator.genFailResult("状态异常！");
		}
		if (goodsService.batchUpdateSellStatus(ids,sellStatus)){
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult("修改失败");
		}
	}
}
