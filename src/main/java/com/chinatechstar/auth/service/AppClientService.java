package com.chinatechstar.auth.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinatechstar.auth.entity.AppClient;

/**
 * 应用信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface AppClientService {

	/**
	 * 查询应用分页
	 * 
	 * @param currentPage       当前页数
	 * @param pageSize          每页记录数
	 * @param clientCode        应用编码
	 * @param authType          授权类型
	 * @param clientSecret      应用密钥
	 * @param authScope         授权范围
	 * @param tokenSeconds      令牌秒数
	 * @param refreshSeconds    刷新秒数
	 * @param fallbackUrl       回调地址
	 * @param clientDescription 应用描述
	 * @param sorter            排序
	 * @return
	 */
	Map<String, Object> queryAppClient(Integer currentPage, Integer pageSize, String clientCode, String authType, String clientSecret, String authScope,
			Long tokenSeconds, Long refreshSeconds, String fallbackUrl, String clientDescription, String sorter);

	/**
	 * 查询应用的导出数据列表
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryAppClientForExcel(Map<String, Object> paramMap);

	/**
	 * 新增应用
	 * 
	 * @param appClient 应用对象
	 */
	void insertAppClient(AppClient appClient);

	/**
	 * 编辑应用
	 * 
	 * @param appClient 应用对象
	 */
	void updateAppClient(AppClient appClient);

	/**
	 * 删除应用
	 * 
	 * @param id 应用ID
	 */
	void deleteAppClient(Long[] id);

}
