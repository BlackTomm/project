package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatSeckillException;
import com.seckill.exception.SeckillClosedException;
import com.seckill.exception.SeckillException;

import java.util.List;

/**
 * @description:接口定义原则：方法定义粒度，参数（简练），返回类型（友好） 站在调用方角度，而不是具体方法实现
 * @author: Black Tom
 * @create: 2020-06-23 18:18
 **/
public interface SeckillService {
	/**
	 * @Description: 查询秒杀商品列表
	 * @return: java.util.List<seckill.entity.Seckill>
	 */
	List<Seckill> getSeckillList();

	/**
	 * @Description: 根据商品id查询秒杀信息
	 * @return: seckill.entity.Seckill
	 */
	Seckill getById(long seckillId);

	Exposer exportSeckillUrl(long seckillId);

	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatSeckillException, SeckillClosedException;

	;

	/**
	 * 秒杀（存储过程）
	 *
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 */
	/*SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatSeckillException, SeckillClosedException;*/
}
