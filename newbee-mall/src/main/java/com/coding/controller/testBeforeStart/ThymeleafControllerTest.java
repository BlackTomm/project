package com.coding.controller.testBeforeStart;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: thymeleaf测试
 * @author: Black Tom
 * @create: 2020-07-06 19:05
 **/
@Controller
public class ThymeleafControllerTest {
	@GetMapping("/test")
	public String test(ModelMap map) {
		map.put("title", "Thymeleaf 语法测试");
		map.put("testString", "Spring Boot 商城");
		map.put("bool", true);
		map.put("testArray", new Integer[]{2018, 2019, 2020, 2021});
		map.put("testList", Arrays.asList("Spring", "Spring Boot", "Thymeleaf", "MyBatis", "Java"));
		Map testMap = new HashMap();
		testMap.put("platform", "juejin");
		testMap.put("title", "Spring Boot 大型商城项目实战");
		testMap.put("author", "十三");
		map.put("testMap", testMap);
		map.put("testDate", new Date());
		return "test";
	}
}
