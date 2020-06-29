package com.seckill.mapper;

import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SeckillMapperTest {
	@Autowired
	private SeckillMapper seckillMapper;

	@Test
	public void testQueryAll() {
		List<Seckill> list = seckillMapper.queryAll();
		for (Seckill seckill : list) {
			System.out.println(seckill);
		}
	}

	@Test
	public void testQueryById() {
		long id = 1000;
		Seckill seckill = seckillMapper.queryById(id);
		System.out.println(seckill);
	}

	@Test
	public void testReduceNumber() throws Exception {
		Date killTime = new Date();
		int updateCount = seckillMapper.reduceNumber(1003L, killTime);
		System.out.println("updateCount: " + updateCount);
	}

}