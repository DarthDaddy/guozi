package com.chinatechstar.activiti.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chinatechstar.activiti.service.DeploymentService;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;

/**
 * 部署信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/deployment")
public class DeploymentController {

	@Autowired
	private DeploymentService deploymentService;

	/**
	 * 查询已部署的模型分页
	 * 
	 * @param currentPage    当前页数
	 * @param pageSize       每页记录数
	 * @param deploymentName 已部署的模型名称
	 * @return
	 */
	@GetMapping(path = "/queryDeployment")
	public ListResult<Object> queryDeployment(@RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(name = "deploymentName", required = false) String deploymentName,
			@RequestParam(name = "category", required = false) String category) {
		Map<String, Object> data = deploymentService.queryDeployment(currentPage, pageSize, deploymentName, category);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 删除已部署的模型
	 * 
	 * @param deploymentId 部署ID
	 * @return
	 */
	@PostMapping(path = "/deleteDeployment")
	public ActionResult deleteDeployment(@RequestParam(name = "deploymentId", required = true) String[] deploymentId) {
		deploymentService.deleteDeployment(deploymentId);
		return ResultBuilder.buildActionSuccess();
	}

}
