package com.coding.service;

import com.coding.controller.vo.MallShoppingCartItemVO;
import com.coding.entity.MallShoppingCartItem;

import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-16 19:31
 **/
public interface MallShoppingCartService {

	String saveMallCartItem(MallShoppingCartItem shoppingCartItem);

	String updateMallCartItem(MallShoppingCartItem temp);

	/**
	 * 获取我的购物车中的列表数据
	 *
	 * @param userId
	 * @return
	 */
	List<MallShoppingCartItemVO> getMyShoppingCartItems(Long userId);

	Boolean deleteById(Long mallShoppingCartItemId);
}
