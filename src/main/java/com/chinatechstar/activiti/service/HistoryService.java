package com.chinatechstar.activiti.service;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 流程任务历史信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface HistoryService {

	/**
	 * 查询流程实例历史数据分页
	 * 
	 * @param currentPage              当前页数
	 * @param pageSize                 每页记录数
	 * @param processInstanceName      流程实例名称
	 * @param processDefinitionKey     流程定义编号
	 * @param processDefinitionName    流程定义名称
	 * @param processDefinitionVersion 流程定义版本
	 * @param startedAfterDate         发起时间
	 * @param finishedBeforeDate       结束时间
	 * @param username                 用户名
	 * @return
	 */
	Map<String, Object> queryHistoricProcessInstance(Integer currentPage, Integer pageSize, String processInstanceName, String processDefinitionKey,
			String processDefinitionName, Integer processDefinitionVersion, String startedAfterDate, String finishedBeforeDate, String username);

	/**
	 * 查询节点执行历史数据分页
	 * 
	 * @param currentPage       当前页数
	 * @param pageSize          每页记录数
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	LinkedHashMap<String, Object> queryHistoricActivityInstance(Integer currentPage, Integer pageSize, String processInstanceId);

	/**
	 * 查询流程实例和节点执行的详细信息历史数据分页
	 * 
	 * @param currentPage       当前页数
	 * @param pageSize          每页记录数
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	LinkedHashMap<String, Object> queryHistoricDetail(Integer currentPage, Integer pageSize, String processInstanceId);

	/**
	 * 查询流程实例已结束的变量历史数据分页
	 * 
	 * @param currentPage       当前页数
	 * @param pageSize          每页记录数
	 * @param processInstanceId 流程实例ID
	 * @return
	 * @throws ParseException
	 */
	Map<String, Object> queryHistoricVariableInstance(Integer currentPage, Integer pageSize, String processInstanceId) throws ParseException;

	/**
	 * 查询流程任务历史数据分页
	 * 
	 * @param currentPage           当前页数
	 * @param pageSize              每页记录数
	 * @param processDefinitionName 流程定义名称
	 * @param taskName              任务名称
	 * @param startTime             开始时间
	 * @param endTime               结束时间
	 * @return
	 */
	Map<String, Object> queryHistoricTaskInstance(Integer currentPage, Integer pageSize, String processDefinitionName, String taskName, String startTime,
			String endTime);

	/**
	 * 删除流程实例历史数据
	 * 
	 * @param processInstanceId 流程实例ID
	 */
	void deleteHistoricProcessInstance(String processInstanceId);

	/**
	 * 删除流程任务历史数据
	 * 
	 * @param id 流程任务历史ID
	 */
	void deleteHistoricTaskInstance(String[] id);

}
