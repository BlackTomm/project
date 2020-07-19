package com.coding.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coding.common.ServiceResultEnum;
import com.coding.controller.vo.IndexConfigGoodsVO;
import com.coding.entity.Goods;
import com.coding.entity.IndexConfig;
import com.coding.mapper.GoodsMapper;
import com.coding.mapper.IndexConfigMapper;
import com.coding.service.IndexConfigService;
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
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-15 17:14
 **/
@Service
public class IndexConfigServiceImpl implements IndexConfigService {

	@Autowired
	private IndexConfigMapper indexConfigMapper;

	@Autowired
	private GoodsMapper goodsMapper;

	@Override
	public PageResult getConfigsPage(PageUtil pageUtil) {
		List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigList(pageUtil);
		int total = indexConfigMapper.getTotalIndexConfigs(pageUtil);
		PageResult pageResult = new PageResult(indexConfigs, total, pageUtil.getLimit(), pageUtil.getLimit());
		return pageResult;
	}

	@Override
	public String saveIndexConfig(IndexConfig indexConfig) {
		//todo 判断是否存在该商品,该比较哪些项说明是否存在
		if (indexConfigMapper.insert(indexConfig) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public String updateIndexConfig(IndexConfig indexConfig) {
		//todo 判断是否存在该商品
		IndexConfig temp = indexConfigMapper.selectById(indexConfig.getConfigId());
		if (temp == null) {
			return ServiceResultEnum.DATA_NOT_EXIST.getResult();
		}
		if (indexConfigMapper.updateById(indexConfig) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public IndexConfig getIndexConfigById(Long id) {
		return indexConfigMapper.selectById(id);
	}

	@Override
	public boolean deleteBatch(Long[] ids) {
		List<Long> idList = new ArrayList<>(Arrays.asList(ids));
		int updateNumbers = idList.stream().mapToInt(id -> indexConfigMapper.update(
				new IndexConfig().setIsDeleted(1).setUpdateTime(new Date()),
				new QueryWrapper<IndexConfig>()
						.lambda().eq(IndexConfig::getConfigId, id))).sum();
		return updateNumbers == ids.length;
	}

	/**
	 * @Description: 根据 configType 参数读取固定数量的首页配置数据，之后获取配置项中关联的商品记录属性，然后对字符串
	 * 进行处理并封装到 VO 对象里，之后设置到 request 域中，这里主要是将需要的数据读取出来并封装成一个对象返回给视图层。
	 * <p>
	 * VO 对象就是视图层使用的对象，一般与 entity 对象有一点点区别，entity 对象中的字段与数据库表字段逐一对应，VO 对
	 * 象里的字段则是视图层需要哪些字段就设置哪些字段，也可以不新增 VO 对象直接返回 entity 对象，这个取决于各个开发者
	 * 的写法习惯
	 * @Param: [configType, number]
	 * @return: java.util.List<com.coding.controller.vo.IndexConfigGoodsVO>
	 */
	@Override
	public List<IndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
		List<IndexConfigGoodsVO> indexConfigGoodsVOS = new ArrayList<>(number);
		List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
		if (!CollectionUtils.isEmpty(indexConfigs)) {
			//取出goodsId
			List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
			List<Goods> goods = goodsMapper.selectByPrimaryKeys(goodsIds);
			indexConfigGoodsVOS = BeanUtil.copyList(goods, IndexConfigGoodsVO.class);

			for (IndexConfigGoodsVO indexConfigGoodsVO : indexConfigGoodsVOS) {
				String goodsName = indexConfigGoodsVO.getGoodsName();
				String goodsIntro = indexConfigGoodsVO.getGoodsIntro();
				// 字符串过长导致文字超出的问题
				if (goodsName.length() > 30) {
					goodsName = goodsName.substring(0, 30) + "...";
					indexConfigGoodsVO.setGoodsName(goodsName);
				}
				if (goodsIntro.length() > 22) {
					goodsIntro = goodsIntro.substring(0, 22) + "...";
					indexConfigGoodsVO.setGoodsIntro(goodsIntro);
				}
			}
		}
		return indexConfigGoodsVOS;
	}
}
