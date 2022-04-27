package com.chinatechstar.activiti.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chinatechstar.activiti.service.TaskService;
import com.chinatechstar.admin.controller.SysUserController;
import com.chinatechstar.admin.mapper.SysOrgMapper;
import com.chinatechstar.admin.mapper.SysUserMapper;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;

/**
 * 任务信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/task")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private SysUserController sysUserServiceClient;

	@Autowired
	private SysOrgMapper sysOrgMapper;

	@Autowired
	private SysUserMapper sysUserMapper;

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
	@GetMapping(path = "/queryTask")
	public ListResult<Object> queryTask(@RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(name = "processDefinitionName", required = false) String processDefinitionName,
			@RequestParam(name = "taskName", required = false) String taskName, @RequestParam(name = "assignee", required = false) String assignee) {
		Map<String, Object> data = taskService.queryTask(currentPage, pageSize, processDefinitionName, taskName, assignee);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 签收任务
	 * 
	 * @param taskId 任务ID
	 * @return
	 */
	@PostMapping(path = "/claimTask")
	public ActionResult claimTask(@RequestParam(name = "taskId", required = true) String[] taskId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		taskService.claimTask(taskId, authentication.getName());
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 完结任务，任务进入下一节点
	 * 
	 * @param taskId            任务ID
	 * @param processInstanceId 流程实例ID
	 * @param candidate         下一节点候选者
	 * @param comment           批注
	 * @return
	 */
	@PostMapping(path = "/completeTask")
	public ActionResult completeTask(@RequestParam(name = "taskId", required = true) String taskId,
			@RequestParam(name = "processInstanceId", required = true) String processInstanceId,
			@RequestParam(name = "candidate", required = false) String candidate, @RequestParam(name = "comment", required = true) String comment) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		taskService.completeTask(taskId, processInstanceId, candidate, comment, authentication.getName());
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 委派任务
	 * 
	 * @param taskId            任务ID
	 * @param processInstanceId 流程实例ID
	 * @param assignee          签收者
	 * @param comment           批注
	 * @return
	 */
	@PostMapping(path = "/delegateTask")
	public ActionResult delegateTask(@RequestParam(name = "taskId", required = true) String taskId,
			@RequestParam(name = "processInstanceId", required = true) String processInstanceId,
			@RequestParam(name = "assignee", required = false) String assignee, @RequestParam(name = "comment", required = true) String comment) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		taskService.delegateTask(taskId, processInstanceId, comment, authentication.getName(), assignee);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 被委派人完结任务并返回
	 * 
	 * @param taskId            任务ID
	 * @param processInstanceId 流程实例ID
	 * @param comment           批注
	 * @return
	 */
	@PostMapping(path = "/resolveTask")
	public ActionResult resolveTask(@RequestParam(name = "taskId", required = true) String taskId,
			@RequestParam(name = "processInstanceId", required = true) String processInstanceId,
			@RequestParam(name = "comment", required = true) String comment) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		taskService.resolveTask(taskId, processInstanceId, comment, authentication.getName());
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 回退任务
	 * 
	 * @param taskId            任务ID
	 * @param processInstanceId 流程实例ID
	 * @param comment           批注
	 * @return
	 */
	@PostMapping(path = "/regressTask")
	public ActionResult regressTask(@RequestParam(name = "taskId", required = true) String taskId,
			@RequestParam(name = "processInstanceId", required = true) String processInstanceId,
			@RequestParam(name = "comment", required = true) String comment) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		taskService.regressTask(taskId, processInstanceId, comment, authentication.getName());
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 驳回任务
	 * 
	 * @param taskId            任务ID
	 * @param processInstanceId 流程实例ID
	 * @param comment           批注
	 * @return
	 */
	@PostMapping(path = "/rejectTask")
	public ActionResult rejectTask(@RequestParam(name = "taskId", required = true) String taskId,
			@RequestParam(name = "processInstanceId", required = true) String processInstanceId,
			@RequestParam(name = "comment", required = true) String comment) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		taskService.rejectTask(taskId, processInstanceId, comment, authentication.getName());
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 终止任务
	 * 
	 * @param taskId            任务ID
	 * @param processInstanceId 流程实例ID
	 * @param comment           批注
	 * @return
	 */
	@PostMapping(path = "/terminateTask")
	public ActionResult terminateTask(@RequestParam(name = "taskId", required = true) String taskId,
			@RequestParam(name = "processInstanceId", required = true) String processInstanceId,
			@RequestParam(name = "comment", required = true) String comment) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		taskService.terminateTask(taskId, processInstanceId, comment, authentication.getName());
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 查询个人任务分页
	 * 
	 * @param currentPage           当前页数
	 * @param pageSize              每页记录数
	 * @param processDefinitionName 流程定义名称
	 * @param taskName              任务名称
	 * @param startTime             请假开始时间
	 * @param endTime               请假结束时间
	 * @param description           请假备注
	 * @param taskStatus            任务状态
	 * @return
	 */
	@GetMapping(path = "/queryPersonalTask")
	public ListResult<Object> queryPersonalTask(@RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(name = "processDefinitionName", required = false) String processDefinitionName,
			@RequestParam(name = "taskName", required = false) String taskName, @RequestParam(name = "startTime", required = false) String startTime,
			@RequestParam(name = "endTime", required = false) String endTime, @RequestParam(name = "description", required = false) String description,
			@RequestParam(name = "taskStatus", required = true) String taskStatus) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> data = taskService.queryPersonalTask(currentPage, pageSize, authentication.getName(), processDefinitionName, taskName, startTime,
				endTime, description, taskStatus);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询代理人的下拉框数据
	 * 
	 * @return
	 */
	@GetMapping(path = "/queryUsername")
	public ListResult<Object> queryUsername() {
		List<IdentityHashMap<String, Object>> data = new ArrayList<IdentityHashMap<String, Object>>();

		Map<String, Object> orgParamMap = new HashMap<>();
		List<LinkedHashMap<String, Object>> sysOrgList = sysOrgMapper.querySysOrg(orgParamMap);
		for (int i = 0; i < sysOrgList.size(); i++) {
			IdentityHashMap<String, Object> optGroupMap = new IdentityHashMap<>();
			for (Entry<String, Object> sysOrgEntry : sysOrgList.get(i).entrySet()) {
				String sysOrgKey = sysOrgEntry.getKey();
				Object sysOrgValue = sysOrgEntry.getValue();
				if (sysOrgKey.equals("orgName")) {
					optGroupMap.put("value", sysOrgValue);
				}
				if (sysOrgKey.equals("id")) {
					Map<String, Object> sysUserParamMap = new HashMap<>();
					sysUserParamMap.put("orgId", sysOrgValue);
					List<LinkedHashMap<String, Object>> sysUserList = sysUserMapper.querySysUser(sysUserParamMap);
					List<IdentityHashMap<String, Object>> childrenList = new ArrayList<IdentityHashMap<String, Object>>();
					for (int j = 0; j < sysUserList.size(); j++) {
						IdentityHashMap<String, Object> optionMap = new IdentityHashMap<>();
						StringBuilder stringBuilder = new StringBuilder();
						for (Entry<String, Object> sysUserEntry : sysUserList.get(j).entrySet()) {
							String sysUserKey = sysUserEntry.getKey();
							Object sysUserValue = sysUserEntry.getValue();
							if (sysUserKey.equals("username")) {
								stringBuilder.append(sysUserValue);
								optionMap.put("key", sysUserValue);
							}
							if (sysUserKey.equals("nickname")) {
								stringBuilder.append("(" + sysUserValue + ")");
							}
						}
						optionMap.put("value", stringBuilder.toString());
						childrenList.add(optionMap);
					}
					optGroupMap.put("children", childrenList);
				}
			}
			if (!optGroupMap.get("children").toString().equals("[]")) {
				data.add(optGroupMap);
			}
		}

		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询任务批注
	 * 
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	@GetMapping(path = "/queryTaskComment")
	public ListResult<Object> queryTaskComment(@RequestParam(name = "processInstanceId", required = true) String processInstanceId) {
		Map<String, Object> data = taskService.queryTaskComment(processInstanceId);
		return ResultBuilder.buildListSuccess(data);
	}

}
