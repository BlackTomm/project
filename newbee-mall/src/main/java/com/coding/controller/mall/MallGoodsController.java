package com.coding.controller.mall;

import com.coding.common.Constants;
import com.coding.common.MallException;
import com.coding.common.ServiceResultEnum;
import com.coding.controller.vo.GoodsDetailVO;
import com.coding.controller.vo.SearchPageCategoryVO;
import com.coding.entity.Goods;
import com.coding.service.GoodsCategoryService;
import com.coding.service.GoodsService;
import com.coding.util.BeanUtil;
import com.coding.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-16 14:58
 **/
@Controller
public class MallGoodsController {

	@Resource
	private GoodsService goodsService;
	@Resource
	private GoodsCategoryService goodsCategoryService;

	/**
	 * @Description: 所有的传参我们都是用 Map 对象来接收，前端传过来的参数主要有： page、keyword、goodsCategoryId、orderBy
	 * 其中page 参数是分页所必需的字段，如果不传的话默认为第 1 页，keyword 参数是关键字，用来过滤商品名和商品简介，goodsCategoryId
	 * 参数是用来过滤商品分类 id 的字段，orderBy 则是排序字段，传过来不同的排序方式，返回的数据也会不同
	 * @Param: [params, request]
	 * @return: java.lang.String
	 */
	@GetMapping({"/search", "/search.html"})
	public String searchPage(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		if (StringUtils.isEmpty(params.get("page"))) {
			params.put("page", 1);
		}
		params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
		//封装数据
		if (params.containsKey("goodsCategoryId") && !StringUtils.isEmpty(params.get("goodsCategoryId") + "")) {
			Long categoryId = Long.valueOf(params.get("goodsCategoryId") + "");
			SearchPageCategoryVO searchPageCategoryVO = goodsCategoryService.getCategoriesForSearch(categoryId);
			if (searchPageCategoryVO != null) {
				request.setAttribute("goodsCategoryId", categoryId);
				request.setAttribute("searchPageCategoryVO", searchPageCategoryVO);
			}
		}
		//封装参数供前端回显
		if (params.containsKey("orderBy") && !StringUtils.isEmpty(params.get("orderBy") + "")) {
			request.setAttribute("orderBy", params.get("orderBy") + "");
		}
		String keyword = "";
		//对keyword做过滤 去掉空格
		if (params.containsKey("keyword") && !StringUtils.isEmpty((params.get("keyword") + "").trim())) {
			keyword = params.get("keyword") + "";
		}
		request.setAttribute("keyword", keyword);
		params.put("keyword", keyword);
		//搜索上架状态下的商品
		params.put("goodsSellStatus", Constants.SELL_STATUS_UP);
		//封装商品数据
		PageUtil pageUtil = new PageUtil(params);
		System.out.println(pageUtil);
		request.setAttribute("pageResult", goodsService.searchNewBeeMallGoods(pageUtil));
		return "mall/search";
	}

	@GetMapping("/goods/detail/{goodsId}")
	public String detailPage(@PathVariable("goodsId") Long goodsId, HttpServletRequest request) {
		if (goodsId < 1) {
			return "error/error_5xx";
		}
		Goods goods = goodsService.getNewBeeMallGoodsById(goodsId);
		if (goods == null) {
			MallException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
		}
		if (Constants.SELL_STATUS_UP != goods.getGoodsSellStatus()) {
			MallException.fail(ServiceResultEnum.GOODS_PUT_DOWN.getResult());
		}

		GoodsDetailVO goodsDetailVO = new GoodsDetailVO();
		BeanUtil.copyProperties(goods, goodsDetailVO);
		goodsDetailVO.setGoodsCarouselList(goods.getGoodsCarousel().split(","));
		request.setAttribute("goodsDetail", goodsDetailVO);
		return "mall/detail";
	}

}
