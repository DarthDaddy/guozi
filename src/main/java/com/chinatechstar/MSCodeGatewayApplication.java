package com.chinatechstar;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import com.bstek.ureport.console.UReportServlet;
import com.chinatechstar.admin.mapper.SysRoleMapper;
import com.chinatechstar.admin.mapper.SysUrlMapper;
import com.chinatechstar.cache.redis.constants.ApplicationConstants;
import com.chinatechstar.cache.redis.util.RedisUtils;
import com.chinatechstar.gateway.filter.GatewayFilter;

import io.micrometer.core.instrument.MeterRegistry;

/**
 * MSCode的入口网关启动类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@SpringBootApplication(scanBasePackages = "com.chinatechstar")
@EnableResourceServer
@EnableOAuth2Client
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableDiscoveryClient
@EnableZuulProxy
@ImportResource("classpath:ureport-console-context.xml")
public class MSCodeGatewayApplication implements CommandLineRunner {

	@Autowired
	private SysUrlMapper sysUrlMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private RedisUtils redisUtils;

	public static void main(String[] args) {
		SpringApplication.run(MSCodeGatewayApplication.class);
	}

	@Bean
	public GatewayFilter gatewayFilter() {
		return new GatewayFilter();
	}

	@Bean
	public ServletRegistrationBean buildUReportServlet() {
		return new ServletRegistrationBean(new UReportServlet(), "/ureport/*");
	}

	@Bean
	public MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
		return (registry) -> registry.config().commonTags("application", applicationName);
	}

	/**
	 * 初始化角色编码对应的URL授权数据到Redis缓存，供网关验证权限
	 */
	@Override
	public void run(String... args) throws Exception {
		List<String> roleCodeList = sysRoleMapper.queryRoleCodeList();
		for (int i = 0; i < roleCodeList.size(); i++) {
			String roleCode = roleCodeList.get(i);
			List<String> url = sysUrlMapper.queryRoleUrl(roleCode);
			redisUtils.psetex(ApplicationConstants.URL_ROLECODE_PREFIX + roleCode, url == null ? Collections.emptyList().toString() : url.toString());
		}
	}

}
