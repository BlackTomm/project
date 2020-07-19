package com.coding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coding.entity.Carousel;
import com.coding.util.PageUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-10 17:19
 **/
public interface CarouselMapper extends BaseMapper<Carousel> {

	@Select("select * from tb_newbee_mall_carousel where is_deleted = 0 order by carousel_rank desc limit #{start}, #{limit}")
	List<Carousel> findCarouselList(PageUtil pageUtil);

	/**
	* @Description: 获取轮播页面数目
	* @Param: [pageUtil]
	* @return: int
	*/
	@Select("select count(*) from tb_newbee_mall_carousel where is_deleted = 0")
	int getTotalCarousels(PageUtil pageUtil);

	/**
	* @Description: 查找优先级靠前的 number 张图片
	* @Param: [number]
	* @return: java.util.List<com.coding.entity.Carousel>
	*/
	@Select("select * from tb_newbee_mall_carousel where is_deleted = 0 order by carousel_rank desc limit #{number}")
	List<Carousel> findCarouselsByNum(int number);
}
