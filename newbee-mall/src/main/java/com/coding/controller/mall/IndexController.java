package com.coding.controller.mall;

import com.coding.common.Constants;
import com.coding.common.IndexConfigTypeEnum;
import com.coding.controller.vo.IndexCarouselVO;
import com.coding.controller.vo.IndexCategoryVO;
import com.coding.controller.vo.IndexConfigGoodsVO;
import com.coding.service.CarouselService;
import com.coding.service.GoodsCategoryService;
import com.coding.service.IndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-15 11:51
 **/
@Controller
public class IndexController {

	@Resource
	private CarouselService carouselService;
	@Resource
	private GoodsCategoryService goodsCategoryService;

	@Resource
	private IndexConfigService indexConfigService;

	/**
	 * @Description: 首页将依次展示 商品一级及二级分类；轮播图；热门商品；新品；推荐商品。
	 * @Param: [request]
	 * @return: java.lang.String
	 */
	@GetMapping({"/index", "/", "/index.html"})
	public String indexPage(HttpServletRequest request) {
		//侧边栏商品分类数据
		List<IndexCategoryVO> categories = goodsCategoryService.getCategoriesForIndex();
		if (CollectionUtils.isEmpty(categories)) {
			return "error/error_5xx";
		}
		//获取首页轮播图
		List<IndexCarouselVO> carousels = carouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
		//热门商品
		List<IndexConfigGoodsVO> hotGoodses = indexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(),
				Constants.INDEX_GOODS_HOT_NUMBER);
			List<IndexConfigGoodsVO> newGoodses = indexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(),
				Constants.INDEX_GOODS_NEW_NUMBER);
		List<IndexConfigGoodsVO> recommendGoodses = indexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMEND.getType(),
				Constants.INDEX_GOODS_RECOMMEND_NUMBER);
		request.setAttribute("categories", categories);//分类数据
		request.setAttribute("carousels", carousels);//轮播图
		request.setAttribute("hotGoodses", hotGoodses);//热销商品
		request.setAttribute("newGoodses", newGoodses);//新品
		request.setAttribute("recommendGoodses", recommendGoodses);//推荐商品
		return "mall/index";
	}
}
