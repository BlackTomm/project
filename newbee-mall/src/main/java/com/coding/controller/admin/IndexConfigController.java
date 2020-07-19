package com.coding.controller.admin;

import com.coding.common.IndexConfigTypeEnum;
import com.coding.common.ServiceResultEnum;
import com.coding.entity.IndexConfig;
import com.coding.service.IndexConfigService;
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
 * @description:
 * @author: Black Tom
 * @create: 2020-07-15 17:55
 **/
@Controller
@RequestMapping("/admin")
public class IndexConfigController {
	@Resource
	private IndexConfigService indexConfigService;

	@GetMapping("/indexConfigs")
	public String indexConfigsPage(HttpServletRequest request, @RequestParam("configType") int configType) {
		//根据configType显示不同的页面
		IndexConfigTypeEnum indexConfigTypeEnum = IndexConfigTypeEnum.getIndexConfigTypeEnumByType(configType);
		if(indexConfigTypeEnum.equals(IndexConfigTypeEnum.DEFAULT)){
			return "error/error_5xx";
		}
		request.setAttribute("path", indexConfigTypeEnum.getName());
		request.setAttribute("configType", configType);
		return "admin/index_config";
	}

	@RequestMapping(value = "/indexConfigs/list", method = RequestMethod.GET)
	@ResponseBody
	public Result list(@RequestParam Map<String, Object> params) {
		if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		PageUtil pageUtil = new PageUtil(params);
		return ResultGenerator.genSuccessResult(indexConfigService.getConfigsPage(pageUtil));
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/indexConfigs/save", method = RequestMethod.POST)
	@ResponseBody
	public Result save(@RequestBody IndexConfig indexConfig) {
		if (Objects.isNull(indexConfig.getConfigType())
				|| StringUtils.isEmpty(indexConfig.getConfigName())
				|| Objects.isNull(indexConfig.getConfigRank())) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		String result = indexConfigService.saveIndexConfig(indexConfig);
		if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(result);
		}
	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/indexConfigs/update", method = RequestMethod.POST)
	@ResponseBody
	public Result update(@RequestBody IndexConfig indexConfig) {
		if (Objects.isNull(indexConfig.getConfigType())
				|| Objects.isNull(indexConfig.getConfigId())
				|| StringUtils.isEmpty(indexConfig.getConfigName())
				|| Objects.isNull(indexConfig.getConfigRank())) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		String result = indexConfigService.updateIndexConfig(indexConfig);
		if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(result);
		}
	}

	/**
	 * 详情
	 */
	@GetMapping("/indexConfigs/info/{id}")
	@ResponseBody
	public Result info(@PathVariable("id") Long id) {
		IndexConfig config =indexConfigService.getIndexConfigById(id);
		if (config == null) {
			return ResultGenerator.genFailResult("未查询到数据");
		}
		return ResultGenerator.genSuccessResult(config);
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/indexConfigs/delete", method = RequestMethod.POST)
	@ResponseBody
	public Result delete(@RequestBody Long[] ids) {
		if(ids.length<1){
			return ResultGenerator.genFailResult("参数异常");
		}
		if(indexConfigService.deleteBatch(ids)){
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult("删除失败");
		}
	}
}

