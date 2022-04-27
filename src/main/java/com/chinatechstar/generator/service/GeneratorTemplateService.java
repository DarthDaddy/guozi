package com.chinatechstar.generator.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinatechstar.generator.entity.GeneratorTemplate;

/**
 * 模板信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface GeneratorTemplateService {

	/**
	 * 查询模板信息分页
	 * 
	 * @param currentPage 当前页数
	 * @param pageSize    每页记录数
	 * @param type        模板类型
	 * @param item        模板项目
	 * @param sorter      排序
	 * @return
	 */
	Map<String, Object> queryGeneratorTemplate(Integer currentPage, Integer pageSize, String type, String item, String sorter);

	/**
	 * 查询模板内容
	 * 
	 * @param type 模板类型
	 * @param item 模板项目
	 * @return
	 */
	String queryGeneratorTemplateContent(String type, String item);

	/**
	 * 查询模板信息的导出数据列表
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryGeneratorTemplateForExcel(Map<String, Object> paramMap);

	/**
	 * 新增模板信息
	 * 
	 * @param generatorTemplate 模板信息对象
	 */
	void insertGeneratorTemplate(GeneratorTemplate generatorTemplate);

	/**
	 * 编辑模板信息
	 * 
	 * @param generatorTemplate 模板信息对象
	 */
	void updateGeneratorTemplate(GeneratorTemplate generatorTemplate);

	/**
	 * 编辑模板内容
	 * 
	 * @param generatorTemplate 模板信息对象
	 */
	void updateGeneratorTemplateContent(GeneratorTemplate generatorTemplate);

	/**
	 * 删除模板信息
	 * 
	 * @param type 模板类型
	 */
	void deleteGeneratorTemplate(String[] type);

}
