package com.chinatechstar.activiti.service;

import java.util.Map;

/**
 * 任务信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface TaskService {

	/**
	 * 查询任务分页
	 * 
	 * @param currentPage           当前页数
	 * @param pageSize              每页记录数
	 * @param processDefinitionName 流程定义名称
	 * @param taskName              任务名称
	 * @param assignee              当前签收者
	 * @return
	 */
	Map<String, Object> queryTask(Integer currentPage, Integer pageSize, String processDefinitionName, String taskName, String assignee);

	/**
	 * 编辑任务
	 * 
	 * @param taskId    任务ID
	 * @param candidate 候选者
	 */
	void updateTask(String taskId, String candidate);

	/**
	 * 签收任务
	 * 
	 * @param taskId   任务ID
	 * @param assignee 签收者
	 */
	void claimTask(String[] taskId, String assignee);

	/**
	 * 完结任务，任务进入下一节点
	 * 
	 * @param taskId            任务ID
	 * @param processInstanceId 流程实例ID
	 * @param candidate         下一节点候选者
	 * @param comment           批注
	 * @param username          用户名
	 */
	void completeTask(String taskId, String processInstanceId, String candidate, String comment, String username);

	/**
	 * 委派任务
	 * 
	 * @param taskId            任务ID
	 * @param processInstanceId 流程实例ID
	 * @param comment           批注
	 * @param username          用户名
	 * @param assignee          签收者
	 */
	void delegateTask(String taskId, String processInstanceId, String comment, String username, String assignee);

	/**
	 * 被委派人完结任务并返回
	 * 
	 * @param taskId            任务ID
	 * @param processInstanceId 流程实例ID
	 * @param comment           批注
	 * @param username          用户名
	 */
	void resolveTask(String taskId, String processInstanceId, String comment, String username);

	/**
	 * 回退任务
	 * 
	 * @param taskId            任务ID
	 * @param processInstanceId 流程实例ID
	 * @param comment           批注
	 * @param username          用户名
	 */
	void regressTask(String taskId, String processInstanceId, String comment, String username);

	/**
	 * 驳回任务
	 * 
	 * @param taskId            任务ID
	 * @param processInstanceId 流程实例ID
	 * @param comment           批注
	 * @param username          用户名
	 */
	void rejectTask(String taskId, String processInstanceId, String comment, String username);

	/**
	 * 终止任务
	 * 
	 * @param taskId            任务ID
	 * @param processInstanceId 流程实例ID
	 * @param comment           批注
	 * @param username          用户名
	 */
	void terminateTask(String taskId, String processInstanceId, String comment, String username);

	/**
	 * 删除任务
	 * 
	 * @param taskId 任务ID
	 */
	void deleteTask(String taskId);

	/**
	 * 查询个人任务分页
	 * 
	 * @param currentPage           当前页数
	 * @param pageSize              每页记录数
	 * @param username              用户名
	 * @param processDefinitionName 流程定义名称
	 * @param taskName              任务名称
	 * @param startTime             请假开始时间
	 * @param endTime               请假结束时间
	 * @param description           请假备注
	 * @param taskStatus            任务状态
	 * @return
	 */
	Map<String, Object> queryPersonalTask(Integer currentPage, Integer pageSize, String username, String processDefinitionName, String taskName,
			String startTime, String endTime, String description, String taskStatus);

	/**
	 * 查询任务批注
	 * 
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	Map<String, Object> queryTaskComment(String processInstanceId);

}
