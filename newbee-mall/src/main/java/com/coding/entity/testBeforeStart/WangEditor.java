package com.coding.entity.testBeforeStart;

import lombok.Data;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-14 15:45
 **/
@Data
public class WangEditor {
	private Integer errno; //错误代码，0 表示没有错误。
	private String[] data; //已上传的图片路径

	public WangEditor() {
		super();
	}
	public WangEditor(String[] data) {
		super();
		this.errno = 0;
		this.data = data;
	}
}
