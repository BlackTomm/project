package com.seckill.exception;

/**
 * @description: 重复秒杀异常
 * @author: Black Tom
 * @create: 2020-06-23 19:29
 **/
public class RepeatSeckillException extends SeckillException {
	public RepeatSeckillException(String message) {
		super(message);
	}

	public RepeatSeckillException(String message, Throwable cause) {
		super(message, cause);
	}
}
