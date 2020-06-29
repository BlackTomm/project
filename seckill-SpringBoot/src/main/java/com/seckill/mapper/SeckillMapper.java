package com.seckill.mapper;

import com.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-06-28 17:49
 **/
@Mapper
public interface SeckillMapper {
	/**
	 * 查询所有秒杀商品的记录信息
	 */
	List<Seckill> queryAll();

	/**
	 * 根据主键查询当前秒杀商品的数据
	 **/
	Seckill queryById(long seckillId);

	/**
	 * 减库存。
	 * 对于Mapper映射接口方法中存在多个参数的要加@Param()注解标识字段名称，不然Mybatis不能识别出来哪个字段相互对应
	 *
	 * @param seckillId 秒杀商品ID
	 * @param killTime  秒杀时间
	 * @return 返回此SQL更新的记录数，如果>=1表示更新成功
	 */
	int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

}
