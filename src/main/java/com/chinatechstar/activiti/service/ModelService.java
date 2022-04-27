package com.chinatechstar.activiti.service;

import java.io.IOException;
import java.util.LinkedHashMap;

import javax.xml.stream.XMLStreamException;

/**
 * 模型信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface ModelService {

	/**
	 * 查询模型分页
	 * 
	 * @param currentPage 当前页数
	 * @param pageSize    每页记录数
	 * @param name        模型名称
	 * @param category    模型类别
	 * @param orgId       机构ID
	 * @param version     模型版本
	 * @return
	 */
	LinkedHashMap<String, Object> queryModel(Integer currentPage, Integer pageSize, String name, String category, String orgId, Integer version);

	/**
	 * 新增模型
	 * 
	 * @param name         模型名称
	 * @param category     模型类别
	 * @param description  模型描述
	 * @param orgId        机构ID
	 * @param menuCode     菜单编码
	 * @param referGroupId 可引用机构ID
	 * @param userId       可引用人ID
	 */
	void addModel(String name, String category, String description, String orgId, String menuCode, String[] referGroupId, String[][] userId);

	/**
	 * 编辑模型
	 * 
	 * @param modelId      模型ID
	 * @param name         模型名称
	 * @param category     模型类别
	 * @param orgId        机构ID
	 * @param menuCode     菜单编码
	 * @param referGroupId 可引用机构ID
	 * @param userId       可引用人ID
	 * @throws IOException
	 */
	void updateModel(String modelId, String name, String category, String orgId, String menuCode, String[] referGroupId, String[][] userId) throws IOException;

	/**
	 * 复制模型
	 * 
	 * @param modelId 模型ID
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	void copyModel(String modelId) throws IOException, XMLStreamException;

	/**
	 * 部署模型
	 * 
	 * @param modelId 模型ID
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	void deployModel(String modelId) throws IOException, XMLStreamException;

	/**
	 * 删除模型
	 * 
	 * @param modelId 模型ID
	 */
	void deleteModel(String[] modelId);

}
