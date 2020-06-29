package com.seckill.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @description: redis配置类
 * @author: Black Tom
 * @create: 2020-06-28 18:11
 **/
@Slf4j
@Configuration
public class JedisConfig {
	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.timeout}")
	private int timeout;

	@Value("${spring.redis.jedis.pool.max-active}")
	private int maxActive;

	@Value("${spring.redis.jedis.pool.max-idle}")
	private int maxIdle;

	@Value("${spring.redis.jedis.pool.min-idle}")
	private int minIdle;

	@Value("${spring.redis.jedis.pool.max-wait}")
	private long maxWaitMillis;

	@Bean
	public JedisPool redisPoolFactory() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(maxActive);
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		JedisPool jedisPool = new JedisPool(jedisPoolConfig,host,port,timeout,null);

		log.info("JedisPool注入成功");
		log.info("redis地址：" + host + ":" + port);
		return jedisPool;
	}

}
