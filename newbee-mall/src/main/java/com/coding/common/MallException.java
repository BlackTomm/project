package com.coding.common;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-16 18:36
 **/
public class MallException extends RuntimeException {
	public MallException() {
	}

	public MallException(String message) {
		super(message);
	}

	/**
	 * 丢出一个异常
	 *
	 * @param message
	 */
	public static void fail(String message) {
		throw new MallException(message);
	}
}
