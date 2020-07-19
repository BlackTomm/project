package com.coding.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 轮播页首图
 * @author: Black Tom
 * @create: 2020-07-10 17:12
 **/
@Data
public class IndexCarouselVO implements Serializable {

	private String carouselUrl;

	private String redirectUrl;
}
