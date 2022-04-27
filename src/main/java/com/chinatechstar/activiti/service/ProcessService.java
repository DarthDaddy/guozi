package com.chinatechstar.activiti.service;

import java.io.InputStream;
import java.util.Map;

import com.chinatechstar.activiti.entity.Leave;

/**
 * 流程信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface ProcessService {

	/**
	 * 查询流程定义分页
	 * 
	 * @param currentPage               当前页数
	 * @param pageSize                  每页记录数
	 * @param processDefinitionKey      流程定义编号
	 * @param processDefinitionName     流程定义名称
	 * @param processDefinitionCategory 流程定义类别
	 * @param processDefinitionVersion  流程定义版本
	 * @param suspended                 是否挂起
	 * @return
	 */
	Map<String, Object> queryProcessDefinition(Integer currentPage, Integer pageSize, String processDefinitionKey, String processDefinitionName,
			String processDefinitionCategory, Integer processDefinitionVersion, Short suspended);

	/**
	 * 查询流程实例分页
	 * 
	 * @param currentPage              当前页数
	 * @param pageSize                 每页记录数
	 * @param processDefinitionKey     流程定义编号
	 * @param processDefinitionName    流程定义名称
	 * @param processDefinitionVersion 流程定义版本
	 * @param processInstanceName      流程实例名称
	 * @param suspended                是否挂起
	 * @param startUserId              发起者
	 * @return
	 */
	Map<String, Object> queryProcessInstance(Integer currentPage, Integer pageSize, String processDefinitionKey, String processDefinitionName,
			Integer processDefinitionVersion, String processInstanceName, Short suspended, String startUserId);

	/**
	 * 查询流程实例执行路径分页
	 * 
	 * @param currentPage       当前页数
	 * @param pageSize          每页记录数
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	Map<String, Object> queryExecution(Integer currentPage, Integer pageSize, String processInstanceId);

	/**
	 * 获取流程定义XML
	 * 
	 * @param deploymentId 部署ID
	 * @return
	 */
	String getProcessResource(String deploymentId);

	/**
	 * 获取高亮跟踪流程图
	 * 
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	String getHighLightedProcessDiagram(String processInstanceId);

	/**
	 * 获取流程定义图片
	 * 
	 * @param deploymentId 部署ID
	 * @return
	 */
	InputStream getProcessImage(String deploymentId);

	/**
	 * 持久化流程
	 * 
	 * @param modelId 模型ID
	 * @param jsonXml 流程基本信息
	 * @param svgXml  流程设计信息
	 */
	void persistProcess(String modelId, String jsonXml, String svgXml);

	/**
	 * 发起流程
	 * 
	 * @param leave       请假对象
	 * @param starterUser 发起者
	 */
	void startProcessInstance(Leave leave, String starterUser);

	/**
	 * 挂起流程定义
	 * 
	 * @param processDefinitionId 流程定义ID
	 */
	void suspendProcessDefinition(String processDefinitionId);

	/**
	 * 激活流程定义
	 * 
	 * @param processDefinitionId 流程定义ID
	 */
	void activateProcessDefinition(String processDefinitionId);

	/**
	 * 挂起流程实例
	 * 
	 * @param processInstanceId 流程实例ID
	 */
	void suspendProcessInstance(String processInstanceId);

	/**
	 * 激活流程实例
	 * 
	 * @param processInstanceId 流程实例ID
	 */
	void activateProcessInstance(String processInstanceId);

	/**
	 * 删除流程实例
	 * 
	 * @param processInstanceId 流程实例ID
	 */
	void deleteProcessInstance(String[] processInstanceId);

}
