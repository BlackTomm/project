package com.coding.service;

import com.coding.entity.Goods;
import com.coding.util.PageResult;
import com.coding.util.PageUtil;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-13 15:15
 **/
public interface GoodsService {

	/**
	 * 获取商品详情
	 *
	 * @param id
	 * @return
	 */
	Goods getNewBeeMallGoodsById(Long id);

	/**
	* @Description: 添加商品
	* @Param: [goods]
	* @return: java.lang.String
	*/
	String saveNewBeeMallGoods(Goods goods);

	/**
	 * 修改商品信息
	 * @param goods
	 * @return
	 */
	String updateNewBeeMallGoods(Goods goods);

	/**
	* @Description: 获取商品列表
	* @Param: []
	* @return: com.coding.util.Result
	 * @param pageUtil
	 */
	PageResult getNewBeeMallGoodsPage(PageUtil pageUtil);

	/**
	* @Description: 批量修改商品售卖状态：上架/下架
	* @Param: [ids, sellStatus]
	* @return: boolean
	*/
	boolean batchUpdateSellStatus(Long[] ids, byte sellStatus);

	/**
	 * 商品搜索
	 *
	 * @param pageUtil
	 * @return
	 */
	PageResult searchNewBeeMallGoods(PageUtil pageUtil);
}
