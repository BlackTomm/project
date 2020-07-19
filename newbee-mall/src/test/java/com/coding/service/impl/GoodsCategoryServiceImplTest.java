package com.coding.service.impl;

import com.coding.service.GoodsCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
class GoodsCategoryServiceImplTest {

	@Autowired
	private GoodsCategoryService goodsCategoryService;

	@Test
	void getCategoriesForIndex(){
		log.info("index category is {}", goodsCategoryService.getCategoriesForIndex());
	}
}