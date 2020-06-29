package com.seckill.service.impl;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatSeckillException;
import com.seckill.exception.SeckillClosedException;
import com.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SeckillServiceImplTest {
	@Autowired
	private SeckillService seckillService;

	@Test
	public void getSeckillList() {
		List<Seckill> seckillList = seckillService.getSeckillList();
		log.info("list={}", seckillList);
	}

	@Test
	public void getById() {
		long id = 1000L;
		Seckill seckill = seckillService.getById(id);
		log.info("seckill = {}", seckill);
	}

	@Test
	public void exportSeckillUrl() {
		long id = 1003L;
		Exposer exposer = seckillService.exportSeckillUrl(id);
		log.info("exposer={}", exposer);
	}

	@Test
	public void executeSeckill() {
		long id = 1003L;
		long userPhone = 19290292900L;
		String md5 = "f5fd0ce496720966b85ed7ae8933715e";
		SeckillExecution execution = seckillService.executeSeckill(id, userPhone, md5);
		log.info("result={}", execution);
	}

	//将exportSeckillUrl()与executeSeckill()测试方法整合
	@Test
	public void testSeckillLogic() {
		long id = 1000L;
		long userPhone = 19290292900L;

		Exposer exposer = seckillService.exportSeckillUrl(id);
		if (exposer.isExposed()) {
			String md5 = exposer.getMd5();
			try {
				SeckillExecution execution = seckillService.executeSeckill(id, userPhone, md5);
				log.info("result={}", execution);
			} catch (RepeatSeckillException e2) {
				log.error(e2.getMessage());
			} catch (SeckillClosedException e1) {
				log.error(e1.getMessage());
			}
		} else {
			//秒杀未开启
			log.warn("exposer={}", exposer);
		}
	}
}
