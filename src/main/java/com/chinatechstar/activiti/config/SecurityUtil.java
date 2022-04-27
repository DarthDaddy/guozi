package com.chinatechstar.activiti.config;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Activiti安全访问工具类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Component
public class SecurityUtil {

	private Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

	@Autowired
	private UserDetailsService userDetailsService;

	public void logInAs(String username) {
		UserDetails user = userDetailsService.loadUserByUsername(username);
		if (user == null) {
			throw new IllegalStateException("User " + username + " doesn't exist, please provide a valid user");
		}
		logger.info("登录用户名是： {}", username);
		SecurityContextHolder.setContext(new SecurityContextImpl(new Authentication() {
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return user.getAuthorities();
			}

			@Override
			public Object getCredentials() {
				return user.getPassword();
			}

			@Override
			public Object getDetails() {
				return user;
			}

			@Override
			public Object getPrincipal() {
				return user;
			}

			@Override
			public boolean isAuthenticated() {
				return true;
			}

			@Override
			public void setAuthenticated(boolean isAuthenticated) {
				// 默认，可根据实际业务逻辑重写
			}

			@Override
			public String getName() {
				return user.getUsername();
			}
		}));
		org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(username);
	}

}
