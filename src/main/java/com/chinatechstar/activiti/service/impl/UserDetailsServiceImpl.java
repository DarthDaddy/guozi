package com.chinatechstar.activiti.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Activiti7工作流的自带用户信息，MSCode微服务平台采用框架平台的用户信息
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String arg) {
		return new User("admin", passwordEncoder.encode("123456"), AuthorityUtils.createAuthorityList("ROLE_ACTIVITI_ADMIN", "ROLE_ACTIVITI_USER"));
	}

}
