package com.seckill.exception;

/**
 * @description: 秒杀关闭异常
 * @author: Black Tom
 * @create: 2020-06-23 19:31
 **/
public class SeckillClosedException extends SeckillException {
	public SeckillClosedException(String message) {
		super(message);
	}

	public SeckillClosedException(String message, Throwable cause) {
		super(message, cause);
	}
}
