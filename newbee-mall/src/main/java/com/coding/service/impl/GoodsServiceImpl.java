package com.coding.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.coding.common.ServiceResultEnum;
import com.coding.controller.vo.MallSearchGoodsVO;
import com.coding.entity.Goods;
import com.coding.mapper.GoodsMapper;
import com.coding.service.GoodsService;
import com.coding.util.BeanUtil;
import com.coding.util.PageResult;
import com.coding.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-13 15:15
 **/
@Service
public class GoodsServiceImpl implements GoodsService {
	@Autowired
	private GoodsMapper goodsMapper;

	@Override
	public Goods getNewBeeMallGoodsById(Long id) {
		return goodsMapper.selectById(id);
	}

	@Override
	public String saveNewBeeMallGoods(Goods goods) {
		if (goodsMapper.insert(goods) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public PageResult getNewBeeMallGoodsPage(PageUtil pageUtil) {
		List<Goods> goodsList = goodsMapper.findNewBeeMallGoodsList(pageUtil);
		int total = goodsMapper.getTotalNewBeeMallGoods(pageUtil);
		PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;
	}

	@Override
	public String updateNewBeeMallGoods(Goods goods) {
		//非空判断
		Goods temp = goodsMapper.selectById(goods.getGoodsId());
		if (temp == null) {
			return ServiceResultEnum.DB_ERROR.getResult();
		}
		goods.setUpdateTime(new Date());
		if (goodsMapper.updateById(goods) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public boolean batchUpdateSellStatus(Long[] ids, byte sellStatus) {
		List<Long> idList = new ArrayList<Long>(Arrays.asList(ids));
		int updateNumbers = idList.stream().mapToInt(id -> goodsMapper.update(
				new Goods().setGoodsSellStatus(sellStatus).setUpdateTime(new Date()),
				new QueryWrapper<Goods>()
						.lambda().eq(Goods::getGoodsId, id))).sum();
		return updateNumbers == ids.length;
	}

	@Override
	public PageResult searchNewBeeMallGoods(PageUtil pageUtil) {
		List<Goods> goodsList = goodsMapper.findNewBeeMallGoodsListBySearch(pageUtil);
		int total = goodsMapper.getTotalNewBeeMallGoodsBySearch(pageUtil);
		List<MallSearchGoodsVO> mallSearchGoodsVOS = new ArrayList<>();
		if(!CollectionUtils.isEmpty(goodsList)){
			mallSearchGoodsVOS = BeanUtil.copyList(goodsList, MallSearchGoodsVO.class);

			for(MallSearchGoodsVO mallSearchGoodsVO: mallSearchGoodsVOS){
				String goodsName = mallSearchGoodsVO.getGoodsName();
				String goodsIntro = mallSearchGoodsVO.getGoodsIntro();
				// 字符串过长导致文字超出的问题
				if (goodsName.length() > 28) {
					goodsName = goodsName.substring(0, 28) + "...";
					mallSearchGoodsVO.setGoodsName(goodsName);
				}
				if (goodsIntro.length() > 30) {
					goodsIntro = goodsIntro.substring(0, 30) + "...";
					mallSearchGoodsVO.setGoodsIntro(goodsIntro);
				}
			}
		}
		PageResult pageResult = new PageResult(mallSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;
	}
}
