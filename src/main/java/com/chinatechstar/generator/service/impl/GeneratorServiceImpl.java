package com.chinatechstar.generator.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.admin.controller.SysRoleController;
import com.chinatechstar.component.commons.exception.ServiceException;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import com.chinatechstar.component.commons.utils.CollectionUtils;
import com.chinatechstar.component.commons.utils.CurrentUserUtils;
import com.chinatechstar.component.commons.utils.GeneratorUtils;
import com.chinatechstar.component.commons.utils.SequenceGenerator;
import com.chinatechstar.generator.entity.Generator;
import com.chinatechstar.generator.mapper.GeneratorMapper;
import com.chinatechstar.generator.mapper.GeneratorTemplateMapper;
import com.chinatechstar.generator.service.GeneratorService;
import com.chinatechstar.generator.vo.GeneratorFieldVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 代码信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class GeneratorServiceImpl implements GeneratorService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static SequenceGenerator sequenceGenerator = new SequenceGenerator();

	@Autowired
	private GeneratorMapper generatorMapper;
	@Autowired
	private GeneratorTemplateMapper generatorTemplateMapper;
	@Autowired
	private SysRoleController sysRoleServiceClient;

	/**
	 * 查询代码信息分页
	 */
	@Override
	public Map<String, Object> queryGenerator(Integer currentPage, Integer pageSize, String entityName, String serviceName, String packageName,
			String tableName, String sorter) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("entityName", entityName);
		paramMap.put("serviceName", serviceName);
		paramMap.put("packageName", packageName);
		paramMap.put("tableName", tableName);
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
		List<LinkedHashMap<String, Object>> resultList = generatorMapper.queryGenerator(paramMap);

		String roleData = sysRoleServiceClient.queryRoleData("generator", CurrentUserUtils.getOAuth2AuthenticationInfo().get("name"));
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), page.getTotal(), currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, page.getTotal(), currentPage, pageSize);
		}
	}

	/**
	 * 查询代码信息的导出数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> queryGeneratorForExcel(Map<String, Object> paramMap) {
		return generatorMapper.queryGenerator(paramMap);
	}

	/**
	 * 根据代码信息ID查询对应的实体字段
	 */
	@Override
	public Map<String, Object> queryFieldByGeneratorId(GeneratorFieldVO generatorFieldVO) {
		Map<String, Object> resultMap = new HashMap<>();
		List<LinkedHashMap<String, Object>> resultList = generatorMapper.queryFieldByGeneratorId(generatorFieldVO.getGeneratorId());
		resultMap.put("list", resultList);
		resultMap.put("count", resultList.size());
		return resultMap;
	}

	/**
	 * 查询数据表信息分页
	 */
	@Override
	public Map<String, Object> queryGeneratorTable(Integer currentPage, Integer pageSize, String tableName, String tableComment, String sorter) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tableName", tableName);
		paramMap.put("tableComment", tableComment);
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
		List<LinkedHashMap<String, Object>> resultList = generatorMapper.queryGeneratorTable(paramMap);

		String roleData = sysRoleServiceClient.queryRoleData("generatortable", CurrentUserUtils.getOAuth2AuthenticationInfo().get("name"));
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), page.getTotal(), currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, page.getTotal(), currentPage, pageSize);
		}
	}

	/**
	 * 查询数据表信息的导出数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> queryGeneratorTableForExcel(Map<String, Object> paramMap) {
		return generatorMapper.queryGeneratorTable(paramMap);
	}

	/**
	 * 新增代码信息
	 */
	@Override
	public void insertGenerator(Generator generator) {
		generator.setId(sequenceGenerator.nextId());
		insertGeneratorField(generator);
		generator.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		generatorMapper.insertGenerator(generator);
		logger.info("代码信息已新增： {}", generator.getServiceName());
	}

	/**
	 * 编辑代码信息
	 */
	@Override
	public void updateGenerator(Generator generator) {
		generatorMapper.deleteGeneratorField(new Long[] { generator.getId() });
		insertGeneratorField(generator);
		generatorMapper.updateGenerator(generator);
		logger.info("代码信息已编辑： {}", generator.getServiceName());
	}

	/**
	 * 新增实体字段信息
	 */
	private void insertGeneratorField(Generator generator) {
		for (int i = 0; i < generator.getGeneratorField().size(); i++) {
			generatorMapper.insertGeneratorField(sequenceGenerator.nextId(), generator.getGeneratorField().get(i).getFieldType(),
					generator.getGeneratorField().get(i).getField(), generator.getId());
		}
	}

	/**
	 * 删除代码信息
	 */
	@Override
	public void deleteGenerator(Long[] id) {
		generatorMapper.deleteGenerator(id);
		generatorMapper.deleteGeneratorField(id);
	}

	/**
	 * 生成代码资源
	 */
	public byte[] generateResource(Long[] id, String type) {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);) {
			List<LinkedHashMap<String, Object>> codeList = generatorMapper.queryGeneratorById(id);
			for (int i = 0; i < codeList.size(); i++) {
				GeneratorUtils.generateResource(codeList.get(i), generatorMapper.queryFieldByGeneratorId(Long.valueOf(codeList.get(i).get("id").toString())),
						generatorTemplateMapper.queryTemplateByType(type), zipOutputStream);
			}
			zipOutputStream.close();
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			throw new ServiceException(e.toString());
		}
	}

	/**
	 * 生成反向代码资源
	 */
	@Override
	public byte[] generateTableResource(String tableName, String columnName, String packageName, String entityName, String serviceName, String type) {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);) {
			LinkedHashMap<String, Object> codeMap = new LinkedHashMap<>();
			codeMap.put("tableName", tableName);
			codeMap.put("packageName", packageName);
			codeMap.put("entityName", entityName);
			List<LinkedHashMap<String, Object>> fieldList = new ArrayList<>();
			String[] columnNameArray = columnName.split(",");
			if (columnName != null) {
				for (int i = 0; i < columnNameArray.length; i++) {
					LinkedHashMap<String, Object> columnNameMap = new LinkedHashMap<>();
					columnNameMap.put("fieldType", "String");
					columnNameMap.put("field", GeneratorUtils.fieldToProperty(columnNameArray[i]));
					columnNameMap.put("upperField", GeneratorUtils.toUpperCaseFirstOne(GeneratorUtils.fieldToProperty(columnNameArray[i])));
					fieldList.add(columnNameMap);
				}
			}
			GeneratorUtils.generateResource(codeMap, fieldList, generatorTemplateMapper.queryTemplateByType(type), zipOutputStream);
			zipOutputStream.close();
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			throw new ServiceException(e.toString());
		}
	}

}
