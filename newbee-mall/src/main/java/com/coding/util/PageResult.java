package com.coding.util;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 分页参数封装
 * @author: Black Tom
 * @create: 2020-07-09 16:19
 **/
@Data
public class PageResult implements Serializable {

	//总记录数
	private int totalCount;
	//每页记录数
	private int pageSize;
	//总页数
	private int totalPage;
	//当前页数
	private int currentPage;
	//列表数据
	private List<?> list;

	public PageResult(List<?> list, int totalCount, int pageSize, int currentPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.totalPage = (int) Math.ceil((double) totalCount/pageSize);
	}
}
