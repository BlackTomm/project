package com.seckill.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 使用枚举表示秒杀状态
 * @author: Black Tom
 * @create: 2020-06-23 21:27
 **/
@AllArgsConstructor
@Getter
public enum SeckillStateEnum {
	SUCCESS(1, "秒杀成功"),
	END(0, "秒杀结束"),
	REPEAT_KILL(-1, "已秒杀成功，不能多次参与"),
	INNER_ERROR(-2, "系统异常"),
	DATA_REWRITE(-3, "数据篡改");

	private int state;
	private String stateInfo;

	public static SeckillStateEnum stateOf(int index) {
		for (SeckillStateEnum stateEnum : values()) {
			if (stateEnum.getState() == index) {
				return stateEnum;
			}
		}
		return null;
	}
}
