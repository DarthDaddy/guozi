package com.chinatechstar.activiti.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.activiti.service.DeploymentService;
import com.chinatechstar.admin.controller.SysRoleController;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import com.chinatechstar.component.commons.utils.CollectionUtils;

/**
 * 部署信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class DeploymentServiceImpl implements DeploymentService {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private SysRoleController sysRoleServiceClient;

	/**
	 * 查询已部署的模型分页
	 */
	@Override
	public Map<String, Object> queryDeployment(Integer currentPage, Integer pageSize, String deploymentName, String category) {
		DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
		if (StringUtils.isNotBlank(deploymentName)) {
			deploymentQuery.deploymentNameLike("%" + deploymentName + "%");
		}
		if (StringUtils.isNotBlank(category)) {
			deploymentQuery.deploymentCategoryLike("%" + category + "%");
		}
		List<Deployment> deploymentList = deploymentQuery.orderByDeploymenTime().desc().listPage(pageSize * (currentPage - 1), pageSize);
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		for (int i = 0; i < deploymentList.size(); i++) {
			LinkedHashMap<String, Object> entityMap = new LinkedHashMap<>();
			entityMap.put("id", deploymentList.get(i).getId());
			entityMap.put("key", deploymentList.get(i).getKey());
			entityMap.put("name", deploymentList.get(i).getName());
			entityMap.put("category", deploymentList.get(i).getCategory());
			entityMap.put("deploymentTime", deploymentList.get(i).getDeploymentTime());
			entityMap.put("tenantId", deploymentList.get(i).getTenantId());
			Model model = repositoryService.createModelQuery().deploymentId(deploymentList.get(i).getId()).singleResult();
			if (model != null) {
				entityMap.put("modelCategory", model.getCategory());
				entityMap.put("modelVersion", model.getVersion());
			}
			resultList.add(entityMap);
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String roleData = sysRoleServiceClient.queryRoleData("deployment", authentication.getName());
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), deploymentQuery.count(), currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, deploymentQuery.count(), currentPage, pageSize);
		}
	}

	/**
	 * 删除已部署的模型
	 */
	@Override
	public void deleteDeployment(String[] deploymentId) {
		for (int i = 0; i < deploymentId.length; i++) {
			repositoryService.deleteDeployment(deploymentId[i], true);// true指级联删除
		}
	}

}
