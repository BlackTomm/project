package com.seckill.mapper;

import com.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-06-28 17:57
 **/
@Mapper
public interface SuccessKilledMapper {
	/**
	 * 插入购买订单明细
	 *
	 * @param seckillId 秒杀到的商品ID
	 * @param userPhone 秒杀的用户
	 * @return 返回该SQL更新的记录数，如果>=1则更新成功
	 */
	int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);

	/**
	 * 根据秒杀商品ID查询订单明细数据并得到对应秒杀商品的数据，因为我们再SuccessKilled中已经定义了一个Seckill的属性
	 *
	 * @param seckillId
	 * @param userPhone
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
