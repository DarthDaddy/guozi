package com.chinatechstar.generator.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.chinatechstar.generator.entity.GeneratorForm;

/**
 * 表单信息的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface GeneratorFormMapper {

	/**
	 * 查询表单分页或导出数据
	 * 
	 * @param paramMap 表单Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryGeneratorForm(Map<String, Object> paramMap);

	/**
	 * 新增表单
	 * 
	 * @param generatorForm 表单对象
	 * @return
	 */
	int insertGeneratorForm(GeneratorForm generatorForm);

	/**
	 * 编辑表单
	 * 
	 * @param generatorForm 表单对象
	 * @return
	 */
	int updateGeneratorForm(GeneratorForm generatorForm);

	/**
	 * 删除表单
	 *
	 * @param id         表单ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteGeneratorForm(@Param(value = "array") Long[] id, @Param(value = "tenantCode") String tenantCode);

}
