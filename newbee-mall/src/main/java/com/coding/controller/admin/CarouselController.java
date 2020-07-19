package com.coding.controller.admin;

import com.coding.common.ServiceResultEnum;
import com.coding.entity.Carousel;
import com.coding.service.CarouselService;
import com.coding.util.PageUtil;
import com.coding.util.Result;
import com.coding.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * @description: 轮播图控制
 * @author: Black Tom
 * @create: 2020-07-10 16:04
 **/
@Controller
@RequestMapping("/admin")
public class CarouselController {

	@Resource
	private CarouselService carouselService;

	@GetMapping("/carousels")
	public String carouselPage(HttpServletRequest request) {
		request.setAttribute("path", "newbee_mall_carousel");
		return "admin/carousel";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/carousels/list", method = RequestMethod.GET)
	@ResponseBody
	public Result list(@RequestParam Map<String, Object> params) {
		if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
			return ResultGenerator.genFailResult("参数异常");
		}
		PageUtil pageUtil = new PageUtil(params);
		return ResultGenerator.genSuccessResult(carouselService.getCarouselPage(pageUtil));
	}

	@RequestMapping(value = "/carousels/save", method = RequestMethod.POST)
	@ResponseBody
	public Result save(@RequestBody Carousel carousel) {
		if (StringUtils.isEmpty(carousel.getCarouselUrl()) || Objects.isNull(carousel.getCarouselRank())) {
			return ResultGenerator.genFailResult("参数异常");
		}
		String result = carouselService.saveCarousel(carousel);
		if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(result);
		}
	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/carousels/update", method = RequestMethod.POST)
	@ResponseBody
	public Result update(@RequestBody Carousel carousel) {
		if (Objects.isNull(carousel.getCarouselId())
				|| StringUtils.isEmpty(carousel.getCarouselUrl())
				|| Objects.isNull(carousel.getCarouselRank())) {
			return ResultGenerator.genFailResult("参数异常");
		}
		String result = carouselService.updateCarousel(carousel);
		if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(result);
		}
	}

	/**
	 * 将选择的记录回显到模态编辑框中以供修改
	 */
	@GetMapping("/carousels/info/{id}")
	@ResponseBody
	public Result info(@PathVariable("id") Integer id) {
		Carousel carousel = carouselService.getCarouselById(id);
		if (carousel == null) {
			return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
		}
		return ResultGenerator.genSuccessResult(carousel);
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/carousels/delete", method = RequestMethod.POST)
	@ResponseBody
	public Result delete(@RequestBody Integer[] ids) {
		if (ids.length < 1) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		if (carouselService.deletedBatch(ids)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult("删除失败");
		}
	}
}
