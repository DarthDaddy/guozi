package com.chinatechstar.auth.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 用户认证授权信息的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 *
 */
public interface AuthMapper {

	/**
	 * 根据用户名查询当前用户信息
	 *
	 * @param username   用户名
	 * @return
	 */
	LinkedHashMap<String, Object> getSysUser(@Param(value = "username") String username);

	/**
	 * 根据用户名查询用户详细信息
	 *
	 * @param username   用户名
	 * @return
	 */
	LinkedHashMap<String, String> getSysUserByUsername(@Param(value = "username") String username);

	/**
	 * 根据手机号查询用户详细信息
	 *
	 * @param mobile     手机号
	 * @return
	 */
	LinkedHashMap<String, String> getSysUserByMobile(@Param(value = "mobile") String mobile);

	/**
	 * 根据用户名查询角色编码
	 *
	 * @param username   用户名
	 * @return
	 */
	List<String> queryRoleCodeByUsername(@Param(value = "username") String username);

	/**
	 * 根据手机号查询角色编码
	 *
	 * @param mobile     手机号
	 * @return
	 */
	List<String> queryRoleCodeByMobile(@Param(value = "mobile") String mobile);

}
