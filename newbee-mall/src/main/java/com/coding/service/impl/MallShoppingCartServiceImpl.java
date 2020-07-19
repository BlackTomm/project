package com.coding.service.impl;

import com.coding.common.Constants;
import com.coding.common.ServiceResultEnum;
import com.coding.controller.vo.MallShoppingCartItemVO;
import com.coding.entity.Goods;
import com.coding.entity.MallShoppingCartItem;
import com.coding.mapper.GoodsMapper;
import com.coding.mapper.MallShoppingCartItemMapper;
import com.coding.service.MallShoppingCartService;
import com.coding.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-16 19:32
 **/
@Service
public class MallShoppingCartServiceImpl implements MallShoppingCartService {

	@Autowired
	private MallShoppingCartItemMapper mallShoppingCartItemMapper;

	@Autowired
	private GoodsMapper goodsMapper;

	/**
	 * @Description: 首先对参数进行校验，校验步骤如下：
	 * <p>
	 * 1.根据用户信息和商品信息去查询购物项表中是否已存在相同的记录，如果存在则进行修改操作，不存在则进行后续操作
	 * 2.判断商品数据是否正确
	 * 3.判断用户的购物车中的商品数量是否已超出最大限制
	 * 校验通过后再进行新增操作，将该记录保存到数据库中，以上操作中都需要调用 SQL 语句来完成。
	 * @Param: [mallShoppingCartItem]
	 * @return: java.lang.String
	 */
	@Override
	public String saveMallCartItem(MallShoppingCartItem mallShoppingCartItem) {
		MallShoppingCartItem temp = mallShoppingCartItemMapper.selectByUserIdAndGoodsId(mallShoppingCartItem.getUserId(),
				mallShoppingCartItem.getGoodsId());
		//1.查询购物项表中是否已存在相同的记录，如果存在则进行修改操作，不存在则进行后续操作
		if (temp != null) {
			temp.setGoodsCount(temp.getGoodsCount() + mallShoppingCartItem.getGoodsCount());
			return updateMallCartItem(temp);
		}
		Goods goods = goodsMapper.selectById(mallShoppingCartItem.getGoodsId());
		if (goods == null) {
			return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
		}
		int totalItem = mallShoppingCartItemMapper.selectCountByUserId(mallShoppingCartItem.getUserId());
		//超出单个商品的最大数量
		if (totalItem > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
			return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
		}
		//超出最大数量
		if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
			return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
		}
		//保存
		if (mallShoppingCartItemMapper.insert(mallShoppingCartItem) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public String updateMallCartItem(MallShoppingCartItem temp) {
		MallShoppingCartItem mallShoppingCartItemUpdate = mallShoppingCartItemMapper.selectByPrimaryKey(temp.getCartItemId());
		if (mallShoppingCartItemUpdate == null) {
			return ServiceResultEnum.DATA_NOT_EXIST.getResult();
		}
		//超出单个商品的最大数量
		if (temp.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
			return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
		}
		//todo userId不同不能修改
		mallShoppingCartItemUpdate.setGoodsCount(temp.getGoodsCount());
		mallShoppingCartItemUpdate.setUpdateTime(new Date());
		if (mallShoppingCartItemMapper.updateById(mallShoppingCartItemUpdate) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	/**
	 * @Description: 定义了 getMyShoppingCartItems() 方法并传入 userId 即用户 id 作为参数，之后通过 SQL
	 * 查询出当前 userId 下的购物项列表数据，因为购物车页面需要展示商品信息，所以通过购物项表中的 goods_id 来
	 * 获取到每个购物项对应的商品信息，再之后是填充数据，将相关字段封装到 NewBeeMallShoppingCartItemVO 对象
	 * 中，某些字段太长会导致页面上的展示效果不好，所以对这些字段进行了简单的字符串处理，最终将封装好的 List 对
	 * 象返回
	 * @Param: [userId]
	 * @return: java.util.List<com.coding.controller.vo.MallShoppingCartItemVO>
	 */
	@Override
	public List<MallShoppingCartItemVO> getMyShoppingCartItems(Long userId) {
		List<MallShoppingCartItemVO> mallShoppingCartItemVOS = new ArrayList<>();
		List<MallShoppingCartItem> mallShoppingCartItems = mallShoppingCartItemMapper.selectByUserId(
				userId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
		if (!CollectionUtils.isEmpty(mallShoppingCartItems)) {
			//查询商品信息并做数据转换
			List<Long> mallGoodsIds = mallShoppingCartItems.stream().map(MallShoppingCartItem::getGoodsId)
					.collect(Collectors.toList());
			List<Goods> mallGoods = goodsMapper.selectByPrimaryKeys(mallGoodsIds);
			Map<Long, Goods> mallGoodsMap = new HashMap<>();
			if (!CollectionUtils.isEmpty(mallGoods)) {
				//此处用法可参考 http://www.spring4all.com/article/15079
				mallGoodsMap = mallGoods.stream().collect(Collectors.toMap(Goods::getGoodsId,
						Function.identity(), (entity1, entity2) -> entity1));
			}
			for (MallShoppingCartItem mallShoppingCartItem : mallShoppingCartItems) {
				MallShoppingCartItemVO mallShoppingCartItemVO = new MallShoppingCartItemVO();
				BeanUtil.copyProperties(mallShoppingCartItem, mallShoppingCartItemVO);
				if (mallGoodsMap.containsKey(mallShoppingCartItem.getGoodsId())) {
					Goods mallGoodsTemp = mallGoodsMap.get(mallShoppingCartItem.getGoodsId());
					mallShoppingCartItemVO.setGoodsCoverImg(mallGoodsTemp.getGoodsCoverImg());
					String goodsName = mallGoodsTemp.getGoodsName();
					// 字符串过长导致文字超出的问题
					if (goodsName.length() > 28) {
						goodsName = goodsName.substring(0, 28) + "...";
					}
					mallShoppingCartItemVO.setGoodsName(goodsName);
					mallShoppingCartItemVO.setSellingPrice(mallGoodsTemp.getSellingPrice());
					mallShoppingCartItemVOS.add(mallShoppingCartItemVO);
				}
			}
		}
		return mallShoppingCartItemVOS;
	}

	@Override
	public Boolean deleteById(Long mallShoppingCartItemId) {
		return mallShoppingCartItemMapper.deleteById(mallShoppingCartItemId)>0;
	}
}
