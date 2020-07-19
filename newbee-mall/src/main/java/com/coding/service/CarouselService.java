package com.coding.service;

import com.coding.controller.vo.IndexCarouselVO;
import com.coding.entity.Carousel;
import com.coding.util.PageResult;
import com.coding.util.PageUtil;

import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-10 16:56
 **/
public interface CarouselService {

	/**
	 * @Description: 获取轮播图分页列表接口
	 * @Param: [pageUtil]
	 * @return: com.coding.util.PageResult
	 */
	PageResult getCarouselPage(PageUtil pageUtil);

	/**
	* @Description: 添加轮播图接口
	* @Param: [carousel]
	* @return: java.lang.String
	*/
	String saveCarousel(Carousel carousel);

	/**
	* @Description: 修改轮播图接口
	* @Param: [carousel]
	* @return: java.lang.String
	*/
	String updateCarousel(Carousel carousel);

	/**
	* @Description: 根据 id 获取单条轮播图记录接口
	* @Param: [id]
	* @return: com.coding.entity.Carousel
	*/
	Carousel getCarouselById(Integer id);

	/**
	* @Description: 批量删除轮播图接口
	* @Param: [ids]
	* @return: java.lang.Boolean
	*/
	Boolean deletedBatch(Integer[] ids);

	/**
	* @Description: 返回优先级较高和指定数目的轮播图至首页
	* @Param: [number]
	* @return: java.util.List<com.coding.controller.vo.IndexCarouselVO>
	*/
	List<IndexCarouselVO> getCarouselsForIndex(int number);
}
