package com.seckill.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class SuccessKilled implements Serializable {
	private long seckillId;//秒杀到的商品ID

	private long userPhone;//秒杀用户的手机号

	private short state; //订单状态， -1:无效 0:成功 1:已付款

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private Seckill seckill; //秒杀商品，和订单是一对多的关系
}
