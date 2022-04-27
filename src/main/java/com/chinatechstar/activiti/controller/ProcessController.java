package com.chinatechstar.activiti.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chinatechstar.activiti.entity.Leave;
import com.chinatechstar.activiti.service.ProcessService;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.validator.InsertValidator;

/**
 * 流程信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/process")
public class ProcessController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProcessService processService;

	/**
	 * 查询流程定义分页
	 * 
	 * @param currentPage               当前页数
	 * @param pageSize                  每页记录数
	 * @param processDefinitionKey      流程定义编号
	 * @param processDefinitionName     流程定义名称
	 * @param processDefinitionCategory 流程定义类别
	 * @return
	 */
	@GetMapping(path = "/queryProcessDefinition")
	public ListResult<Object> queryProcessDefinition(@RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(name = "processDefinitionKey", required = false) String processDefinitionKey,
			@RequestParam(name = "processDefinitionName", required = false) String processDefinitionName,
			@RequestParam(name = "processDefinitionCategory", required = false) String processDefinitionCategory,
			@RequestParam(name = "processDefinitionVersion", required = false) Integer processDefinitionVersion,
			@RequestParam(name = "suspended", required = false) Short suspended) {
		Map<String, Object> data = processService.queryProcessDefinition(currentPage, pageSize, processDefinitionKey, processDefinitionName,
				processDefinitionCategory, processDefinitionVersion, suspended);
		return ResultBuilder.buildListSuccess(data);
	}

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
	@GetMapping(path = "/queryProcessInstance")
	public ListResult<Object> queryProcessInstance(@RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(name = "processDefinitionKey", required = false) String processDefinitionKey,
			@RequestParam(name = "processDefinitionName", required = false) String processDefinitionName,
			@RequestParam(name = "processDefinitionVersion", required = false) Integer processDefinitionVersion,
			@RequestParam(name = "processInstanceName", required = false) String processInstanceName,
			@RequestParam(name = "suspended", required = false) Short suspended, @RequestParam(name = "startUserId", required = false) String startUserId) {
		Map<String, Object> data = processService.queryProcessInstance(currentPage, pageSize, processDefinitionKey, processDefinitionName,
				processDefinitionVersion, processInstanceName, suspended, startUserId);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询流程实例执行路径分页
	 * 
	 * @param currentPage       当前页数
	 * @param pageSize          每页记录数
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	@GetMapping(path = "/queryExecution")
	public ListResult<Object> queryExecution(@RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(name = "processInstanceId", required = true) String processInstanceId) {
		Map<String, Object> data = processService.queryExecution(currentPage, pageSize, processInstanceId);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 获取流程定义XML
	 * 
	 * @param deploymentId 部署ID
	 * @return
	 */
	@PostMapping(path = "/getProcessResource")
	public ListResult<Object> getProcessResource(@RequestParam(name = "deploymentId", required = true) String deploymentId) {
		return ResultBuilder.buildListSuccess(processService.getProcessResource(deploymentId));
	}

	/**
	 * 获取高亮跟踪流程图
	 * 
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	@PostMapping(path = "/getHighLightedProcessDiagram")
	public ListResult<Object> getHighLightedProcessDiagram(@RequestParam(name = "processInstanceId", required = true) String processInstanceId) {
		return ResultBuilder.buildListSuccess(processService.getHighLightedProcessDiagram(processInstanceId));
	}

	/**
	 * 获取流程定义图片
	 * 
	 * @param deploymentId 部署ID
	 * @return
	 */
	@PostMapping(value = "/getProcessImage")
	public ResponseEntity<byte[]> getProcessImage(@RequestParam(name = "deploymentId", required = true) String deploymentId) {
		ResponseEntity<byte[]> responseEntity = null;
		try {
			InputStream imageInputStream = processService.getProcessImage(deploymentId);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Content-Type", "text/xml");
			responseEntity = new ResponseEntity<>(IOUtils.toByteArray(imageInputStream), responseHeaders, HttpStatus.OK);
		} catch (IOException e) {
			logger.warn(e.toString());
		}
		return responseEntity;
	}

	/**
	 * 持久化流程
	 * 
	 * @param modelId 模型ID
	 * @param jsonXml 流程基本信息
	 * @param svgXml  流程设计信息
	 * @throws Exception
	 */
	@PostMapping(path = "/persistProcess")
	public ActionResult persistProcess(@RequestParam(name = "modelId", required = true) String modelId,
			@RequestParam(name = "jsonXml", required = true) String jsonXml, @RequestParam(name = "svgXml", required = true) String svgXml) {
		processService.persistProcess(modelId, jsonXml, svgXml);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 发起流程
	 * 
	 * @param leave 请假对象
	 * @return
	 */
	@PostMapping(path = "/startProcessInstance")
	public ActionResult startProcessInstance(@Validated(InsertValidator.class) @RequestBody Leave leave) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		processService.startProcessInstance(leave, authentication.getName());
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 挂起流程定义
	 * 
	 * @param processDefinitionId 流程定义ID
	 * @return
	 */
	@PostMapping(path = "/suspendProcessDefinition")
	public ActionResult suspendProcessDefinition(@RequestParam(name = "processDefinitionId", required = true) String processDefinitionId) {
		processService.suspendProcessDefinition(processDefinitionId);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 激活流程定义
	 * 
	 * @param processDefinitionId 流程定义ID
	 * @return
	 */
	@PostMapping(path = "/activateProcessDefinition")
	public ActionResult activateProcessDefinition(@RequestParam(name = "processDefinitionId", required = true) String processDefinitionId) {
		processService.activateProcessDefinition(processDefinitionId);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 挂起流程实例
	 * 
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	@PostMapping(path = "/suspendProcessInstance")
	public ActionResult suspendProcessInstance(@RequestParam(name = "processInstanceId", required = true) String processInstanceId) {
		processService.suspendProcessInstance(processInstanceId);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 激活流程实例
	 * 
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	@PostMapping(path = "/activateProcessInstance")
	public ActionResult activateProcessInstance(@RequestParam(name = "processInstanceId", required = true) String processInstanceId) {
		processService.activateProcessInstance(processInstanceId);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除流程实例
	 * 
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	@PostMapping(path = "/deleteProcessInstance")
	public ActionResult deleteProcessInstance(@RequestParam(name = "processInstanceId", required = true) String[] processInstanceId) {
		processService.deleteProcessInstance(processInstanceId);
		return ResultBuilder.buildActionSuccess();
	}

}
