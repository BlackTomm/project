package com.coding.mapper;

import com.coding.common.Constants;
import com.coding.util.PageUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class MallOrderMapperTest {
	@Autowired
	private MallOrderMapper mallOrderMapper;

	@Test
	void getTotalNewBeeMallOrders(){
		Map<String, Object> params =new LinkedHashMap<>();
		params.put("userId", 2);
		if (StringUtils.isEmpty(params.get("page"))) {
			params.put("page", 1);
		}
		params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
		PageUtil pageUtil = new PageUtil(params);
		System.out.println(mallOrderMapper.getTotalNewBeeMallOrders(pageUtil));
	}


}