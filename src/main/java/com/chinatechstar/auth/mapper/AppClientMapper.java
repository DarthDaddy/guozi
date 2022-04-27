package com.chinatechstar.auth.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.chinatechstar.auth.entity.AppClient;

/**
 * 应用信息的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface AppClientMapper {

	/**
	 * 查询应用分页或导出数据
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryAppClient(Map<String, Object> paramMap);

	/**
	 * 查询是否已存在此应用编码
	 *
	 * @param id         应用ID
	 * @param clientCode 应用编码
	 * @param tenantCode 租户编码
	 * @return
	 */
	Integer getAppClientByClientCode(@Param(value = "id") Long id, @Param(value = "clientCode") String clientCode, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 新增应用
	 * 
	 * @param appClient 应用对象
	 * @return
	 */
	int insertAppClient(AppClient appClient);

	/**
	 * 编辑应用
	 * 
	 * @param appClient 应用对象
	 * @return
	 */
	int updateAppClient(AppClient appClient);

	/**
	 * 删除应用
	 *
	 * @param id         应用ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteAppClient(@Param(value = "array") Long[] id, @Param(value = "tenantCode") String tenantCode);

}
