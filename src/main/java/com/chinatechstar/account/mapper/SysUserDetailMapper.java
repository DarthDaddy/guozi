package com.chinatechstar.account.mapper;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.chinatechstar.account.entity.SysUserDetail;

/**
 * 用户详细信息的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface SysUserDetailMapper {

	/**
	 * 根据用户名或手机号查询用户的租户编码
	 *
	 * @param username 用户名
	 * @param mobile   手机号
	 * @return
	 */
	String getTenantCodeByUser(@Param(value = "username") String username, @Param(value = "mobile") String mobile);

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
	 * 查询原密码
	 *
	 * @param id         用户ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	String getPasswordById(@Param(value = "id") Long id, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询当前用户的角色
	 *
	 * @param username   用户名
	 * @param mobile     手机号
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<String> getRoleCodeBySysUser(@Param(value = "username") String username, @Param(value = "mobile") String mobile, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询当前用户的授权菜单
	 *
	 * @param username   用户名
	 * @param mobile     手机号
	 * @param postCode   岗位编码
	 * @param userId     用户ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysMenuAuthorityTree(@Param(value = "username") String username, @Param(value = "mobile") String mobile, @Param(value = "postCode") String postCode, @Param(value = "userId") Long userId, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询当前用户的授权按钮隐藏项
	 *
	 * @param username   用户名
	 * @param mobile     手机号
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<String> queryRoleMenuButton(@Param(value = "username") String username, @Param(value = "mobile") String mobile, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 新增用户
	 * 
	 * @param sysUserDetail 用户详细信息
	 * @return
	 */
	int insertSysUser(SysUserDetail sysUserDetail);

	/**
	 * 编辑用户详细信息
	 * 
	 * @param sysUserDetail 用户详细信息
	 * @return
	 */
	int updateSysUserDetail(SysUserDetail sysUserDetail);

	/**
	 * 根据字段编辑用户信息
	 *
	 * @param fieldValue 修改的字段值
	 * @param field      修改的字段
	 * @param id         用户ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int updateSysUserInfo(@Param(value = "fieldValue") String fieldValue, @Param(value = "field") String field, @Param(value = "id") Long id, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据邮箱更新密码
	 *
	 * @param newPassword 新密码
	 * @param email       邮箱
	 * @param tenantCode  租户编码
	 * @return
	 */
	int updatePasswordByEmail(@Param(value = "newPassword") String newPassword, @Param(value = "email") String email, @Param(value = "tenantCode") String tenantCode);

}
