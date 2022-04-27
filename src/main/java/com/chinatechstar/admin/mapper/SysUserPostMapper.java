package com.chinatechstar.admin.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.chinatechstar.admin.entity.SysUserPost;

/**
 * 用户与岗位关联信息的数据持久接口层
 *
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface SysUserPostMapper {

	/**
	 * 查询用户与岗位关联信息分页或导出数据
	 *
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysUserPost(Map<String, Object> paramMap);

	/**
	 * 查询用户与岗位关联信息的下拉框数据
	 *
	 * @param userId     用户ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysUserPostTree(@Param(value = "userId") String userId, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 校验用户与岗位关联信息
	 *
	 * @param id         用户与岗位关联ID
	 * @param userId     用户ID
	 * @param postType   岗位类型
	 * @param tenantCode 租户编码
	 * @return
	 */
	Integer getSysUserPostInfo(@Param(value = "id") Long id, @Param(value = "userId") Long userId, @Param(value = "postType") Short postType, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 新增用户与岗位关联信息
	 *
	 * @param sysUserPost 用户与岗位关联对象
	 * @return
	 */
	int insertSysUserPost(SysUserPost sysUserPost);

	/**
	 * 编辑用户与岗位关联信息
	 *
	 * @param sysUserPost 用户与岗位关联对象
	 * @return
	 */
	int updateSysUserPost(SysUserPost sysUserPost);

	/**
	 * 删除用户与岗位关联信息
	 *
	 * @param id         用户与岗位关联ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteSysUserPost(@Param(value = "array") Long[] id, @Param(value = "tenantCode") String tenantCode);

}
