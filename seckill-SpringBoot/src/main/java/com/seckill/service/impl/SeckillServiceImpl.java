package com.seckill.service.impl;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillStateEnum;
import com.seckill.exception.RepeatSeckillException;
import com.seckill.exception.SeckillClosedException;
import com.seckill.exception.SeckillException;
import com.seckill.mapper.SeckillMapper;
import com.seckill.mapper.SuccessKilledMapper;
import com.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-06-23 19:01
 **/
@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {
	@Autowired
	private SeckillMapper seckillMapper;
	@Autowired
	private SuccessKilledMapper successKilledMapper;
	@Autowired
	private RedisTemplate redisTemplate;

	//设置秒杀redis缓存的key
	private final String key = "seckill";
	//设置盐值字符串，随便定义，用于混淆MD5值
	private final String salt = "jaskdjladlfliueu2810101f372jsajdj";

	@Override
	public List<Seckill> getSeckillList() {
		List<Seckill> seckillList = redisTemplate.boundHashOps("seckill").values();
		if (seckillList == null || seckillList.size() == 0) {
			//说明缓存中没有秒杀列表数据
			//查询数据库中秒杀列表数据，并将列表数据循环放入redis缓存中
			seckillList = seckillMapper.queryAll();
			for (Seckill seckill : seckillList) {
				//将秒杀列表数据依次放入redis缓存中，key:秒杀表的ID值；value:秒杀商品数据
				redisTemplate.boundHashOps(key).put(seckill.getSeckillId(), seckill);
				log.info("queryAll -> 从数据库中读取放入缓存中");
			}
		} else {
			log.info("queryAll -> 从缓存中读取");
		}
		return seckillList;
	}

	@Override
	public Seckill getById(long seckillId) {
		return seckillMapper.queryById(seckillId);
	}

	/**
	 * @Description:
	 * @Param: [seckillId]
	 * @return: seckill.dto.Exposer
	 */
	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		/*Seckill seckill = seckillMapper.queryById(seckillId);*/
		/*替换以上查询，优先使用redis做cache缓冲层,也意味着cache查询不到，
		要在数据库再次查询，判断该商品是否在秒杀列表*/
		Seckill seckill = (Seckill) redisTemplate.boundHashOps(key).get(seckillId);
		if (seckill == null) {
			//说明redis缓存中没有此key对应的value，需要查询数据库，并将数据放入缓存中
			seckill = seckillMapper.queryById(seckillId);
			if (seckill == null) {
				return new Exposer(false, seckillId);
			} else {
				//查询到了，存入redis缓存中。 key:秒杀表的ID值； value:秒杀表数据
				redisTemplate.boundHashOps(key).put(seckill.getSeckillId(), seckill);
				log.info("RedisTemplate -> 从数据库中读取并放入缓存中");
			}
		} else {
			log.info("RedisTemplate -> 从缓存中读取");
		}

		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		//系统当前时间
		Date currentTime = new Date();
		//根据当前时间判断是都在秒杀时间段内
		if (currentTime.getTime() < startTime.getTime() || currentTime.getTime() > endTime.getTime()) {
			return new Exposer(false, seckillId, currentTime.getTime()
					, startTime.getTime(), endTime.getTime());
		}
		//秒杀成功才添加md5值
		String md5 = getMd5(seckillId);
		return new Exposer(true, md5, seckillId);
	}

	private String getMd5(long seckillId) {
		String base = seckillId + "/" + salt;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		log.info("md5: {}", md5);
		return md5;
	}

	/**
	 * 使用注解式事务方法的有优点：开发团队达成了一致约定，明确标注事务方法的编程风格
	 * 使用事务控制需要注意：
	 * 1.保证事务方法的执行时间尽可能短，不要穿插其他网络操作PRC/HTTP请求（可以将这些请求剥离出来）
	 * 2.不是所有的方法都需要事务控制，如只有一条修改的操作、只读操作等是不需要进行事务控制的
	 * <p>
	 * Spring默认只对运行期异常进行事务的回滚操作，对于编译异常Spring是不进行回滚的，所以对于需要进行事务控制的方法尽可能将可能抛出的异常都转换成运行期异常
	 */
	@Override
	@Transactional
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatSeckillException, SeckillClosedException {
		//如果md5的值不匹配，则判断秒杀地址错误（秒杀商品不一致），返回秒杀数据被重写
		if (md5 == null || !md5.equals(getMd5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		//md5相等，意味着对想要的商品可以执行秒杀逻辑：减库存 + 记录购买行为
		Date currentKillTime = new Date();
		try {
			//购买行为，返回由于插入导致变动的行数
			int insertCount = successKilledMapper.insertSuccessKilled(seckillId, userPhone);
			if (insertCount <= 0) {
				//重复秒杀
				throw new RepeatSeckillException("seckill repeated");
			} else {
				//秒杀成功，先更新库存，再更新redis缓冲层
				int updateCount = seckillMapper.reduceNumber(seckillId, currentKillTime);
				if (updateCount <= 0) {
					//没有更新到记录
					throw new SeckillClosedException("seckill is closed");
				} else {
					SuccessKilled successKilled = successKilledMapper.queryByIdWithSeckill(seckillId, userPhone);
					//更新redis
					Seckill seckill = (Seckill) redisTemplate.boundHashOps(key).get(seckillId);
					seckill.setNumber(seckill.getNumber() - 1);
					redisTemplate.boundHashOps(key).put(seckillId, seckill);
					return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);

				}
			}
		} catch (SeckillClosedException e1) {
			throw e1;
		} catch (RepeatSeckillException e2) {
			throw e2;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			//所有编译器异常转化为运行期异常
			throw new SeckillException("seckill inner error:" + e.getMessage());
		}
	}

}
