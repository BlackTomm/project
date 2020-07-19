package com.coding.controller.testBeforeStart;

import com.coding.common.Constants;
import com.coding.service.testBeforeStart.UserService;
import com.coding.util.PageUtil;
import com.coding.util.Result;
import com.coding.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-09 17:23
 **/
@RestController
@RequestMapping("/users")
public class UserControllerTest {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result list(@RequestParam Map<String, Object> params) {
		if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
			return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "参数异常！");
		}
		//查询列表数据
		PageUtil pageUtil = new PageUtil(params);
		return ResultGenerator.genSuccessResult(userService.getAdminUserPage(pageUtil));
	}
}
