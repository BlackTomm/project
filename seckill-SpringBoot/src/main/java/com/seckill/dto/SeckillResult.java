package com.seckill.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * @description: 用于秒杀过程及结束结果返回，封装JSON返回的结果
 * @author: Black Tom
 * @create: 2020-06-24 14:51
 **/
@Data
public class SeckillResult<T> {
	//不要过度使用lombok插件
	@Setter(AccessLevel.NONE)
	private boolean success;

	private T data;

	private String error;

	public SeckillResult(boolean success, String error) {
		this.success = success;
		this.error = error;
	}

	public SeckillResult(T data) {
		this.success = true;
		this.data = data;
	}

	public SeckillResult(boolean success, T data) {
		this.success = success;
		this.data = data;
	}

	/**
	 * 成功
	 *
	 * @param data
	 * @return
	 */
	public static SeckillResult ok(Object data) {
		return new SeckillResult<>(data);
	}

	/**
	 * 失败
	 *
	 * @param error
	 * @return
	 */
	public static SeckillResult error(String error) {
		return new SeckillResult<>(false, error);
	}
}
