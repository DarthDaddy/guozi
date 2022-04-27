package com.chinatechstar.admin.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.chinatechstar.admin.entity.SysRegion;

/**
 * 区域信息的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface SysRegionMapper {

	/**
	 * 查询区域分页或导出数据
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysRegion(Map<String, Object> paramMap);

	/**
	 * 查询区域的树数据列表
	 * 
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysRegionTree(@Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询全部省份数据
	 *
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryProvince(@Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据省份代码查询对应地市数据
	 *
	 * @param province   省份代码
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryCity(@Param(value = "province") String province, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询是否已存在此区域代码
	 *
	 * @param regionCode 区域代码
	 * @param tenantCode 租户编码
	 * @return
	 */
	Integer getSysRegionByRegionCode(@Param(value = "regionCode") String regionCode, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 新增区域
	 * 
	 * @param sysRegion 区域对象
	 * @return
	 */
	int insertSysRegion(SysRegion sysRegion);

	/**
	 * 编辑区域
	 * 
	 * @param sysRegion 区域对象
	 * @return
	 */
	int updateSysRegion(SysRegion sysRegion);

	/**
	 * 删除区域
	 * 
	 * @param regionCode 区域代码
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteSysRegion(@Param(value = "array") String[] regionCode, @Param(value = "tenantCode") String tenantCode);

}
