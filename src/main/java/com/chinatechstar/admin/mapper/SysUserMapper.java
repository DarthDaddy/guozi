package com.chinatechstar.admin.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.chinatechstar.admin.entity.SysUser;

/**
 * 用户信息的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface SysUserMapper {

	/**
	 * 查询用户分页或导出数据
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysUser(Map<String, Object> paramMap);

	/**
	 * 根据用户ID查询用户名的数据列表
	 *
	 * @param id         用户ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryUsername(@Param(value = "array") Long[] id, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据用户名查询用户ID的数据列表
	 *
	 * @param username   用户名
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<Long> querySysUserId(@Param(value = "array") String[] username, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询是否已存在此用户名或邮箱或手机号
	 *
	 * @param username   用户名
	 * @param email      邮箱
	 * @param mobile     手机号
	 * @return
	 */
	Integer getSysUserByIdentification(@Param(value = "username") String username, @Param(value = "email") String email, @Param(value = "mobile") String mobile);

	/**
	 * 查询是否已存在此邮箱或手机号
	 *
	 * @param id         用户ID
	 * @param email      邮箱
	 * @param mobile     手机号
	 * @param tenantCode 租户编码
	 * @return
	 */
	Integer getSysUserByIdEmailMobile(@Param(value = "id") Long id, @Param(value = "email") String email, @Param(value = "mobile") String mobile, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据机构ID查询用户ID
	 *
	 * @param orgId      机构ID
	 * @param roleId     角色ID
	 * @param assign     是否分配（0是未分配，1是已分配）
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<String> queryUserIdByOrgId(@Param(value = "orgId") Long orgId, @Param(value = "roleId") Long roleId, @Param(value = "assign") Short assign, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据机构ID查询模型信息的的用户ID
	 *
	 * @param orgId      机构ID
	 * @param modelId    模型ID
	 * @param assign     是否分配（0是未分配，1是已分配）
	 * @return
	 */
	List<String> queryModelUserIdByOrgId(@Param(value = "orgId") Long orgId, @Param(value = "modelId") String modelId, @Param(value = "assign") Short assign);

	/**
	 * 获取用户名和昵称
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryUsernameNickname(@Param(value = "tenantCode") String tenantCode);

	/**
	 * 新增用户
	 * 
	 * @param sysUser 用户对象
	 * @return
	 */
	int insertSysUser(SysUser sysUser);

	/**
	 * 编辑用户
	 * 
	 * @param sysUser 用户对象
	 * @return
	 */
	int updateSysUser(SysUser sysUser);

	/**
	 * 删除用户
	 *
	 * @param id         用户ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteSysUser(@Param(value = "array") Long[] id, @Param(value = "tenantCode") String tenantCode);

}
