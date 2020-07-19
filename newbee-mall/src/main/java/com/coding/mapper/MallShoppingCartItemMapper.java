package com.coding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coding.entity.MallShoppingCartItem;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-16 19:49
 **/
public interface MallShoppingCartItemMapper extends BaseMapper<MallShoppingCartItem> {

	@Select("select * from tb_newbee_mall_shopping_cart_item where user_id = #{userId,jdbcType=BIGINT} " +
			"and goods_id=#{goodsId,jdbcType=BIGINT} and is_deleted = 0 limit 1")
	MallShoppingCartItem selectByUserIdAndGoodsId(Long userId, Long goodsId);

	@Select("select count(*) from tb_newbee_mall_shopping_cart_item" +
			" where user_id = #{userId,jdbcType=BIGINT} and is_deleted = 0")
	int selectCountByUserId(Long userId);

	@Select("select * from tb_newbee_mall_shopping_cart_item where cart_item_id = " +
			"#{cartItemId,jdbcType=BIGINT} and is_deleted = 0")
	MallShoppingCartItem selectByPrimaryKey(Long cartItemId);

	@Select("select * from tb_newbee_mall_shopping_cart_item" +
			" where user_id = #{userId,jdbcType=BIGINT} and is_deleted = 0 limit #{shoppingCartItemTotalNumber}")
	List<MallShoppingCartItem> selectByUserId(Long userId, int shoppingCartItemTotalNumber);
}
