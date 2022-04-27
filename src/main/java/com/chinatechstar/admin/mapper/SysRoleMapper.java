package com.chinatechstar.admin.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.chinatechstar.admin.entity.SysRole;

/**
 * 角色信息的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface SysRoleMapper {

	/**
	 * 查询角色分页或导出数据
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysRole(Map<String, Object> paramMap);

	/**
	 * 查询角色名称的下拉框数据列表
	 * 
	 * @param tenantCode 租户编码
	 * @param userId     用户ID
	 * @param postCode   岗位编码
	 * @param assign     是否授权（0是未授权，1是已授权）
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryRoleName(@Param(value = "tenantCode") String tenantCode, @Param(value = "userId") Long userId, @Param(value = "postCode") String postCode, @Param(value = "assign") Short assign);

	/**
	 * 查询角色编码的下拉框数据列表
	 *
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryRoleCode(@Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询角色编码数据列表
	 *
	 * @return
	 */
	List<String> queryRoleCodeList();

	/**
	 * 查询角色的多选框数据列表
	 *
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryRoleNameCheckbox(@Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询是否已存在此角色编码
	 *
	 * @param roleCode   角色编码
	 * @param tenantCode 租户编码
	 * @return
	 */
	Integer getSysRoleByRoleCode(@Param(value = "roleCode") String roleCode, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询当前用户的过滤数据字段
	 *
	 * @param menuCode   菜单编码
	 * @param username   用户名
	 * @param tenantCode 租户编码
	 * @return
	 */
	String queryRoleData(@Param(value = "menuCode") String menuCode, @Param(value = "username") String username, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据角色ID查询用户ID
	 *
	 * @param roleId     角色ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<Long> queryUserIdByRoleId(@Param(value = "roleId") Long roleId, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据用户ID查询角色ID
	 *
	 * @param userId     用户ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<Long> queryRoleIdByUserId(@Param(value = "userId") Long userId, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 新增角色
	 * 
	 * @param sysRole 角色对象
	 * @return
	 */
	int insertSysRole(SysRole sysRole);

	/**
	 * 编辑角色
	 * 
	 * @param sysRole 角色对象
	 * @return
	 */
	int updateSysRole(SysRole sysRole);

	/**
	 * 删除角色
	 *
	 * @param id         角色ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteSysRole(@Param(value = "array") Long[] id, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 新增角色和用户关联信息
	 *
	 * @param id         ID
	 * @param roleId     角色ID
	 * @param userId     用户ID
	 * @param postCode   岗位编码
	 * @param tenantCode 租户编码
	 * @return
	 */
	int insertSysRoleUser(@Param(value = "id") Long id, @Param(value = "roleId") Long roleId, @Param(value = "userId") Long userId, @Param(value = "postCode") String postCode, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 删除角色和用户关联信息
	 *
	 * @param userId     用户ID
	 * @param roleId     角色ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteSysRoleUser(@Param(value = "userId") Long userId, @Param(value = "roleId") Long roleId, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 删除角色和用户岗位关联信息
	 *
	 * @param userId     用户ID
	 * @param postCode   岗位编码
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteSysRoleUserPost(@Param(value = "userId") Long userId, @Param(value = "postCode") String postCode, @Param(value = "tenantCode") String tenantCode);

}
