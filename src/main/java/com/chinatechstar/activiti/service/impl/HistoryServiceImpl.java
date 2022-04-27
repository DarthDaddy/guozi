package com.chinatechstar.activiti.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.activiti.core.common.spring.security.policies.ActivitiForbiddenException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricDetailQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.admin.controller.SysRoleController;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import com.chinatechstar.component.commons.utils.CollectionUtils;

/**
 * 流程任务历史信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class HistoryServiceImpl implements com.chinatechstar.activiti.service.HistoryService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String PROCESSINSTANCEID = "processInstanceId";

	@Autowired
	private HistoryService historyService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private org.activiti.engine.TaskService taskService;

	@Autowired
	private SysRoleController sysRoleServiceClient;

	/**
	 * 查询流程实例历史数据分页
	 */
	@SuppressWarnings("all")
	@Override
	public Map<String, Object> queryHistoricProcessInstance(Integer currentPage, Integer pageSize, String processInstanceName, String processDefinitionKey,
			String processDefinitionName, Integer processDefinitionVersion, String startedAfterDate, String finishedBeforeDate, String username) {
		HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
		if (StringUtils.isNotBlank(processInstanceName)) {
			historicProcessInstanceQuery.processInstanceNameLike("%" + processInstanceName + "%");
		}
		if (StringUtils.isNotBlank(processDefinitionKey)) {
			historicProcessInstanceQuery.processDefinitionKey(processDefinitionKey);
		}
		if (StringUtils.isNotBlank(processDefinitionName)) {
			historicProcessInstanceQuery.processDefinitionName(processDefinitionName);
		}
		if (processDefinitionVersion != null) {
			historicProcessInstanceQuery.processDefinitionVersion(processDefinitionVersion);
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (StringUtils.isNotBlank(startedAfterDate)) {
				historicProcessInstanceQuery.startedAfter(sdf.parse(startedAfterDate));
			}
			if (StringUtils.isNotBlank(finishedBeforeDate)) {
				historicProcessInstanceQuery.finishedBefore(sdf.parse(finishedBeforeDate));
			}
		} catch (ParseException e) {
			logger.warn(e.toString());
		}

		if (StringUtils.isNotBlank(username)) {
			historicProcessInstanceQuery.startedBy(username);
		}
		List<HistoricProcessInstance> historicProcessInstanceList = historicProcessInstanceQuery.orderByProcessInstanceEndTime().asc()
				.listPage(pageSize * (currentPage - 1), pageSize);
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		for (int i = 0; i < historicProcessInstanceList.size(); i++) {
			LinkedHashMap<String, Object> historicProcessInstanceMap = new LinkedHashMap<>();
			historicProcessInstanceMap.put("id", historicProcessInstanceList.get(i).getId());
			historicProcessInstanceMap.put(PROCESSINSTANCEID, historicProcessInstanceList.get(i).getId());
			historicProcessInstanceMap.put("businessKey", historicProcessInstanceList.get(i).getBusinessKey());
			historicProcessInstanceMap.put("deleteReason", historicProcessInstanceList.get(i).getDeleteReason());
			historicProcessInstanceMap.put("deploymentId", historicProcessInstanceList.get(i).getDeploymentId());
			historicProcessInstanceMap.put("description", historicProcessInstanceList.get(i).getDescription());
			historicProcessInstanceMap.put("durationInMillis", historicProcessInstanceList.get(i).getDurationInMillis());
			historicProcessInstanceMap.put("endActivityId", historicProcessInstanceList.get(i).getEndActivityId());
			historicProcessInstanceMap.put("endTime", historicProcessInstanceList.get(i).getEndTime());
			historicProcessInstanceMap.put("name", historicProcessInstanceList.get(i).getName());
			historicProcessInstanceMap.put("processDefinitionId", historicProcessInstanceList.get(i).getProcessDefinitionId());
			historicProcessInstanceMap.put("processDefinitionKey", historicProcessInstanceList.get(i).getProcessDefinitionKey());
			historicProcessInstanceMap.put("processDefinitionName", historicProcessInstanceList.get(i).getProcessDefinitionName());
			historicProcessInstanceMap.put("processDefinitionVersion", historicProcessInstanceList.get(i).getProcessDefinitionVersion());
			historicProcessInstanceMap.put("processVariables", historicProcessInstanceList.get(i).getProcessVariables());
			historicProcessInstanceMap.put("startActivityId", historicProcessInstanceList.get(i).getStartActivityId());
			historicProcessInstanceMap.put("startTime", historicProcessInstanceList.get(i).getStartTime());
			historicProcessInstanceMap.put("startUserId", historicProcessInstanceList.get(i).getStartUserId());
			historicProcessInstanceMap.put("superProcessInstanceId", historicProcessInstanceList.get(i).getSuperProcessInstanceId());
			historicProcessInstanceMap.put("tenantId", historicProcessInstanceList.get(i).getTenantId());
			List<HistoricVariableInstance> historicVariableInstanceList = historyService.createHistoricVariableInstanceQuery()
					.processInstanceId(historicProcessInstanceList.get(i).getId()).list();
			boolean approved = false;
			for (HistoricVariableInstance historicVariableInstance : historicVariableInstanceList) {
				if ("approved".equals(historicVariableInstance.getVariableName())) {
					approved = (boolean) historicVariableInstance.getValue();
				}
			}
			String processStatus = null;
			if (historicProcessInstanceList.get(i).getDeleteReason() != null && historicProcessInstanceList.get(i).getEndTime() != null) {
				processStatus = "已终止";
			} else if (approved == true && historicProcessInstanceList.get(i).getDeleteReason() == null
					&& historicProcessInstanceList.get(i).getEndTime() != null) {
				processStatus = "审批通过";
			} else if (approved == false && historicProcessInstanceList.get(i).getDeleteReason() == null
					&& historicProcessInstanceList.get(i).getEndTime() != null) {
				processStatus = "已驳回";
			} else if (historicProcessInstanceList.get(i).getEndTime() == null) {
				processStatus = "审批中";
			}
			historicProcessInstanceMap.put("processStatus", processStatus);
			List<Comment> commentList = taskService.getProcessInstanceComments(historicProcessInstanceList.get(i).getId());// 查询该流程实例ID的所有批注 NOSONAR
			List<String> comments = new ArrayList<>();
			for (int j = 0; j < commentList.size(); j++) {
				comments.add(commentList.get(j).getUserId() + "：" + commentList.get(j).getFullMessage() + "\n");
			}
			historicProcessInstanceMap.put("comments", comments);
			resultList.add(historicProcessInstanceMap);
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String roleData = sysRoleServiceClient.queryRoleData("historicprocessinstance", authentication.getName());
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), historicProcessInstanceQuery.count(),
					currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, historicProcessInstanceQuery.count(), currentPage, pageSize);
		}
	}

	/**
	 * 查询节点执行历史数据分页
	 */
	@Override
	public LinkedHashMap<String, Object> queryHistoricActivityInstance(Integer currentPage, Integer pageSize, String processInstanceId) {
		HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
		if (StringUtils.isNotBlank(processInstanceId)) {
			historicActivityInstanceQuery.processInstanceId(processInstanceId);
		}
		List<HistoricActivityInstance> historicActivityInstanceList = historicActivityInstanceQuery.listPage(pageSize * (currentPage - 1), pageSize);
		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put("list", historicActivityInstanceList);
		LinkedHashMap<String, Long> paginationMap = new LinkedHashMap<>();
		paginationMap.put("total", historicActivityInstanceQuery.count());
		paginationMap.put("pageSize", (long) pageSize);
		paginationMap.put("current", (long) currentPage);
		resultMap.put("pagination", paginationMap);
		return resultMap;
	}

	/**
	 * 查询流程实例和节点执行的详细信息历史数据分页
	 */
	@Override
	public LinkedHashMap<String, Object> queryHistoricDetail(Integer currentPage, Integer pageSize, String processInstanceId) {
		HistoricDetailQuery historicDetailQuery = historyService.createHistoricDetailQuery();
		if (StringUtils.isNotBlank(processInstanceId)) {
			historicDetailQuery.processInstanceId(processInstanceId);
		}
		List<HistoricDetail> historicDetailList = historicDetailQuery.orderByTime().desc().listPage(pageSize * (currentPage - 1), pageSize);
		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put("list", historicDetailList);
		LinkedHashMap<String, Long> paginationMap = new LinkedHashMap<>();
		paginationMap.put("total", historicDetailQuery.count());
		paginationMap.put("pageSize", (long) pageSize);
		paginationMap.put("current", (long) currentPage);
		resultMap.put("pagination", paginationMap);
		return resultMap;
	}

	/**
	 * 查询流程实例已结束的变量历史数据分页
	 */
	@Override
	public Map<String, Object> queryHistoricVariableInstance(Integer currentPage, Integer pageSize, String processInstanceId) throws ParseException {
		HistoricVariableInstanceQuery historicVariableInstanceQuery = historyService.createHistoricVariableInstanceQuery();
		if (StringUtils.isNotBlank(processInstanceId)) {
			historicVariableInstanceQuery.processInstanceId(processInstanceId);
		}
		List<HistoricVariableInstance> historicVariableInstanceList = historicVariableInstanceQuery.listPage(pageSize * (currentPage - 1), pageSize);
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		for (int i = 0; i < historicVariableInstanceList.size(); i++) {
			LinkedHashMap<String, Object> variableInstanceMap = new LinkedHashMap<>();
			variableInstanceMap.put("id", historicVariableInstanceList.get(i).getId());
			variableInstanceMap.put("variableName", historicVariableInstanceList.get(i).getVariableName());
			variableInstanceMap.put("variableTypeName", historicVariableInstanceList.get(i).getVariableTypeName());
			if ("date".equals(historicVariableInstanceList.get(i).getVariableTypeName())) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
				Date date = simpleDateFormat.parse(historicVariableInstanceList.get(i).getValue().toString());
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				variableInstanceMap.put("value", simpleDateFormat.format(date));
			} else {
				variableInstanceMap.put("value", historicVariableInstanceList.get(i).getValue());
			}
			variableInstanceMap.put(PROCESSINSTANCEID, historicVariableInstanceList.get(i).getProcessInstanceId());
			variableInstanceMap.put("createTime", historicVariableInstanceList.get(i).getCreateTime());
			variableInstanceMap.put("lastUpdatedTime", historicVariableInstanceList.get(i).getLastUpdatedTime());
			variableInstanceMap.put("taskId", historicVariableInstanceList.get(i).getTaskId());
			variableInstanceMap.put("time", historicVariableInstanceList.get(i).getTime());
			resultList.add(variableInstanceMap);
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String roleData = sysRoleServiceClient.queryRoleData("historicvariableinstance", authentication.getName());
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), historicVariableInstanceQuery.count(),
					currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, historicVariableInstanceQuery.count(), currentPage, pageSize);
		}
	}

	/**
	 * 查询流程任务历史数据分页
	 */
	@Override
	public Map<String, Object> queryHistoricTaskInstance(Integer currentPage, Integer pageSize, String processDefinitionName, String taskName, String startTime,
			String endTime) {
		HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
		if (StringUtils.isNotBlank(processDefinitionName)) {
			historicTaskInstanceQuery.processDefinitionNameLike("%" + processDefinitionName + "%");
		}
		if (StringUtils.isNotBlank(taskName)) {
			historicTaskInstanceQuery.taskNameLike("%" + taskName + "%");
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (StringUtils.isNotBlank(startTime)) {
				historicTaskInstanceQuery.taskCompletedAfter(sdf.parse(startTime));
			}
			if (StringUtils.isNotBlank(endTime)) {
				historicTaskInstanceQuery.taskCompletedBefore(sdf.parse(endTime));
			}
		} catch (ParseException e) {
			logger.warn(e.toString());
		}

		List<HistoricTaskInstance> historicTaskInstanceList = historicTaskInstanceQuery.orderByHistoricTaskInstanceEndTime().desc()
				.listPage(pageSize * (currentPage - 1), pageSize);
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		for (int i = 0; i < historicTaskInstanceList.size(); i++) {
			LinkedHashMap<String, Object> taskInstanceMap = new LinkedHashMap<>();
			taskInstanceMap.put("id", historicTaskInstanceList.get(i).getId());
			taskInstanceMap.put("assignee", historicTaskInstanceList.get(i).getAssignee());
			taskInstanceMap.put("category", historicTaskInstanceList.get(i).getCategory());
			taskInstanceMap.put("claimTime", historicTaskInstanceList.get(i).getClaimTime());
			taskInstanceMap.put("createTime", historicTaskInstanceList.get(i).getCreateTime());
			taskInstanceMap.put("deleteReason", historicTaskInstanceList.get(i).getDeleteReason());
			taskInstanceMap.put("description", historicTaskInstanceList.get(i).getDescription());
			taskInstanceMap.put("dueDate", historicTaskInstanceList.get(i).getDueDate());
			taskInstanceMap.put("durationInMillis", historicTaskInstanceList.get(i).getDurationInMillis());
			taskInstanceMap.put("endTime", historicTaskInstanceList.get(i).getEndTime());
			taskInstanceMap.put("executionId", historicTaskInstanceList.get(i).getExecutionId());
			taskInstanceMap.put("formKey", historicTaskInstanceList.get(i).getFormKey());
			taskInstanceMap.put("name", historicTaskInstanceList.get(i).getName());
			taskInstanceMap.put("owner", historicTaskInstanceList.get(i).getOwner());
			taskInstanceMap.put("parentTaskId", historicTaskInstanceList.get(i).getParentTaskId());
			taskInstanceMap.put("priority", historicTaskInstanceList.get(i).getPriority());
			taskInstanceMap.put("processDefinitionId", historicTaskInstanceList.get(i).getProcessDefinitionId());
			taskInstanceMap.put(PROCESSINSTANCEID, historicTaskInstanceList.get(i).getProcessInstanceId());
			taskInstanceMap.put("processVariables", historicTaskInstanceList.get(i).getProcessVariables());
			taskInstanceMap.put("startTime", historicTaskInstanceList.get(i).getStartTime());
			taskInstanceMap.put("taskDefinitionKey", historicTaskInstanceList.get(i).getTaskDefinitionKey());
			taskInstanceMap.put("taskLocalVariables", historicTaskInstanceList.get(i).getTaskLocalVariables());
			taskInstanceMap.put("tenantId", historicTaskInstanceList.get(i).getTenantId());
			taskInstanceMap.put("time", historicTaskInstanceList.get(i).getTime());
			taskInstanceMap.put("workTimeInMillis", historicTaskInstanceList.get(i).getWorkTimeInMillis());
			taskInstanceMap.put("status", historicTaskInstanceList.get(i).getEndTime() == null ? "办理中" : "已结束");
			ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService
					.getProcessDefinition(historicTaskInstanceList.get(i).getProcessDefinitionId());
			taskInstanceMap.put("processDefinitionName", processDefinition.getName());
			List<Comment> commentList = taskService.getProcessInstanceComments(historicTaskInstanceList.get(i).getProcessInstanceId());// 查询该流程实例ID的所有批注 NOSONAR
			List<String> comments = new ArrayList<>();
			for (int j = 0; j < commentList.size(); j++) {
				comments.add(commentList.get(j).getUserId() + "：" + commentList.get(j).getFullMessage() + "\n");
			}
			taskInstanceMap.put("comments", comments);
			resultList.add(taskInstanceMap);
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String roleData = sysRoleServiceClient.queryRoleData("historictaskinstance", authentication.getName());
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), historicTaskInstanceQuery.count(), currentPage,
					pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, historicTaskInstanceQuery.count(), currentPage, pageSize);
		}
	}

	/**
	 * 删除流程实例历史数据
	 */
	@Override
	public void deleteHistoricProcessInstance(String processInstanceId) {
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		if (task != null) {
			throw new ActivitiForbiddenException("包含流程实例中的任务，不能删除");
		}
		historyService.deleteHistoricProcessInstance(processInstanceId);
	}

	/**
	 * 删除流程任务历史数据
	 */
	@Override
	public void deleteHistoricTaskInstance(String[] id) {
		for (int i = 0; i < id.length; i++) {
			historyService.deleteHistoricTaskInstance(id[i]);
		}
	}

}
