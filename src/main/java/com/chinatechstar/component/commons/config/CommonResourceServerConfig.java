package com.chinatechstar.component.commons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * OAuth2资源服务器
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Configuration
@EnableResourceServer
public class CommonResourceServerConfig extends ResourceServerConfigurerAdapter {

	private final ResourceServerProperties sso;

	@Autowired
	public CommonResourceServerConfig(ResourceServerProperties sso) {
		this.sso = sso;
	}

	/**
	 * 配置拦截路径的安全规则
	 */
	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.headers().frameOptions().disable();
		httpSecurity.authorizeRequests()
				.antMatchers("/**/oauth/token", "/**/actuator/**", "/**/sysuserdetail/registerAccount", "/**/sysuserdetail/retrievePassword",
						"/**/sysuserdetail/compareCaptcha", "/**/captcha/generateImageCaptcha","/**/notification/queryNotifications",
						"/**/captcha/getSmsCaptcha", "/**/swagger-ui/**", "/**/file/deleteFile",
						"/**/swagger-ui.html/**", "/**/webjars/springfox-swagger-ui/**", "/**/swagger-resources/**", "/**/v2/api-docs/**",
						"/**/static/upload/**", "/**/druid/**", "/**/ureport/**")
				.permitAll().anyRequest().authenticated().and().csrf().disable();
	}

	/**
	 * 实例化会话监听
	 * 
	 * @return
	 */
	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

}
