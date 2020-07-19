package com.coding.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 分页结果显示
 * @author: Black Tom
 * @create: 2020-07-09 15:40
 **/
@Data
@NoArgsConstructor
public class Result<T> implements Serializable {
	private int resultCode;
	private String message;
	private T data;

	public Result(int resultCode, String message) {
		this.resultCode = resultCode;
		this.message = message;
	}
}
