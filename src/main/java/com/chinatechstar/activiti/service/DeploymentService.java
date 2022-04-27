package com.chinatechstar.activiti.service;

import java.util.Map;

/**
 * 部署信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface DeploymentService {

	/**
	 * 查询已部署的模型分页
	 * 
	 * @param currentPage    当前页数
	 * @param pageSize       每页记录数
	 * @param deploymentName 已部署的模型名称
	 * @param category       模型类别
	 * @return
	 */
	Map<String, Object> queryDeployment(Integer currentPage, Integer pageSize, String deploymentName, String category);

	/**
	 * 删除已部署的模型
	 * 
	 * @param deploymentId 部署ID
	 */
	void deleteDeployment(String[] deploymentId);

}
