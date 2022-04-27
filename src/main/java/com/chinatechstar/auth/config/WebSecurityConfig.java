package com.chinatechstar.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.chinatechstar.auth.service.impl.AuthServiceImpl;

/**
 * Web安全配置类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 *
 */
@Configuration
@Order(11)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthServiceImpl authServiceImpl;

	/**
	 * 为特定的Http请求配置基于Web的安全约束
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests()
				.antMatchers("/**/oauth/token", "/**/actuator/**", "/**/sysuserdetail/registerAccount", "/**/sysuserdetail/retrievePassword",
						"/**/sysuserdetail/compareCaptcha", "/**/captcha/generateImageCaptcha", "/**/captcha/getSmsCaptcha", "/**/swagger-ui/**",
						"/**/swagger-ui.html/**", "/**/webjars/springfox-swagger-ui/**", "/**/swagger-resources/**", "/**/v2/api-docs/**",
						"/**/static/upload/**")
				.permitAll().anyRequest().authenticated().and().csrf().disable();
	}

	/**
	 * 配置认证信息
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(authServiceImpl).passwordEncoder(new BCryptPasswordEncoder());
	}

	/**
	 * 实例化AuthenticationManager对象，以处理认证请求
	 */
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}