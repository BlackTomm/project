package com.coding.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coding.common.*;
import com.coding.controller.vo.*;
import com.coding.entity.Goods;
import com.coding.entity.MallOrder;
import com.coding.entity.MallOrderItem;
import com.coding.entity.StockNumDTO;
import com.coding.mapper.GoodsMapper;
import com.coding.mapper.MallOrderItemMapper;
import com.coding.mapper.MallOrderMapper;
import com.coding.mapper.MallShoppingCartItemMapper;
import com.coding.service.MallOrderService;
import com.coding.util.BeanUtil;
import com.coding.util.NumberUtil;
import com.coding.util.PageResult;
import com.coding.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-18 00:29
 **/
@Slf4j
@Service
public class MallOrderServiceImpl implements MallOrderService {

	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private MallShoppingCartItemMapper mallShoppingCartItemMapper;
	@Autowired
	private MallOrderMapper mallOrderMapper;
	@Autowired
	private MallOrderItemMapper mallOrderItemMapper;

	@Override
	@Transactional
	public String saveOrder(MallUserVO user, List<MallShoppingCartItemVO> myShoppingCartItems) {
		//获取该用户购物车中所有项（商品-->该商品下单数 总体对应一项）
		List<Long> itemIdList = myShoppingCartItems.stream().map(MallShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
		List<Long> goodsIds = myShoppingCartItems.stream().map(MallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
		List<Goods> goods = goodsMapper.selectByPrimaryKeys(goodsIds);
		//检查商品是否下架
		List<Goods> goodsListNotSelling = goods.stream()
				.filter(goodsTemp -> goodsTemp.getGoodsSellStatus() != Constants.SELL_STATUS_UP)
				.collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
			//goodsListNotSelling 对象非空则表示有下架商品
			MallException.fail(goodsListNotSelling.get(0).getGoodsName() + "已下架，无法生成订单");
		}
		Map<Long, Goods> goodsMap = goods.stream().collect(Collectors.toMap(Goods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
		//判断商品库存
		for (MallShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
			//查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
			if (!goodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
				MallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
			}
			//存在数量大于库存的情况，直接返回错误提醒
			int stock_test = goodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum();
			if (shoppingCartItemVO.getGoodsCount() > goodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
				MallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
			}
		}
		//生成订单并删除购物车项目
		if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(goods)) {
			if (mallShoppingCartItemMapper.deleteBatchIds(itemIdList) > 0) {
				List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
				int updateStockNumResult = goodsMapper.updateStockNum(stockNumDTOS);
				if (updateStockNumResult < 1) {
					MallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
				}

				//生成订单号
				String orderNo = NumberUtil.genOrderNo();
				int priceTotal = 0;
				//保存订单
				MallOrder mallOrder = new MallOrder();
				mallOrder.setOrderNo(orderNo);
				mallOrder.setUserId(user.getUserId());
				mallOrder.setUserAddress(user.getAddress());
				//总价
				for (MallShoppingCartItemVO mallShoppingCartItemVO : myShoppingCartItems) {
					priceTotal += mallShoppingCartItemVO.getGoodsCount() * mallShoppingCartItemVO.getSellingPrice();
				}
				if (priceTotal < 1) {
					MallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
				}
				mallOrder.setTotalPrice(priceTotal);
				//todo 订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串
				String extraInfo = "";
				mallOrder.setExtraInfo(extraInfo);
				//生成订单项并保存订单项纪录
				if (mallOrderMapper.insert(mallOrder) > 0) {
					//生成所有的订单项快照，并保存至数据库
					List<MallOrderItem> mallOrderItems = new ArrayList<>();
					for (MallShoppingCartItemVO mallShoppingCartItemVO : myShoppingCartItems) {
						MallOrderItem mallOrderItem = new MallOrderItem();
						//m将newBeeMallShoppingCartItemVO中的属性复制到newBeeMallOrderItem对象中
						BeanUtil.copyProperties(mallShoppingCartItemVO, mallOrderItem);
						//mallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
						mallOrderItem.setOrderId(mallOrder.getOrderId());
						mallOrderItems.add(mallOrderItem);
					}
					//保存至数据库
					if (mallOrderItems.stream().mapToInt(mallOrderItem -> mallOrderItemMapper.insert(mallOrderItem)).sum()
							== mallOrderItems.size()) {
						//所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
						return orderNo;
					}
					MallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
				}
				MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
			}
			MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
		}
		MallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
		return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
	}

	@Override
	public MallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
		MallOrder mallOrder = mallOrderMapper.selectOne(new QueryWrapper<MallOrder>().lambda()
				.eq(MallOrder::getOrderNo, orderNo));
		if (mallOrder != null) {
			//todo 验证是否是当前userId下的订单，否则报错
			//查找同一个订单中所有商品
			List<MallOrderItem> mallOrderItemList = mallOrderItemMapper.selectByOrderId(mallOrder.getOrderId());
			if (!CollectionUtils.isEmpty(mallOrderItemList)) {
				//进行类型转化
				List<MallOrderItemVO> mallOrderItemVOS = BeanUtil.copyList(mallOrderItemList, MallOrderItemVO.class);

				MallOrderDetailVO mallOrderDetailVO = new MallOrderDetailVO();
				BeanUtil.copyProperties(mallOrder, mallOrderDetailVO);
				//设置订单状态
				mallOrderDetailVO.setOrderStatusString(MallOrderStatusEnum.getMallOrderStatusEnumByStatus(
						mallOrderDetailVO.getOrderStatus()).getName());
				//设置支付方式
				mallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(mallOrderDetailVO.getPayType()).getName());
				mallOrderDetailVO.setMallOrderItemVOS(mallOrderItemVOS);
				return mallOrderDetailVO;
			}
		}
		return null;
	}

	@Override
	public PageResult getMyOrders(PageUtil pageUtil) {
		int total = mallOrderMapper.getTotalNewBeeMallOrders(pageUtil);
		//找到满足条件的订单（取决于pageUtil中参数）
		List<MallOrder> mallOrders = mallOrderMapper.findNewBeeMallOrderList(pageUtil);
		List<MallOrderListVO> orderListVOS = new ArrayList<>();
		if (total > 0) {
			//将实体转化为vo
			orderListVOS = BeanUtil.copyList(mallOrders, MallOrderListVO.class);
			for (MallOrderListVO mallOrderListVO : orderListVOS) {
				mallOrderListVO.setOrderStatusString(MallOrderStatusEnum.getMallOrderStatusEnumByStatus(
						mallOrderListVO.getOrderStatus()).getName());
			}

			List<Long> orderIds = mallOrders.stream().map(MallOrder::getOrderId).collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(orderIds)) {
				//TODO 待测
				List<MallOrderItem> orderItems = mallOrderItemMapper.selectBatchIds(orderIds);
				//根据订单id不同划分商品
				Map<Long, List<MallOrderItem>> itemByOrderIdMap = orderItems.stream().collect(
						groupingBy(MallOrderItem::getOrderId));
				for (MallOrderListVO mallOrderListVO : orderListVOS) {
					if (itemByOrderIdMap.containsKey(mallOrderListVO.getOrderId())) {
						List<MallOrderItem> orderItemListTemp = itemByOrderIdMap.get(mallOrderListVO.getOrderId());
						//实体转化为VO
						List<MallOrderItemVO> mallOrderItemVOS = BeanUtil.copyList(orderItemListTemp,
								MallOrderItemVO.class);
						mallOrderListVO.setMallOrderItemVOS(mallOrderItemVOS);
					}
				}
			}
		}
		PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
		log.debug("pageResult.list: {}", pageResult.getList());
		return pageResult;
	}

	@Override
	public String cancelOrder(String orderNo, Long userId) {
		MallOrder mallOrder = mallOrderMapper.selectById(orderNo);
		if (mallOrder != null) {
			//todo 验证是否是当前userId下的订单，否则报错
			//todo 订单状态判断
			if (mallOrderMapper.closeOrder(Collections.singletonList(mallOrder.getOrderId()),
					MallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
				return ServiceResultEnum.SUCCESS.getResult();
			} else {
				return ServiceResultEnum.DB_ERROR.getResult();
			}
		}
		return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
	}

	@Override
	public String finishOrder(String orderNo, Long userId) {
		MallOrder mallOrder = mallOrderMapper.selectOne(new QueryWrapper<MallOrder>().lambda()
				.eq(MallOrder::getOrderNo, orderNo));
		if (mallOrder != null) {
			//todo 订单状态判断
			mallOrder.setOrderStatus((byte) MallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
			mallOrder.setUpdateTime(new Date());
			if (mallOrderMapper.updateById(mallOrder) > 0) {
				return ServiceResultEnum.SUCCESS.getResult();
			} else {
				return ServiceResultEnum.DB_ERROR.getResult();
			}
		}
		return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
	}

	@Override
	public MallOrder getNewBeeMallOrderByOrderNo(String orderNo) {
		return mallOrderMapper.selectByOrderNo(orderNo);
	}

	@Override
	public String paySuccess(String orderNo, int payType) {
		MallOrder mallOrder = mallOrderMapper.selectByOrderNo(orderNo);
		if (mallOrder != null) {
			mallOrder.setOrderStatus((byte) MallOrderStatusEnum.ORDER_PAID.getOrderStatus());
			mallOrder.setPayType((byte) payType);
			mallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
			mallOrder.setPayTime(new Date());
			mallOrder.setUpdateTime(new Date());
			if (mallOrderMapper.updateByPrimaryKeySelective(mallOrder) > 0) {
				return ServiceResultEnum.SUCCESS.getResult();
			} else {
				return ServiceResultEnum.DB_ERROR.getResult();
			}
		}
		return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
	}

	@Override
	public PageResult getNewBeeMallOrdersPage(PageUtil pageUtil) {
		List<MallOrder> newBeeMallOrders = mallOrderMapper.findNewBeeMallOrderList(pageUtil);
		int total = mallOrderMapper.getTotalNewBeeMallOrders(pageUtil);
		PageResult pageResult = new PageResult(newBeeMallOrders, total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;

	}

	@Override
	public String updateOrderInfo(MallOrder mallOrder) {
		MallOrder temp = mallOrderMapper.selectById(mallOrder.getOrderId());
		//不为空且orderStatus>=0且状态为出库之前可以修改部分信息
		if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
			temp.setTotalPrice(mallOrder.getTotalPrice());
			temp.setUserAddress(mallOrder.getUserAddress());
			temp.setUpdateTime(new Date());
			if (mallOrderMapper.updateById(temp) > 0) {
				return ServiceResultEnum.SUCCESS.getResult();
			}
			return ServiceResultEnum.DB_ERROR.getResult();
		}
		return ServiceResultEnum.DATA_NOT_EXIST.getResult();
	}

	@Override
	public List<MallOrderItemVO> getOrderItems(Long orderId) {
		MallOrder mallOrder = mallOrderMapper.selectById(orderId);
		if (mallOrder != null) {
			List<MallOrderItem> orderItems = mallOrderItemMapper.selectByOrderId(orderId);
			if (!CollectionUtils.isEmpty(orderItems)) {
				List<MallOrderItemVO> mallOrderItemVOS = BeanUtil.copyList(orderItems, MallOrderItemVO.class);
				return mallOrderItemVOS;
			}
		}
		return null;
	}

	@Override
	@Transactional
	public String checkDone(Long[] ids) {
		//查询所有的订单 判断状态 修改状态和更新时间
		List<MallOrder> orders = mallOrderMapper.selectBatchIds(Arrays.asList(ids));
		String errorOrderNos = "";
		if (!CollectionUtils.isEmpty(orders)) {
			for (MallOrder mallOrder : orders) {
				if (mallOrder.getIsDeleted() == 1) {
					errorOrderNos += mallOrder.getOrderNo() + " ";
					continue;
				}
				//除去已支付
				if (mallOrder.getOrderStatus() != 1) {
					errorOrderNos += mallOrder.getOrderNo() + " ";
				}
			}
			if (StringUtils.isEmpty(errorOrderNos)) {
				//订单状态正常(已支付) 可以执行配货完成操作 修改订单状态和更新时间
				int checkDoneNum = Arrays.asList(ids).stream().mapToInt(id -> mallOrderMapper.update(
						new MallOrder().setOrderStatus((byte) 2).setUpdateTime(new Date()),
						new QueryWrapper<MallOrder>().lambda().eq(MallOrder::getOrderId, id))).sum();
				if (checkDoneNum == ids.length) {
					return ServiceResultEnum.SUCCESS.getResult();
				} else {
					return ServiceResultEnum.DB_ERROR.getResult();
				}
			} else {
				//订单此时不可执行出库操作
				if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
					return errorOrderNos + "未支付成功无法执行出库操作";
				} else {
					return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
				}
			}
		}
		//未查询到数据 返回错误提示
		return ServiceResultEnum.DATA_NOT_EXIST.getResult();
	}

	@Override
	@Transactional
	public String checkOut(Long[] ids) {
		//查询所有的订单 判断状态 修改状态和更新时间
		List<MallOrder> orders = mallOrderMapper.selectBatchIds(Arrays.asList(ids));
		String errorOrderNos = "";
		if (!CollectionUtils.isEmpty(orders)) {
			for (MallOrder mallOrder : orders) {
				if (mallOrder.getIsDeleted() == 1) {
					errorOrderNos += mallOrder.getOrderNo() + " ";
					continue;
				}
				//除去已支付与配货成功
				if (mallOrder.getOrderStatus() != 1 && mallOrder.getOrderStatus() != 2) {
					errorOrderNos += mallOrder.getOrderNo() + " ";
				}
			}
			if (StringUtils.isEmpty(errorOrderNos)) {
				//订单状态正常(已支付并完成配货) 可以执行出库操作 修改订单状态和更新时间
				int checkOutNum = Arrays.asList(ids).stream().mapToInt(id -> mallOrderMapper.update(
						new MallOrder().setOrderStatus((byte) 3).setUpdateTime(new Date()),
						new QueryWrapper<MallOrder>().lambda().eq(MallOrder::getOrderId, id))).sum();
				if (checkOutNum == ids.length) {
					return ServiceResultEnum.SUCCESS.getResult();
				} else {
					return ServiceResultEnum.DB_ERROR.getResult();
				}
			} else {
				//订单此时不可执行出库操作
				if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
					return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
				} else {
					return "你选择了太多状态不是支付成功或是配货完成的订单，无法执行出库完成操作";
				}
			}
		}
		//未查询到数据 返回错误提示
		return ServiceResultEnum.DATA_NOT_EXIST.getResult();
	}

	@Override
	@Transactional
	public String closeOrder(Long[] ids) {
		//查询所有的订单 判断状态 修改状态和更新时间
		List<MallOrder> orders = mallOrderMapper.selectBatchIds(Arrays.asList(ids));
		String errorOrderNos = "";
		if (!CollectionUtils.isEmpty(orders)) {
			for (MallOrder mallOrder : orders) {
				if (mallOrder.getIsDeleted() == 1) {
					errorOrderNos += mallOrder.getOrderNo() + " ";
					continue;
				}
				//除去已支付与配货成功
				if (mallOrder.getOrderStatus() == 4 && mallOrder.getOrderStatus() < 0) {
					errorOrderNos += mallOrder.getOrderNo() + " ";
				}
			}
			if (StringUtils.isEmpty(errorOrderNos)) {
				//订单状态正常 可以执行关闭操作 修改订单状态和更新时间
				int checkOutNum = Arrays.asList(ids).stream().mapToInt(id -> mallOrderMapper.update(
						new MallOrder().setOrderStatus((byte)MallOrderStatusEnum.ORDER_CLOSED_BY_MERCHANT.getOrderStatus())
								.setUpdateTime(new Date()),
						new QueryWrapper<MallOrder>().lambda().eq(MallOrder::getOrderId, id))).sum();
				if (checkOutNum == ids.length) {
					return ServiceResultEnum.SUCCESS.getResult();
				} else {
					return ServiceResultEnum.DB_ERROR.getResult();
				}
			} else {
				//订单此时不可执行关闭操作
				if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
					return errorOrderNos + "订单不能执行关闭操作";
				} else {
					return "你选择的订单不能执行关闭操作";
				}
			}
		}
		//未查询到数据 返回错误提示
		return ServiceResultEnum.DATA_NOT_EXIST.getResult();
	}
}
