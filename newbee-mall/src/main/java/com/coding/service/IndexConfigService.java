package com.coding.service;

import com.coding.controller.vo.IndexConfigGoodsVO;
import com.coding.entity.IndexConfig;
import com.coding.util.PageResult;
import com.coding.util.PageUtil;

import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-15 17:13
 **/
public interface IndexConfigService {
	/**
	 * 后台分页
	 *
	 * @param pageUtil
	 * @return
	 */
	PageResult getConfigsPage(PageUtil pageUtil);

	/**
	 * 返回固定数量的首页配置商品对象(首页调用)
	 *
	 * @param number
	 * @return
	 */
	List<IndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);

	String saveIndexConfig(IndexConfig indexConfig);

	String updateIndexConfig(IndexConfig indexConfig);

	IndexConfig getIndexConfigById(Long id);

	boolean deleteBatch(Long[] ids);
}
