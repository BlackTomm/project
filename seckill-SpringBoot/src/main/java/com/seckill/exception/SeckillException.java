package com.seckill.exception;

/**
 * @description: 秒杀异常处理
 * @author: Black Tom
 * @create: 2020-06-23 19:27
 **/
public class SeckillException extends RuntimeException {
	public SeckillException(String message) {
		super(message);
	}

	public SeckillException(String message, Throwable cause) {
		super(message, cause);
	}
}
