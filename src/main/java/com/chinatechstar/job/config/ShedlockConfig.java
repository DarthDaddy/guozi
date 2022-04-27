package com.chinatechstar.job.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.jedis.JedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 分布式定时任务调度配置类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class ShedlockConfig {

	/**
	 * Jedis作为分布式锁提供者
	 * 
	 * @return
	 */
	@Bean
	public LockProvider lockProvider(Environment environment) {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, environment.getProperty("redis.host"), Integer.valueOf(environment.getProperty("redis.port")),
				Integer.valueOf(environment.getProperty("redis.expiredTime")), environment.getProperty("redis.password"));
		return new JedisLockProvider(jedisPool, "MSCODE:SHEDLOCK");
	}

}