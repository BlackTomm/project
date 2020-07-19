package com.coding.service.impl;

import com.coding.entity.Carousel;
import com.coding.mapper.CarouselMapper;
import com.coding.service.CarouselService;
import com.coding.util.PageUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class CarouselServiceImplTest {

	@Resource
	private CarouselMapper carouselMapper;

	@Autowired
	private CarouselService carouselService;

	@Test
	void getCarouselPage() {
		Map<String,Object> params = new LinkedHashMap<>();
		params.put("page", 1);
		params.put("limit", 6);
		PageUtil pageUtil= new PageUtil(params);
		carouselService.getCarouselPage(pageUtil);
	}

	@Test
	void saveCarousel() {
		Carousel carousel = Carousel.builder().carouselId(9)
				.carouselUrl("https://newbee-mall.oss-cn-beijing.aliyuncs.com/images/banner3.png")
				.redirectUrl("https://www-file.huawei.com/-/media/corp2020/home/banner/1/vmall_sv_pc.png")
				.carouselRank(2)
				.build();
		carouselService.saveCarousel(carousel);
		/*主要用于批量测试
		carousel = Carousel.builder().carouselId(7)
				.carouselUrl("https://newbee-mall.oss-cn-beijing.aliyuncs.com/images/banner3.png")
				.redirectUrl("https://www-file.huawei.com/-/media/corp2020/home/banner/1/vmall_sv_pc.png")
				.carouselRank(2)
				.isDeleted(0)
				.createTime(new Date())
				.createUser(0)
				.updateTime(new Date())
				.updateUser(0).build();
		carouselService.saveCarousel(carousel);
		carousel = Carousel.builder().carouselId(8)
				.carouselUrl("https://newbee-mall.oss-cn-beijing.aliyuncs.com/images/banner3.png")
				.redirectUrl("https://www-file.huawei.com/-/media/corp2020/home/banner/1/vmall_sv_pc.png")
				.carouselRank(2)
				.isDeleted(0)
				.createTime(new Date())
				.createUser(0)
				.updateTime(new Date())
				.updateUser(0).build();
		carouselService.saveCarousel(carousel);*/

	}

	@Test
	void updateCarousel() {
		Carousel carousel =Carousel.builder()
				.carouselId(4)
				.carouselUrl("https://newbee-mall.oss-cn-beijing.aliyuncs.com/images/banner3.png")
				.redirectUrl("ttps://www-file.huawei.com/-/media/corp2020/home/banner/1/sustainability-cn2.jpg")
				.carouselRank(2)
				.build();
		carouselService.updateCarousel(carousel);


	}

	@Test
	void getCarouselById() {
		carouselService.getCarouselById(2);
	}

	@Test
	void deletedBatch() {
		Integer[] ids = {6,7,8};
		carouselService.deletedBatch(ids);
	}

}