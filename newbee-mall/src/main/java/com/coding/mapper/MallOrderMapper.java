package com.coding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coding.entity.MallOrder;
import com.coding.util.PageUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-18 01:49
 **/
public interface MallOrderMapper extends BaseMapper<MallOrder> {

	//注解未考虑非空检验，无法使用
	/*@Select("select count(*) from tb_newbee_mall_order where order_no = #{orderNo} and user_id = #{userId} and pay_type = #{payType}" +
			" and order_status = #{orderStatus} and is_deleted = #{isDeleted}  and create_time > #{startTime} and create_time < #{endTime}")*/
	int getTotalNewBeeMallOrders(PageUtil pageUtil);

	List<MallOrder> findNewBeeMallOrderList(PageUtil pageUtil);

	int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

	@Select("select * from tb_newbee_mall_order where order_no = #{orderNo,jdbcType=VARCHAR} and is_deleted=0 limit 1")
	MallOrder selectByOrderNo(String orderNo);

	int updateByPrimaryKeySelective(MallOrder mallOrder);
}
