package com.chinatechstar.generator.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.chinatechstar.generator.entity.GeneratorTemplate;

/**
 * 模板信息的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface GeneratorTemplateMapper {

	/**
	 * 查询模板信息分页或导出数据
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryGeneratorTemplate(Map<String, Object> paramMap);

	/**
	 * 查询模板信息
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryGeneratorTemplateEntity(Map<String, Object> paramMap);

	/**
	 * 根据模板类型查询模板信息
	 *
	 * @param type       模板类型
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryTemplateByType(@Param(value = "type") String type, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询模板内容
	 *
	 * @param type       模板类型
	 * @param item       模板项目
	 * @param tenantCode 租户编码
	 * @return
	 */
	String queryGeneratorTemplateContent(@Param(value = "type") String type, @Param(value = "item") String item, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询是否已存在此模板类型
	 *
	 * @param type       模板类型
	 * @param tenantCode 租户编码
	 * @return
	 */
	Integer getGeneratorTemplateByType(@Param(value = "type") String type, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 新增模板信息
	 * 
	 * @param generatorTemplate 模板信息对象
	 * @return
	 */
	int insertGeneratorTemplate(GeneratorTemplate generatorTemplate);

	/**
	 * 编辑模板内容
	 * 
	 * @param generatorTemplate 模板信息对象
	 * @return
	 */
	int updateGeneratorTemplateContent(GeneratorTemplate generatorTemplate);

	/**
	 * 删除模板信息
	 *
	 * @param type       模板类型
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteGeneratorTemplate(@Param(value = "array") String[] type, @Param(value = "tenantCode") String tenantCode);

}
