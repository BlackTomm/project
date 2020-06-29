package com.seckill.dto;

import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillStateEnum;
import lombok.Data;
import lombok.ToString;

/**
 * @description: 秒杀后的结果
 * @author: Black Tom
 * @create: 2020-06-23 18:47
 **/
@Data
@ToString
public class SeckillExecution {
	private long seckillId;

	//秒杀结果状态
	private int state;

	//状态表示
	private String stateInfo;

	//秒杀成功的订单对象
	private SuccessKilled successKilled;

	public SeckillExecution(long seckillId, SeckillStateEnum seckillStateEnum, SuccessKilled successKilled) {
		this.seckillId = seckillId;
		this.state = seckillStateEnum.getState();
		this.stateInfo = seckillStateEnum.getStateInfo();
		this.successKilled = successKilled;
	}

	public SeckillExecution(long seckillId, SeckillStateEnum seckillStateEnum) {
		this.seckillId = seckillId;
		this.state = seckillStateEnum.getState();
		this.stateInfo = seckillStateEnum.getStateInfo();
	}
}
