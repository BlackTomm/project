package com.coding.service.impl;

import com.coding.service.GoodsService;
import com.coding.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
class GoodsServiceImplTest {

	@Autowired
	private GoodsService goodsService;

	@Test
	void searchNewBeeMallGoods() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("page", "1");
		map.put("limit", "3");
		PageUtil pageUtil = new PageUtil(map);

		log.info("searchNewBeeMallGoods: {}", goodsService.searchNewBeeMallGoods(pageUtil));

	}
}