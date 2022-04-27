package com.chinatechstar.generator.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.admin.controller.SysRoleController;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import com.chinatechstar.component.commons.utils.CollectionUtils;
import com.chinatechstar.component.commons.utils.CurrentUserUtils;
import com.chinatechstar.component.commons.utils.SequenceGenerator;
import com.chinatechstar.generator.entity.GeneratorTemplate;
import com.chinatechstar.generator.mapper.GeneratorTemplateMapper;
import com.chinatechstar.generator.service.GeneratorTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 模板信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class GeneratorTemplateServiceImpl implements GeneratorTemplateService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static SequenceGenerator sequenceGenerator = new SequenceGenerator();

	@Autowired
	private GeneratorTemplateMapper generatorTemplateMapper;
	@Autowired
	private SysRoleController sysRoleServiceClient;

	/**
	 * 查询模板信息分页
	 */
	@Override
	public Map<String, Object> queryGeneratorTemplate(Integer currentPage, Integer pageSize, String type, String item, String sorter) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("type", type);
		paramMap.put("item", item);
		paramMap.put("tenantCode", CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		if (StringUtils.isNotBlank(sorter)) {
			String sort = sorter.substring(0, sorter.lastIndexOf('_'));
			String sequence = "ascend".equals(sorter.substring(sorter.lastIndexOf('_') + 1)) ? "ASC" : "DESC";
			paramMap.put("sort", sort);
			paramMap.put("sequence", sequence);
		} else {
			paramMap.put("sort", "createTime");
			paramMap.put("sequence", "DESC");
		}
		Page<Object> page = PageHelper.startPage(currentPage, pageSize);
		List<LinkedHashMap<String, Object>> resultList = generatorTemplateMapper.queryGeneratorTemplate(paramMap);

		String roleData = sysRoleServiceClient.queryRoleData("generatorTemplate", CurrentUserUtils.getOAuth2AuthenticationInfo().get("name"));
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), page.getTotal(), currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, page.getTotal(), currentPage, pageSize);
		}
	}

	/**
	 * 查询模板内容
	 */
	@Override
	public String queryGeneratorTemplateContent(String type, String item) {
		return generatorTemplateMapper.queryGeneratorTemplateContent(type, item);
	}

	/**
	 * 查询模板信息的导出数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> queryGeneratorTemplateForExcel(Map<String, Object> paramMap) {
		return generatorTemplateMapper.queryGeneratorTemplate(paramMap);
	}

	/**
	 * 新增模板信息
	 */
	@Override
	public void insertGeneratorTemplate(GeneratorTemplate generatorTemplate) {
		Integer existing = generatorTemplateMapper.getGeneratorTemplateByType(generatorTemplate.getType().trim());
		if (existing != null && existing > 0) {
			throw new IllegalArgumentException("模板类型已存在");
		}
		String[] itemArray = generatorTemplate.getItem().split(",");
		for (int i = 0; i < itemArray.length; i++) {
			GeneratorTemplate entity = new GeneratorTemplate();
			entity.setId(sequenceGenerator.nextId());
			entity.setType(generatorTemplate.getType());
			entity.setItem(itemArray[i]);
			entity.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
			generatorTemplateMapper.insertGeneratorTemplate(entity);
		}
		logger.info("模板信息已新增： {}", generatorTemplate.getType());
	}

	/**
	 * 编辑模板信息
	 */
	@Override
	public void updateGeneratorTemplate(GeneratorTemplate generatorTemplate) {
		Map<String, Object> paramMap = new HashMap<>();
		String[] itemArray = generatorTemplate.getItem().split(",");
		paramMap.put("type", generatorTemplate.getType());
		paramMap.put("itemArray", itemArray);
		List<LinkedHashMap<String, Object>> generatorTemplateList = generatorTemplateMapper.queryGeneratorTemplateEntity(paramMap);
		TreeMap<String, String> treeMap = new TreeMap<>();
		for (int i = 0; i < generatorTemplateList.size(); i++) {
			treeMap.put(String.valueOf(generatorTemplateList.get(i).get("type")) + String.valueOf(generatorTemplateList.get(i).get("item")),
					String.valueOf(generatorTemplateList.get(i).get("content")));
		}
		generatorTemplateMapper.deleteGeneratorTemplate(new String[] { generatorTemplate.getType() });
		for (int i = 0; i < itemArray.length; i++) {
			GeneratorTemplate entity = new GeneratorTemplate();
			entity.setId(sequenceGenerator.nextId());
			entity.setType(generatorTemplate.getType());
			entity.setItem(itemArray[i]);
			entity.setContent(treeMap.get(generatorTemplate.getType() + itemArray[i]));
			entity.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
			generatorTemplateMapper.insertGeneratorTemplate(entity);
		}
		logger.info("模板信息已编辑： {}", generatorTemplate.getType());
	}

	/**
	 * 编辑模板内容
	 */
	@Override
	public void updateGeneratorTemplateContent(GeneratorTemplate generatorTemplate) {
		generatorTemplateMapper.updateGeneratorTemplateContent(generatorTemplate);
		logger.info("模板内容已编辑： {}", generatorTemplate.getType());
	}

	/**
	 * 删除模板信息
	 */
	@Override
	public void deleteGeneratorTemplate(String[] type) {
		generatorTemplateMapper.deleteGeneratorTemplate(type);
	}

}
