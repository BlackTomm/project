package com.coding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coding.entity.MallOrderItem;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-18 01:55
 **/
public interface MallOrderItemMapper extends BaseMapper<MallOrderItem> {

	/**
	* @Description: 根据订单id获取订单项列表
	* @Param: [orderId]
	* @return: java.util.List<com.coding.entity.MallOrderItem>
	*/
	@Select("select * from tb_newbee_mall_order_item where order_id = #{orderItemId,jdbcType=BIGINT}")
	List<MallOrderItem> selectByOrderId(Long orderId);
}
