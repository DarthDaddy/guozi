package com.chinatechstar.activiti.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.core.common.spring.security.policies.ActivitiForbiddenException;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.activiti.engine.task.TaskInfoQueryWrapper;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.admin.controller.SysRoleController;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import com.chinatechstar.component.commons.utils.CollectionUtils;

/**
 * 任务信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class TaskServiceImpl implements com.chinatechstar.activiti.service.TaskService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String FINISHED = "finished";
	private static final String ASSIGNEE = "assignee";
	private static final String CANDIDATE = "candidate";

	@Autowired
	private TaskService taskService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private SysRoleController sysRoleServiceClient;

	/**
	 * 查询任务分页
	 */
	@Override
	public Map<String, Object> queryTask(Integer currentPage, Integer pageSize, String processDefinitionName, String taskName, String assignee) {
		TaskQuery taskQuery = taskService.createTaskQuery();
		if (StringUtils.isNotBlank(processDefinitionName)) {
			taskQuery.processDefinitionNameLike("%" + processDefinitionName + "%");
		}
		if (StringUtils.isNotBlank(taskName)) {
			taskQuery.taskNameLike("%" + taskName + "%");
		}
		if (StringUtils.isNotBlank(assignee)) {
			taskQuery.taskAssigneeLikeIgnoreCase(assignee);
		}

		List<Task> taskList = taskQuery.includeProcessVariables().includeTaskLocalVariables().orderByTaskPriority().desc()
				.listPage(pageSize * (currentPage - 1), pageSize);
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		for (int i = 0; i < taskList.size(); i++) {
			LinkedHashMap<String, Object> taskMap = new LinkedHashMap<>();
			taskMap.put("id", taskList.get(i).getId());
			taskMap.put(ASSIGNEE, taskList.get(i).getAssignee());
			taskMap.put("category", taskList.get(i).getCategory());
			taskMap.put("claimTime", taskList.get(i).getClaimTime());
			taskMap.put("createTime", taskList.get(i).getCreateTime());
			taskMap.put("delegationState", taskList.get(i).getDelegationState());
			taskMap.put("description", taskList.get(i).getDescription());
			taskMap.put("dueDate", taskList.get(i).getDueDate());
			taskMap.put("executionId", taskList.get(i).getExecutionId());
			taskMap.put("formKey", taskList.get(i).getFormKey());
			taskMap.put("suspended", taskList.get(i).isSuspended());
			taskMap.put("name", taskList.get(i).getName());
			taskMap.put("owner", taskList.get(i).getOwner());
			taskMap.put("parentTaskId", taskList.get(i).getParentTaskId());
			taskMap.put("priority", taskList.get(i).getPriority());
			taskMap.put("processDefinitionId", taskList.get(i).getProcessDefinitionId());
			taskMap.put("processInstanceId", taskList.get(i).getProcessInstanceId());
			taskMap.put("processVariables", taskList.get(i).getProcessVariables());
			taskMap.put("taskDefinitionKey", taskList.get(i).getTaskDefinitionKey());
			taskMap.put("taskLocalVariables", taskList.get(i).getTaskLocalVariables());
			taskMap.put("tenantId", taskList.get(i).getTenantId());
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(taskList.get(i).getProcessInstanceId())
					.singleResult();
			taskMap.put("processDefinitionName", processInstance.getProcessDefinitionName());
			taskMap.put("startUserId", processInstance.getStartUserId());
			List<Comment> commentList = taskService.getProcessInstanceComments(taskList.get(i).getProcessInstanceId());// 查询该流程实例ID的所有批注 NOSONAR
			List<String> comments = new ArrayList<>();
			for (int j = 0; j < commentList.size(); j++) {
				comments.add(commentList.get(j).getUserId() + "：" + commentList.get(j).getFullMessage() + "\n");
			}
			taskMap.put("comments", comments);
			resultList.add(taskMap);
		}

		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String roleData = sysRoleServiceClient.queryRoleData("task", authentication.getName());
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), taskQuery.count(), currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, taskQuery.count(), currentPage, pageSize);
		}
	}

	/**
	 * 编辑任务
	 */
	@Override
	public void updateTask(String taskId, String candidate) {
		Task task = checkTask(taskId);
		taskService.addCandidateUser(task.getId(), candidate);
		taskService.addUserIdentityLink(task.getId(), candidate, IdentityLinkType.CANDIDATE);
	}

	/**
	 * 签收任务
	 */
	@Override
	public void claimTask(String[] taskId, String assignee) {
		for (int i = 0; i < taskId.length; i++) {
			Task task = checkTask(taskId[i]);
			taskService.claim(task.getId(), assignee);
		}
	}

	/**
	 * 完结任务，任务进入下一节点
	 */
	@Override
	public void completeTask(String taskId, String processInstanceId, String candidate, String comment, String username) {
		Task task = checkTask(taskId);
		Map<String, Object> variablesMap = new HashMap<>();
		Authentication.setAuthenticatedUserId(username);
		taskService.addComment(task.getId(), processInstanceId, comment);
		variablesMap.put("approved", true);
		taskService.complete(task.getId(), variablesMap);
		Task nextTask = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		if (nextTask != null) {
			if (StringUtils.isBlank(candidate)) {
				throw new ActivitiIllegalArgumentException("流程有待审批，请正确选择审批人员");
			}
			taskService.addCandidateUser(nextTask.getId(), candidate);
			taskService.addUserIdentityLink(nextTask.getId(), candidate, IdentityLinkType.CANDIDATE);
		} else {
			if (StringUtils.isNotBlank(candidate)) {
				throw new ActivitiIllegalArgumentException("您是此流程最后审批人员，请选择结束");
			}
		}
	}

	/**
	 * 委派任务
	 */
	@Override
	public void delegateTask(String taskId, String processInstanceId, String comment, String username, String assignee) {
		if (StringUtils.isBlank(assignee)) {
			throw new ActivitiIllegalArgumentException("审批人员不能为空");
		}
		Task task = checkTask(taskId);
		Authentication.setAuthenticatedUserId(username);
		taskService.addComment(task.getId(), processInstanceId, comment);
		taskService.delegateTask(task.getId(), assignee);
	}

	/**
	 * 被委派人完结任务并返回
	 */
	@Override
	public void resolveTask(String taskId, String processInstanceId, String comment, String username) {
		Task task = checkTask(taskId);
		Authentication.setAuthenticatedUserId(username);
		taskService.addComment(task.getId(), processInstanceId, comment);
		taskService.resolveTask(task.getId());
	}

	/**
	 * 回退任务
	 */
	@Override
	public void regressTask(String taskId, String processInstanceId, String comment, String username) {
		Task task = checkTask(taskId);
		Authentication.setAuthenticatedUserId(username);
		taskService.addComment(task.getId(), processInstanceId, comment);
		List<HistoricActivityInstance> historicActivityInstance = historyService.createHistoricActivityInstanceQuery().activityType("userTask")
				.processInstanceId(processInstanceId).finished().list();
		Map<String, Date> map = new LinkedHashMap<>();
		for (int i = 0; i < historicActivityInstance.size(); i++) {
			map.put(historicActivityInstance.get(i).getAssignee(), historicActivityInstance.get(i).getEndTime());
		}
		if (map.size() > 0) {
			map.entrySet().stream().sorted(Map.Entry.<String, Date>comparingByValue()).forEachOrdered(x -> map.put(x.getKey(), x.getValue()));
			String assignee = map.entrySet().iterator().next().getKey();
			taskService.setAssignee(task.getId(), assignee);
		} else {
			throw new ActivitiIllegalArgumentException("没有可回退的用户任务");
		}
	}

	/**
	 * 驳回任务
	 */
	@Override
	public void rejectTask(String taskId, String processInstanceId, String comment, String username) {
		Task task = checkTask(taskId);
		Authentication.setAuthenticatedUserId(username);
		taskService.addComment(task.getId(), processInstanceId, comment);
		while (true) {
			Task nextTask = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
			if (nextTask == null) {
				break;
			}
			Map<String, Object> variablesMap = new HashMap<>();
			taskService.setVariable(nextTask.getId(), "approved", false);
			taskService.complete(nextTask.getId(), variablesMap);
		}
	}

	/**
	 * 终止任务
	 */
	@Override
	public void terminateTask(String taskId, String processInstanceId, String comment, String username) {
		Task task = checkTask(taskId);
		Authentication.setAuthenticatedUserId(username);
		taskService.addComment(task.getId(), processInstanceId, comment);
		runtimeService.deleteProcessInstance(processInstanceId, comment);
	}

	/**
	 * 删除任务
	 */
	@Override
	public void deleteTask(String taskId) {
		Task task = checkTask(taskId);
		if (task.getExecutionId() != null) {
			throw new ActivitiForbiddenException("包含流程实例中的任务，不能删除");
		}
		taskService.deleteTask(task.getId(), "ID为" + task.getId() + "的任务已删除", true);
	}

	/**
	 * 校验任务是否存在和是否挂起
	 */
	private Task checkTask(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task == null) {
			throw new ActivitiObjectNotFoundException("找不到ID为'" + taskId + "'的任务", Task.class);
		}
		if (task.isSuspended()) {
			throw new ActivitiForbiddenException("流程实例已挂起，不能操作");
		}
		return task;
	}

	/**
	 * 查询个人任务分页
	 */
	@Override
	public Map<String, Object> queryPersonalTask(Integer currentPage, Integer pageSize, String username, String processDefinitionName, String taskName,
			String startTime, String endTime, String description, String taskStatus) {
		TaskInfoQueryWrapper taskInfoQueryWrapper = null;
		if (FINISHED.equals(taskStatus)) {// 已办
			HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
			historicTaskInstanceQuery.taskAssignee(username).finished();
			taskInfoQueryWrapper = new TaskInfoQueryWrapper(historicTaskInstanceQuery);
		} else if (ASSIGNEE.equals(taskStatus)) {// 待办
			taskInfoQueryWrapper = new TaskInfoQueryWrapper(taskService.createTaskQuery().taskAssignee(username));
		} else if (CANDIDATE.equals(taskStatus)) {// 待签收
			taskInfoQueryWrapper = new TaskInfoQueryWrapper(taskService.createTaskQuery().taskCandidateUser(username));
		}
		if (StringUtils.isNotBlank(processDefinitionName)) {
			taskInfoQueryWrapper.getTaskInfoQuery().processDefinitionNameLike("%" + processDefinitionName + "%");
		}
		if (StringUtils.isNotBlank(taskName)) {
			taskInfoQueryWrapper.getTaskInfoQuery().taskNameLike("%" + taskName + "%");
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (StringUtils.isNotBlank(startTime)) {
				taskInfoQueryWrapper.getTaskInfoQuery().taskVariableValueGreaterThanOrEqual("startTime", sdf.parse(startTime));
			}
			if (StringUtils.isNotBlank(endTime)) {
				taskInfoQueryWrapper.getTaskInfoQuery().taskVariableValueLessThanOrEqual("endTime", sdf.parse(endTime));
			}
		} catch (ParseException e) {
			logger.warn(e.toString());
		}

		if (StringUtils.isNotBlank(description)) {
			taskInfoQueryWrapper.getTaskInfoQuery().taskVariableValueLike("description", "%" + description + "%");
		}

		List<? extends TaskInfo> taskInfoList = taskInfoQueryWrapper.getTaskInfoQuery().includeProcessVariables().includeTaskLocalVariables()
				.orderByTaskCreateTime().desc().listPage(pageSize * (currentPage - 1), pageSize);
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		for (int i = 0; i < taskInfoList.size(); i++) {
			LinkedHashMap<String, Object> taskMap = new LinkedHashMap<>();
			taskMap.put("id", taskInfoList.get(i).getId());
			taskMap.put(ASSIGNEE, taskInfoList.get(i).getAssignee());
			taskMap.put("category", taskInfoList.get(i).getCategory());
			taskMap.put("claimTime", taskInfoList.get(i).getClaimTime());
			taskMap.put("createTime", taskInfoList.get(i).getCreateTime());
			taskMap.put("description", taskInfoList.get(i).getDescription());
			taskMap.put("dueDate", taskInfoList.get(i).getDueDate());
			taskMap.put("executionId", taskInfoList.get(i).getExecutionId());
			taskMap.put("formKey", taskInfoList.get(i).getFormKey());
			taskMap.put("name", taskInfoList.get(i).getName());
			taskMap.put("owner", taskInfoList.get(i).getOwner());
			taskMap.put("parentTaskId", taskInfoList.get(i).getParentTaskId());
			taskMap.put("priority", taskInfoList.get(i).getPriority());
			taskMap.put("processDefinitionId", taskInfoList.get(i).getProcessDefinitionId());
			taskMap.put("processInstanceId", taskInfoList.get(i).getProcessInstanceId());
			taskMap.put("processVariables", taskInfoList.get(i).getProcessVariables());
			taskMap.put("taskDefinitionKey", taskInfoList.get(i).getTaskDefinitionKey());
			taskMap.put("taskLocalVariables", taskInfoList.get(i).getTaskLocalVariables());

			Map map = new HashMap<>();
			Set<Map.Entry<String, Object>> entrySet = taskInfoList.get(i).getTaskLocalVariables().entrySet();
			Iterator iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				if (entry.getKey().toString().indexOf("动态属性") != -1) {
					map.put(entry.getKey(), entry.getValue());
				}
			}
			taskMap.put("dynamicProperty", map.size() > 0 ? map.toString().replace("{", "").replace("}", "") : null);

			taskMap.put("tenantId", taskInfoList.get(i).getTenantId());
			ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService
					.getProcessDefinition(taskInfoList.get(i).getProcessDefinitionId());
			taskMap.put("processDefinitionName", processDefinition.getName());
			List<Comment> commentList = taskService.getProcessInstanceComments(taskInfoList.get(i).getProcessInstanceId());// 查询该流程实例ID的所有批注 NOSONAR
			List<String> comments = new ArrayList<>();
			for (int j = 0; j < commentList.size(); j++) {
				comments.add(commentList.get(j).getUserId() + "：" + commentList.get(j).getFullMessage() + "\n");
			}
			taskMap.put("comments", comments);
			Task delegationTask = taskService.createTaskQuery().taskId(taskInfoList.get(i).getId()).singleResult();
			if (delegationTask != null) {
				taskMap.put("delegationState", delegationTask.getDelegationState());
			}
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
					.processInstanceId(taskInfoList.get(i).getProcessInstanceId()).singleResult();
			taskMap.put("processInstanceName", historicProcessInstance.getName());
			resultList.add(taskMap);
		}

		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String roleData = sysRoleServiceClient.queryRoleData("candidatetask", authentication.getName());
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), taskInfoQueryWrapper.getTaskInfoQuery().count(),
					currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, taskInfoQueryWrapper.getTaskInfoQuery().count(), currentPage, pageSize);
		}
	}

	/**
	 * 查询任务批注
	 */
	@Override
	public Map<String, Object> queryTaskComment(String processInstanceId) {
		Map<String, Object> map = new HashMap<>();
		List<Comment> commentList = taskService.getProcessInstanceComments(processInstanceId);
		map.put("list", commentList);
		return map;
	}

}
