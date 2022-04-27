package com.chinatechstar.generator.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinatechstar.generator.entity.GeneratorForm;

/**
 * 表单信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface GeneratorFormService {

	/**
	 * 查询表单分页
	 * 
	 * @param currentPage     当前页数
	 * @param pageSize        每页记录数
	 * @param formDescription 表单描述
	 * @param formContent     表单内容
	 * @param sorter          排序
	 * @return
	 */
	Map<String, Object> queryGeneratorForm(Integer currentPage, Integer pageSize, String formDescription, String formContent, String sorter);

	/**
	 * 查询表单的导出数据列表
	 * 
	 * @param paramMap 表单Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryGeneratorFormForExcel(Map<String, Object> paramMap);

	/**
	 * 新增表单
	 * 
	 * @param generatorForm 表单对象
	 */
	void insertGeneratorForm(GeneratorForm generatorForm);

	/**
	 * 编辑表单
	 * 
	 * @param generatorForm 表单对象
	 */
	void updateGeneratorForm(GeneratorForm generatorForm);

	/**
	 * 删除表单
	 * 
	 * @param id 表单ID
	 */
	void deleteGeneratorForm(Long[] id);

}
