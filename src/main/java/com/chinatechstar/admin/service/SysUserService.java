package com.chinatechstar.admin.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinatechstar.admin.entity.SysUser;

/**
 * 用户信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface SysUserService {

	/**
	 * 查询在线用户分页
	 * 
	 * @param currentPage 当前页数
	 * @param pageSize    每页记录数
	 * @param username    用户名
	 * @param nickname    昵称
	 * @param mobile      手机号
	 * @param roleId      角色ID
	 * @param orgId       机构ID
	 * @param sorter      排序
	 * @return
	 */
	Map<String, Object> queryOnlineSysUser(Integer currentPage, Integer pageSize, String username, String nickname, String mobile, Long[] roleId, Long orgId,
			String sorter);

	/**
	 * 查询用户分页
	 * 
	 * @param currentPage 当前页数
	 * @param pageSize    每页记录数
	 * @param username    用户名
	 * @param status      状态
	 * @param orgId       机构ID
	 * @param roleId      角色ID
	 * @param nickname    昵称
	 * @param mobile      手机号
	 * @param sorter      排序
	 * @return
	 */
	Map<String, Object> querySysUser(Integer currentPage, Integer pageSize, String username, String status, Long orgId, Long[] roleId, String nickname,
			String mobile, String sorter);

	/**
	 * 查询用户的导出数据列表
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysUserForExcel(Map<String, Object> paramMap);

	/**
	 * 根据用户ID查询用户名的数据列表
	 * 
	 * @param id 用户ID
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryUsername(Long[] id);

	/**
	 * 根据用户名查询用户ID的数据列表
	 * 
	 * @param username 用户名
	 * @return
	 */
	List<Long> querySysUserId(String[] username);

	/**
	 * 新增用户
	 * 
	 * @param sysUser 用户对象
	 */
	void insertSysUser(SysUser sysUser);

	/**
	 * 将对应的角色授予用户
	 * 
	 * @param roleId   角色ID
	 * @param userId   用户ID
	 * @param postCode 岗位编码
	 */
	void insertRoleIdUserId(Long[] roleId, Long userId, String postCode);

	/**
	 * 编辑用户
	 * 
	 * @param sysUser 用户对象
	 */
	void updateSysUser(SysUser sysUser);

	/**
	 * 删除用户
	 * 
	 * @param id 用户ID
	 */
	void deleteSysUser(Long[] id);

}
