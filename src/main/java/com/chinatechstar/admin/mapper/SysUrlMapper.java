package com.chinatechstar.admin.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.chinatechstar.admin.entity.SysUrl;

/**
 * 接口信息的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface SysUrlMapper {

	/**
	 * 查询接口分页或导出数据
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysUrl(Map<String, Object> paramMap);

	/**
	 * 查询是否已存在此接口
	 *
	 * @param url        URL
	 * @param tenantCode 租户编码
	 * @return
	 */
	Integer getSysUrlByUrl(@Param(value = "url") String url, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据接口ID查询对应的角色ID
	 *
	 * @param urlId      接口ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<String> queryRoleIdByUrlId(@Param(value = "urlId") Long urlId, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询角色编码对应的接口
	 *
	 * @param roleCode   角色编码
	 * @return
	 */
	List<String> queryRoleUrl(@Param(value = "roleCode") String roleCode);

	/**
	 * 根据角色ID查询对应的接口ID
	 *
	 * @param roleId     角色ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<String> queryUrlIdByRoleId(@Param(value = "roleId") Long roleId, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 新增接口
	 * 
	 * @param sysUrl 接口对象
	 * @return
	 */
	int insertSysUrl(SysUrl sysUrl);

	/**
	 * 将接口授权给角色
	 *
	 * @param id         接口与角色关联ID
	 * @param urlId      接口ID
	 * @param roleId     角色ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	void insertUrlIdRoleId(@Param(value = "id") Long id, @Param(value = "urlId") Long urlId, @Param(value = "roleId") Long roleId, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 编辑接口
	 * 
	 * @param sysUrl 接口对象
	 * @return
	 */
	int updateSysUrl(SysUrl sysUrl);

	/**
	 * 删除接口
	 *
	 * @param id         接口ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteSysUrl(@Param(value = "array") Long[] id, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据接口ID删除接口与角色关联信息
	 *
	 * @param urlId      接口ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteUrlRole(@Param(value = "urlId") Long urlId, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据角色ID删除接口与角色关联信息
	 *
	 * @param roleId     角色ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteRoleUrl(@Param(value = "roleId") Long roleId, @Param(value = "tenantCode") String tenantCode);

}
