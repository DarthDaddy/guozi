package com.chinatechstar.admin.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinatechstar.admin.entity.SysDict;
import org.apache.ibatis.annotations.Param;

/**
 * 字典信息的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface SysDictMapper {

	/**
	 * 查询字典分页或导出数据
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysDict(Map<String, Object> paramMap);

	/**
	 * 查询字典的树数据列表
	 * 
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysDictTree(@Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据字典类型查询下拉框数据列表
	 *
	 * @param dictType   字典类型
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryDictByDictType(@Param(value = "dictType") String dictType, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据字典类型查询多选框数据列表
	 *
	 * @param dictType   字典类型
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryDictByDictTypeCheckbox(@Param(value = "dictType") String dictType, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询是否已存在此字典类型
	 *
	 * @param dictType   字典类型
	 * @param tenantCode 租户编码
	 * @return
	 */
	Integer getSysDictByDictType(@Param(value = "dictType") String dictType, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 新增字典
	 * 
	 * @param sysDict 字典对象
	 * @return
	 */
	int insertSysDict(SysDict sysDict);

	/**
	 * 编辑字典
	 * 
	 * @param sysDict 字典对象
	 * @return
	 */
	int updateSysDict(SysDict sysDict);

	/**
	 * 根据上级字典ID查询父节点的字典类型
	 *
	 * @param parentId   上级字典ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	String getDictTypeByParentId(@Param(value = "parentId") Long parentId, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 删除字典
	 *
	 * @param id         字典ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteSysDict(@Param(value = "array") Long[] id, @Param(value = "tenantCode") String tenantCode);

}
