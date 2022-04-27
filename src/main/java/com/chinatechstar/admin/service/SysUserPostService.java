package com.chinatechstar.admin.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinatechstar.admin.entity.SysUserPost;

/**
 * 用户与岗位关联信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface SysUserPostService {

	/**
	 * 查询用户与岗位关联信息的数据分页
	 * 
	 * @param currentPage     当前页数
	 * @param pageSize        每页记录数
	 * @param userId          用户ID
	 * @param postCode        岗位编码
	 * @param postName        岗位名称
	 * @param postType        岗位类型 1:主岗位，2:副岗位
	 * @param status          状态 0：隐藏 1：显示
	 * @param postDescription 岗位描述
	 * @param sorter          排序
	 * @return
	 */
	Map<String, Object> querySysUserPost(Integer currentPage, Integer pageSize, Long userId, String postCode, String postName, Short postType, Short status,
			String postDescription, String sorter);

	/**
	 * 查询用户与岗位关联信息的下拉框数据
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysUserPostTree(String userId);

	/**
	 * 查询用户与岗位关联信息的导出数据列表
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysUserPostForExcel(Map<String, Object> paramMap);

	/**
	 * 新增用户与岗位关联信息
	 * 
	 * @param sysUserPost 用户与岗位关联对象
	 */
	void insertSysUserPost(SysUserPost sysUserPost);

	/**
	 * 编辑用户与岗位关联信息
	 * 
	 * @param sysUserPost 用户与岗位关联对象
	 */
	void updateSysUserPost(SysUserPost sysUserPost);

	/**
	 * 删除用户与岗位关联信息
	 * 
	 * @param id 用户与岗位关联ID
	 */
	void deleteSysUserPost(Long[] id);

}
