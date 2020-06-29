package com.seckill.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @description:暴露秒杀地址
 * @author: Black Tom
 * @create: 2020-06-23 18:27
 **/
@Data
@ToString
public class Exposer {
	//表示秒杀是否开启
	private boolean exposed;

	//加密措施，避免用户通过抓包拿到秒杀地址
	private String md5;

	private long seckillId;

	//系统当前时间（毫秒）
	private long now;

	//秒杀开启时间
	private long start;

	//秒杀结束时间
	private long end;

	public Exposer(boolean exposed, long seckillId) {
		this.exposed = exposed;
		this.seckillId = seckillId;
	}

	public Exposer(boolean exposed, String md5, long seckillId) {
		this.exposed = exposed;
		this.md5 = md5;
		this.seckillId = seckillId;
	}

	public Exposer(boolean exposed, long seckillId, long now, long start, long end) {
		this.exposed = exposed;
		this.seckillId = seckillId;
		this.now = now;
		this.start = start;
		this.end = end;
	}

}
