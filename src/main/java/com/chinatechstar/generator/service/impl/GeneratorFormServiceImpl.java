package com.chinatechstar.generator.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.admin.mapper.SysRoleMapper;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import com.chinatechstar.component.commons.utils.CollectionUtils;
import com.chinatechstar.component.commons.utils.CurrentUserUtils;
import com.chinatechstar.component.commons.utils.SequenceGenerator;
import com.chinatechstar.generator.entity.GeneratorForm;
import com.chinatechstar.generator.mapper.GeneratorFormMapper;
import com.chinatechstar.generator.service.GeneratorFormService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 表单信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class GeneratorFormServiceImpl implements GeneratorFormService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static SequenceGenerator sequenceGenerator = new SequenceGenerator();

	@Autowired
	private GeneratorFormMapper generatorFormMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;

	/**
	 * 查询表单分页
	 */
	@Override
	public Map<String, Object> queryGeneratorForm(Integer currentPage, Integer pageSize, String formDescription, String formContent, String sorter) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("formDescription", formDescription);
		paramMap.put("formContent", formContent);
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
		List<LinkedHashMap<String, Object>> resultList = generatorFormMapper.queryGeneratorForm(paramMap);

		String roleData = sysRoleMapper.queryRoleData("generatorform", CurrentUserUtils.getOAuth2AuthenticationInfo().get("name"));
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), page.getTotal(), currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, page.getTotal(), currentPage, pageSize);
		}
	}

	/**
	 * 查询表单的导出数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> queryGeneratorFormForExcel(Map<String, Object> paramMap) {
		return generatorFormMapper.queryGeneratorForm(paramMap);
	}

	/**
	 * 新增表单
	 */
	@Override
	public void insertGeneratorForm(GeneratorForm generatorForm) {
		generatorForm.setId(sequenceGenerator.nextId());
		generatorForm.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		generatorFormMapper.insertGeneratorForm(generatorForm);
		logger.info("表单已新增： {}", generatorForm.getId());
	}

	/**
	 * 编辑表单
	 */
	@Override
	public void updateGeneratorForm(GeneratorForm generatorForm) {
		generatorFormMapper.updateGeneratorForm(generatorForm);
		logger.info("表单已编辑： {}", generatorForm.getId());
	}

	/**
	 * 删除表单
	 */
	@Override
	public void deleteGeneratorForm(Long[] id) {
		generatorFormMapper.deleteGeneratorForm(id);
	}
}
