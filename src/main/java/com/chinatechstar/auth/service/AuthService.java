package com.chinatechstar.auth.service;

import java.util.LinkedHashMap;

/**
 * 用户认证授权信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 *
 */
public interface AuthService {

	/**
	 * 根据用户名查询当前用户信息
	 * 
	 * @param username 用户名
	 * @return
	 */
	LinkedHashMap<String, Object> getSysUser(String username);

}
