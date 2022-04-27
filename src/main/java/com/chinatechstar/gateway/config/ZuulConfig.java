package com.chinatechstar.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.adapter.gateway.zuul.filters.SentinelZuulErrorFilter;
import com.alibaba.csp.sentinel.adapter.gateway.zuul.filters.SentinelZuulPostFilter;
import com.alibaba.csp.sentinel.adapter.gateway.zuul.filters.SentinelZuulPreFilter;
import com.netflix.zuul.ZuulFilter;

/**
 * Zuul过滤器配置
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Configuration
public class ZuulConfig {

	@Bean
	public ZuulFilter sentinelZuulPreFilter() {
		return new SentinelZuulPreFilter(10000);
	}

	@Bean
	public ZuulFilter sentinelZuulPostFilter() {
		return new SentinelZuulPostFilter(1000);
	}

	@Bean
	public ZuulFilter sentinelZuulErrorFilter() {
		return new SentinelZuulErrorFilter(-1);
	}

}