package com.coding.util;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description: 根据当前页及每页数据量判断下次数据库查询参数
 * @author: Black Tom
 * @create: 2020-07-09 16:40
 **/
@Data
@Builder
@AllArgsConstructor //注意此处需要添加包含全部参数的构造函数
public class PageUtil extends LinkedHashMap<String, Object> {
	//当前页码
//	@NonNull
	private int page;
	//每页条数
//	@NonNull
	private int limit;

	public PageUtil(Map<String, Object> params) {
		this.putAll(params);

		//分页参数
		this.page = Integer.parseInt(params.get("page").toString());
		this.limit = Integer.parseInt(params.get("limit").toString());
		this.put("start",(page - 1)*limit);
		this.put("page", page);
		this.put("limit",limit);
	}
}
