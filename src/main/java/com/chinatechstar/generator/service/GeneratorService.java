package com.chinatechstar.generator.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinatechstar.generator.entity.Generator;
import com.chinatechstar.generator.vo.GeneratorFieldVO;

/**
 * 代码信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface GeneratorService {

	/**
	 * 查询代码信息分页
	 * 
	 * @param currentPage 当前页数
	 * @param pageSize    每页记录数
	 * @param entityName  实体名
	 * @param serviceName 服务名
	 * @param packageName 包名
	 * @param tableName   表名
	 * @param sorter      排序
	 * @return
	 */
	Map<String, Object> queryGenerator(Integer currentPage, Integer pageSize, String entityName, String serviceName, String packageName, String tableName,
			String sorter);

	/**
	 * 查询代码信息的导出数据列表
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryGeneratorForExcel(Map<String, Object> paramMap);

	/**
	 * 根据代码信息ID查询对应的实体字段
	 * 
	 * @param generatorFieldVO 实体字段信息的参数
	 * @return
	 */
	Map<String, Object> queryFieldByGeneratorId(GeneratorFieldVO generatorFieldVO);

	/**
	 * 查询数据表信息分页
	 * 
	 * @param currentPage  当前页数
	 * @param pageSize     每页记录数
	 * @param tableName    表名
	 * @param tableComment 表注释
	 * @param sorter       排序
	 * @return
	 */
	Map<String, Object> queryGeneratorTable(Integer currentPage, Integer pageSize, String tableName, String tableComment, String sorter);

	/**
	 * 查询数据表信息的导出数据列表
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryGeneratorTableForExcel(Map<String, Object> paramMap);

	/**
	 * 新增代码信息
	 * 
	 * @param generator 代码信息对象
	 */
	void insertGenerator(Generator generator);

	/**
	 * 编辑代码信息
	 * 
	 * @param generator 代码信息对象
	 */
	void updateGenerator(Generator generator);

	/**
	 * 删除代码信息
	 * 
	 * @param id 代码信息ID
	 */
	void deleteGenerator(Long[] id);

	/**
	 * 生成正向代码资源
	 * 
	 * @param id   代码信息ID
	 * @param type 模板类型
	 * @return
	 */
	byte[] generateResource(Long[] id, String type);

	/**
	 * 生成反向代码资源
	 * 
	 * @param tableName   表名
	 * @param columnName  表字段
	 * @param packageName 包名
	 * @param entityName  实体名
	 * @param serviceName 服务名
	 * @param type        模板类型
	 * @return
	 */
	byte[] generateTableResource(String tableName, String columnName, String packageName, String entityName, String serviceName, String type);

}
