package com.coding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coding.entity.Goods;
import com.coding.entity.StockNumDTO;
import com.coding.util.PageUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-13 15:14
 **/
public interface GoodsMapper extends BaseMapper<Goods> {

	@Select("select * from tb_newbee_mall_goods_info order by goods_id desc limit #{start}, #{limit}")
	List<Goods> findNewBeeMallGoodsList(PageUtil pageUtil);

	@Select("select count(*) from tb_newbee_mall_goods_info")
	int getTotalNewBeeMallGoods(PageUtil pageUtil);

	List<Goods> findNewBeeMallGoodsListBySearch(PageUtil pageUtil);

	@Select("select count(*) from tb_newbee_mall_goods_info where goods_name like CONCAT('%',#{keyword},'%') or " +
			"goods_intro like CONCAT('%',#{keyword},'%') and goods_category_id = #{goodsCategoryId} " +
			"and goods_sell_status = #{goodsSellStatus}")
	int getTotalNewBeeMallGoodsBySearch(PageUtil pageUtil);

	List<Goods> selectByPrimaryKeys(List<Long> mallGoodsIds);

	int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);
}
