package com.chinatechstar.activiti.service.impl;

import static java.util.Collections.emptyList;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinatechstar.activiti.config.SecurityUtil;
import com.chinatechstar.activiti.entity.Leave;
import com.chinatechstar.activiti.provider.ProcessDiagramGeneratorWrapper;
import com.chinatechstar.activiti.service.ProcessService;
import com.chinatechstar.admin.controller.SysRoleController;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import com.chinatechstar.component.commons.utils.CollectionUtils;

/**
 * 流程信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class ProcessServiceImpl implements ProcessService {

	private static final String DESCRIPTION = "description";
	private static final String SUSPENDED = "suspended";
	private static final String TENANTID = "tenantId";

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private com.chinatechstar.activiti.service.TaskService mscodeTaskService;

	@Autowired
	private org.activiti.engine.TaskService taskService;

	@Autowired
	protected ProcessEngineConfiguration processEngineConfiguration;

	@Autowired
	private SecurityUtil securityUtil;

	@Autowired
	private ProcessRuntime processRuntime;

	@Autowired
	private ProcessDiagramGeneratorWrapper processDiagramGeneratorWrapper;

	@Autowired
	private SysRoleController sysRoleServiceClient;

	/**
	 * 查询流程定义分页
	 */
	@Override
	public Map<String, Object> queryProcessDefinition(Integer currentPage, Integer pageSize, String processDefinitionKey, String processDefinitionName,
			String processDefinitionCategory, Integer processDefinitionVersion, Short suspended) {
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		if (StringUtils.isNotBlank(processDefinitionKey)) {
			processDefinitionQuery.processDefinitionKeyLike("%" + processDefinitionKey + "%");
		}
		if (StringUtils.isNotBlank(processDefinitionName)) {
			processDefinitionQuery.processDefinitionNameLike("%" + processDefinitionName + "%");
		}
		if (StringUtils.isNotBlank(processDefinitionCategory)) {
			processDefinitionQuery.processDefinitionCategoryLike("%" + processDefinitionCategory + "%");
		}
		if (processDefinitionVersion != null) {
			processDefinitionQuery.processDefinitionVersion(processDefinitionVersion);
		}
		if (suspended != null && suspended == 1) {
			processDefinitionQuery.suspended();
		}
		List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(pageSize * (currentPage - 1), pageSize);
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		for (int i = 0; i < processDefinitionList.size(); i++) {
			LinkedHashMap<String, Object> processDefinitionMap = new LinkedHashMap<>();
			processDefinitionMap.put("id", processDefinitionList.get(i).getId());
			processDefinitionMap.put("category", processDefinitionList.get(i).getCategory());
			processDefinitionMap.put(DESCRIPTION, processDefinitionList.get(i).getDescription());
			processDefinitionMap.put("diagramResourceName", processDefinitionList.get(i).getDiagramResourceName());
			processDefinitionMap.put("deploymentId", processDefinitionList.get(i).getDeploymentId());
			processDefinitionMap.put("engineVersion", processDefinitionList.get(i).getEngineVersion());
			processDefinitionMap.put("key", processDefinitionList.get(i).getKey());
			processDefinitionMap.put("name", processDefinitionList.get(i).getName());
			processDefinitionMap.put("resourceName", processDefinitionList.get(i).getResourceName());
			processDefinitionMap.put(SUSPENDED, processDefinitionList.get(i).isSuspended());
			processDefinitionMap.put(TENANTID, processDefinitionList.get(i).getTenantId());
			processDefinitionMap.put("version", processDefinitionList.get(i).getVersion());
			resultList.add(processDefinitionMap);
		}

		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String roleData = sysRoleServiceClient.queryRoleData("processdefinition", authentication.getName());
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), processDefinitionQuery.count(), currentPage,
					pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, processDefinitionQuery.count(), currentPage, pageSize);
		}
	}

	/**
	 * 查询流程实例分页
	 */
	@Override
	public Map<String, Object> queryProcessInstance(Integer currentPage, Integer pageSize, String processDefinitionKey, String processDefinitionName,
			Integer processDefinitionVersion, String processInstanceName, Short suspended, String startUserId) {
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		if (StringUtils.isNotBlank(processDefinitionKey)) {
			processInstanceQuery.processDefinitionKey(processDefinitionKey);
		}
		if (StringUtils.isNotBlank(processDefinitionName)) {
			processInstanceQuery.processDefinitionName(processDefinitionName);
		}
		if (processDefinitionVersion != null) {
			processInstanceQuery.processDefinitionVersion(processDefinitionVersion);
		}
		if (StringUtils.isNotBlank(processInstanceName)) {
			processInstanceQuery.processInstanceNameLike("%" + processInstanceName + "%");
		}
		if (suspended != null && suspended == 1) {
			processInstanceQuery.suspended();
		}
		if (StringUtils.isNotBlank(startUserId)) {
			processInstanceQuery.startedBy(startUserId);
		}
		List<ProcessInstance> processInstanceList = processInstanceQuery.includeProcessVariables().listPage(pageSize * (currentPage - 1), pageSize);
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		for (int i = 0; i < processInstanceList.size(); i++) {
			LinkedHashMap<String, Object> processInstanceMap = new LinkedHashMap<>();
			processInstanceMap.put("id", processInstanceList.get(i).getId());
			processInstanceMap.put("activityId", processInstanceList.get(i).getActivityId());
			processInstanceMap.put("businessKey", processInstanceList.get(i).getBusinessKey());
			processInstanceMap.put(DESCRIPTION, processInstanceList.get(i).getDescription());
			processInstanceMap.put("deploymentId", processInstanceList.get(i).getDeploymentId());
			processInstanceMap.put("ended", processInstanceList.get(i).isEnded());
			processInstanceMap.put("name", processInstanceList.get(i).getName());
			processInstanceMap.put("processInstanceId", processInstanceList.get(i).getProcessInstanceId());
			processInstanceMap.put("processDefinitionId", processInstanceList.get(i).getProcessDefinitionId());
			processInstanceMap.put("processDefinitionKey", processInstanceList.get(i).getProcessDefinitionKey());
			processInstanceMap.put("processDefinitionName", processInstanceList.get(i).getProcessDefinitionName());
			processInstanceMap.put("processDefinitionVersion", processInstanceList.get(i).getProcessDefinitionVersion());
			processInstanceMap.put("processVariables", processInstanceList.get(i).getProcessVariables());
			processInstanceMap.put("startUserId", processInstanceList.get(i).getStartUserId());
			processInstanceMap.put("startTime", processInstanceList.get(i).getStartTime());
			processInstanceMap.put(SUSPENDED, processInstanceList.get(i).isSuspended());
			processInstanceMap.put(TENANTID, processInstanceList.get(i).getTenantId());
			resultList.add(processInstanceMap);
		}

		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String roleData = sysRoleServiceClient.queryRoleData("processinstance", authentication.getName());
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), processInstanceQuery.count(), currentPage,
					pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, processInstanceQuery.count(), currentPage, pageSize);
		}
	}

	/**
	 * 查询流程实例执行路径分页
	 */
	@Override
	public Map<String, Object> queryExecution(Integer currentPage, Integer pageSize, String processInstanceId) {
		ExecutionQuery executionQuery = runtimeService.createExecutionQuery();
		if (StringUtils.isNotBlank(processInstanceId)) {
			executionQuery.processInstanceId(processInstanceId);
		}
		List<Execution> executionList = executionQuery.listPage(pageSize * (currentPage - 1), pageSize);
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		for (int i = 0; i < executionList.size(); i++) {
			LinkedHashMap<String, Object> executionMap = new LinkedHashMap<>();
			executionMap.put("id", executionList.get(i).getId());
			executionMap.put("activityId", executionList.get(i).getActivityId());
			executionMap.put(DESCRIPTION, executionList.get(i).getDescription());
			executionMap.put("name", executionList.get(i).getName());
			executionMap.put("parentId", executionList.get(i).getParentId());
			executionMap.put("parentProcessInstanceId", executionList.get(i).getParentProcessInstanceId());
			executionMap.put("processInstanceId", executionList.get(i).getProcessInstanceId());
			executionMap.put("rootProcessInstanceId", executionList.get(i).getRootProcessInstanceId());
			executionMap.put("superExecutionId", executionList.get(i).getSuperExecutionId());
			executionMap.put(TENANTID, executionList.get(i).getTenantId());
			executionMap.put("ended", executionList.get(i).isEnded());
			executionMap.put(SUSPENDED, executionList.get(i).isSuspended());
			resultList.add(executionMap);
		}

		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String roleData = sysRoleServiceClient.queryRoleData("execution", authentication.getName());
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), executionQuery.count(), currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, executionQuery.count(), currentPage, pageSize);
		}
	}

	/**
	 * 获取流程定义XML
	 */
	@Override
	public String getProcessResource(String deploymentId) {
		ProcessDefinition processDefinition = checkProcessDefinition(deploymentId);
		String bpmnResourceName = processDefinition.getResourceName();
		final InputStream deploymentInputStream = repositoryService.getResourceAsStream(deploymentId, bpmnResourceName);
		byte[] bytes = IoUtil.readInputStream(deploymentInputStream, "input stream");
		return new String(bytes);
	}

	/**
	 * 获取高亮跟踪流程图
	 */
	@Override
	public String getHighLightedProcessDiagram(String processInstanceId) {
		securityUtil.logInAs("admin");
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		if (task == null) {
			throw new ActivitiException("流程已结束");
		}
		org.activiti.api.process.model.ProcessInstance processInstance = processRuntime.processInstance(processInstanceId);
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
		return new String(processDiagramGeneratorWrapper.generateDiagram(bpmnModel,
				processRuntime.processInstanceMeta(processInstance.getId()).getActiveActivitiesIds(), emptyList()), StandardCharsets.UTF_8);
	}

	/**
	 * 获取流程定义图片
	 */
	@Override
	public InputStream getProcessImage(String deploymentId) {
		ProcessDefinition processDefinition = checkProcessDefinition(deploymentId);
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
		InputStream inputStream = null;
		if (bpmnModel != null && bpmnModel.getLocationMap().size() > 0) {
			ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
			inputStream = processDiagramGenerator.generateDiagram(bpmnModel, emptyList(), emptyList(), "宋体", "宋体", "宋体", true);
		}
		return inputStream;
	}

	/**
	 * 校验流程定义是否存在
	 */
	private ProcessDefinition checkProcessDefinition(String deploymentId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
		if (processDefinition == null) {
			throw new ActivitiObjectNotFoundException("找不到此流程定义", ProcessDefinition.class);
		}
		return processDefinition;
	}

	/**
	 * 持久化流程
	 */
	@Override
	public void persistProcess(String modelId, String jsonXml, String svgXml) {
		Model model = repositoryService.getModel(modelId);
		repositoryService.addModelEditorSource(model.getId(), jsonXml.getBytes(StandardCharsets.UTF_8));
		repositoryService.addModelEditorSourceExtra(model.getId(), svgXml.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * 发起流程
	 */
	@Override
	public void startProcessInstance(Leave leave, String starterUser) {
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(leave.getProcessDefinitionId());
		if (processDefinition.isSuspended()) {
			throw new ActivitiIllegalArgumentException("流程定义已挂起，不能发起");
		}
		Authentication.setAuthenticatedUserId(starterUser);
		Map<String, Object> startVariables = new HashMap<>();
		startVariables.put("candidate", leave.getCandidate());
		startVariables.put("startTime", leave.getStartTime());
		startVariables.put("endTime", leave.getEndTime());
		startVariables.put(DESCRIPTION, leave.getDescription());

		JSONArray customForm = leave.getCustomForm();// 自定义字段
		for (int i = 0; i < customForm.size(); i++) {
			JSONObject jsonObject = customForm.getJSONObject(i);
			Iterator iterator = jsonObject.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				startVariables.put("动态属性" + String.valueOf(entry.getKey()), entry.getValue());
			}
		}

		ProcessInstance processInstance = runtimeService.startProcessInstanceById(leave.getProcessDefinitionId(), startVariables);
		runtimeService.setProcessInstanceName(processInstance.getId(), starterUser + "发起的流程");
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		if (task != null) {
			if (StringUtils.isBlank(leave.getCandidate())) {
				throw new ActivitiIllegalArgumentException("流程有待审批，请正确选择审批人员");
			}
			mscodeTaskService.updateTask(task.getId(), leave.getCandidate());
		}
	}

	/**
	 * 挂起流程定义
	 */
	@Override
	public void suspendProcessDefinition(String processDefinitionId) {
		if (repositoryService.isProcessDefinitionSuspended(processDefinitionId)) {
			throw new ActivitiException("ID为'" + processDefinitionId + " '的流程定义已挂起");
		}
		repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null); // true是指包含流程实例
	}

	/**
	 * 激活流程定义
	 */
	@Override
	public void activateProcessDefinition(String processDefinitionId) {
		if (!repositoryService.isProcessDefinitionSuspended(processDefinitionId)) {
			throw new ActivitiException("ID为'" + processDefinitionId + " '的流程定义已激活");
		}
		repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);// true是指包含流程实例
	}

	/**
	 * 挂起流程实例
	 */
	@Override
	public void suspendProcessInstance(String processInstanceId) {
		ProcessInstance processInstance = checkProcessInstance(processInstanceId);
		if (processInstance.isSuspended()) {
			throw new ActivitiException("ID为'" + processInstance.getId() + " '的流程实例已挂起");
		}
		runtimeService.suspendProcessInstanceById(processInstance.getId());
	}

	/**
	 * 激活流程实例
	 */
	@Override
	public void activateProcessInstance(String processInstanceId) {
		ProcessInstance processInstance = checkProcessInstance(processInstanceId);
		if (!processInstance.isSuspended()) {
			throw new ActivitiException("ID为'" + processInstance.getId() + " '的流程实例已激活");
		}
		runtimeService.activateProcessInstanceById(processInstance.getId());
	}

	/**
	 * 删除或终止流程实例
	 */
	@Override
	public void deleteProcessInstance(String[] processInstanceId) {
		for (int i = 0; i < processInstanceId.length; i++) {
			ProcessInstance processInstance = checkProcessInstance(processInstanceId[i]);
			runtimeService.deleteProcessInstance(processInstance.getId(), "删除ID为'" + processInstanceId[i] + " '的流程实例");
		}
	}

	/**
	 * 校验流程实例是否存在
	 */
	private ProcessInstance checkProcessInstance(String processInstanceId) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if (processInstance == null) {
			throw new ActivitiObjectNotFoundException("找不到ID为'" + processInstanceId + "'的流程实例");
		}
		return processInstance;
	}

}
