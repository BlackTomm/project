package com.seckill.mapper;

import com.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SuccessKilledMapperTest {

	@Autowired
	private SuccessKilledMapper successKilledMapper;

	@Test
	public void testInsertSuccessKilled() {
		long id = 1001L;
		long userPhone = 18829200332L;
		int insertCount = successKilledMapper.insertSuccessKilled(id, userPhone);
		System.out.println("insertCount=" + insertCount);
	}

	@Test
	public void testQueryByIdWithSeckill() {
		//根据插入数据进行查询，如果查询表中不存在的数据会报空指针异常
		long id = 1001L;
		long userPhone = 18829200332L;
		SuccessKilled successKilled = successKilledMapper.queryByIdWithSeckill(id, userPhone);
		System.out.println(successKilled);
		System.out.println(successKilled.getSeckill());

	}

}