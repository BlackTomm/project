package com.coding.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coding.common.ServiceResultEnum;
import com.coding.controller.vo.IndexCarouselVO;
import com.coding.entity.Carousel;
import com.coding.mapper.CarouselMapper;
import com.coding.service.CarouselService;
import com.coding.util.BeanUtil;
import com.coding.util.PageResult;
import com.coding.util.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-10 17:23
 **/
@Service
public class CarouselServiceImpl implements CarouselService {

	@Resource
	private CarouselMapper carouselMapper;

	@Override
	public PageResult getCarouselPage(PageUtil pageUtil) {
		List<Carousel> carousels = carouselMapper.findCarouselList(pageUtil);
		int total = carouselMapper.getTotalCarousels(pageUtil);
		PageResult pageResult = new PageResult(carousels, total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;
	}

	@Override
	public String saveCarousel(Carousel carousel) {
//		System.out.println(carousel);
		if (carouselMapper.insert(carousel) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public String updateCarousel(Carousel carousel) {
		Carousel temp = carouselMapper.selectById(carousel.getCarouselId());
		if (temp == null) {
			return ServiceResultEnum.DATA_NOT_EXIST.getResult();
		}
		temp.setCarouselRank(carousel.getCarouselRank());
		temp.setRedirectUrl(carousel.getRedirectUrl());
		temp.setCarouselUrl(carousel.getCarouselUrl());
		temp.setUpdateTime(new Date());
		if (carouselMapper.updateById(temp) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public Carousel getCarouselById(Integer id) {
		return carouselMapper.selectById(id);
	}

	@Override
	public Boolean deletedBatch(Integer[] ids) {
		if (ids.length < 1) {
			return false;
		}
		List<Integer> idList = new ArrayList<>(Arrays.asList(ids));
		//此处需要对Carousel类设置 @Accessors(chain = true),意思是调用setter方法将返回对象而不是void
		int updateNumbers = idList.stream().mapToInt(id -> carouselMapper.update(
				new Carousel().setIsDeleted(1).setUpdateTime(new Date()),
				new QueryWrapper<Carousel>()
						.lambda().eq(Carousel::getCarouselId, id))).sum();
		return updateNumbers == ids.length;
	}

	@Override
	public List<IndexCarouselVO> getCarouselsForIndex(int number) {
		List<IndexCarouselVO> indexCarouselVOS = new ArrayList<>(number);
		List<Carousel> carousels = carouselMapper.findCarouselsByNum(number);
		if (!CollectionUtils.isEmpty(carousels)) {
			indexCarouselVOS = BeanUtil.copyList(carousels, IndexCarouselVO.class);
		}
		return indexCarouselVOS;
	}
}
