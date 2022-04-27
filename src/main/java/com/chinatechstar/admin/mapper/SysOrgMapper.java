package com.chinatechstar.admin.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.chinatechstar.admin.entity.SysOrg;

/**
 * 机构信息的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface SysOrgMapper {

	/**
	 * 查询机构分页或导出数据
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysOrg(Map<String, Object> paramMap);

	/**
	 * 查询机构的树数据列表
	 * 
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysOrgTree(@Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询机构用户的树数据
	 * 
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryOrgUserTree(@Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询所有机构ID
	 *
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<Long> queryOrgId(@Param(value = "tenantCode") String tenantCode);

	/**
	 * 新增机构
	 * 
	 * @param sysOrg 机构对象
	 * @return
	 */
	int insertSysOrg(SysOrg sysOrg);

	/**
	 * 编辑机构
	 * 
	 * @param sysOrg 机构对象
	 * @return
	 */
	int updateSysOrg(SysOrg sysOrg);

	/**
	 * 删除机构
	 *
	 * @param id         机构ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteSysOrg(@Param(value = "array") Long[] id, @Param(value = "tenantCode") String tenantCode);

}
