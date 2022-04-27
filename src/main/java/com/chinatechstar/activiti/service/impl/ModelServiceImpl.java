package com.chinatechstar.activiti.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.activiti.mapper.ModelMapper;
import com.chinatechstar.activiti.service.ModelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 模型信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class ModelServiceImpl implements ModelService, ModelDataJsonConstants {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private RepositoryService repositoryService;

	/**
	 * 查询模型分页
	 */
	@Override
	public LinkedHashMap<String, Object> queryModel(Integer currentPage, Integer pageSize, String name, String category, String orgId, Integer version) {
		ModelQuery modelQuery = repositoryService.createModelQuery();
		if (StringUtils.isNotBlank(name)) {
			modelQuery.modelNameLike("%" + name + "%");
		}
		if (StringUtils.isNotBlank(category)) {
			modelQuery.modelCategoryLike("%" + category + "%");
		}
		if (version != null) {
			modelQuery.modelVersion(version);
		}
		modelQuery.modelOrgId(orgId);
		List<Model> resultList = modelQuery.orderByCreateTime().desc().listPage(pageSize * (currentPage - 1), pageSize);
		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put("list", resultList);
		LinkedHashMap<String, Long> paginationMap = new LinkedHashMap<>();
		paginationMap.put("total", modelQuery.count());
		paginationMap.put("pageSize", (long) pageSize);
		paginationMap.put("current", (long) currentPage);
		resultMap.put("pagination", paginationMap);
		return resultMap;
	}

	/**
	 * 新增模型
	 */
	public void addModel(String name, String category, String description, String orgId, String menuCode, String[] referGroupId, String[][] userId) {
		Model model = repositoryService.newModel();
		int modelRevision = 1;
		String key = UUID.randomUUID().toString();
		ObjectNode modelNode = objectMapper.createObjectNode();
		modelNode.put(MODEL_ORGID, orgId);
		modelNode.put(MODEL_MENUCODE, menuCode);
		modelNode.put(MODEL_NAME, name);
		modelNode.put(MODEL_DESCRIPTION, description);
		modelNode.put(MODEL_REVISION, modelRevision);
		model.setOrgId(orgId);
		model.setMenuCode(menuCode);
		model.setName(name);
		model.setCategory(category);
		model.setKey(key);
		model.setMetaInfo(modelNode.toString());
		repositoryService.saveModel(model);
		persistModel(model.getId(), referGroupId, userId);
	}

	/**
	 * 编辑模型
	 */
	@Override
	public void updateModel(String modelId, String name, String category, String orgId, String menuCode, String[] referGroupId, String[][] userId)
			throws IOException {
		Model model = repositoryService.getModel(modelId);
		ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
		modelJson.put(MODEL_ORGID, orgId);
		modelJson.put(MODEL_MENUCODE, menuCode);
		modelJson.put(MODEL_NAME, name);
		model.setMetaInfo(modelJson.toString());
		model.setOrgId(orgId);
		model.setMenuCode(menuCode);
		model.setName(name);
		model.setCategory(category);
		modelMapper.deleteModelSysOrg(modelId);
		modelMapper.deleteModelSysUser(modelId);
		persistModel(modelId, referGroupId, userId);
		repositoryService.saveModel(model);
	}

	/**
	 * 持久化模型
	 */
	private void persistModel(String modelId, String[] referGroupId, String[][] userId) {
		if (referGroupId != null) {
			for (int i = 0; i < referGroupId.length; i++) {
				modelMapper.insertModelSysOrg(UUID.randomUUID().toString(), modelId, referGroupId[i]);
			}
		}
		if (userId != null) {
			Set<String> userIdSet = new HashSet<>();
			for (int x = 0; x < userId.length; x++) {
				for (int y = 0; y < userId[x].length; y++) {
					userIdSet.add(userId[x][y]);
				}
			}
			Iterator<String> iterator = userIdSet.iterator();
			while (iterator.hasNext()) {
				String newUserId = null;
				String postCode = null;
				String[] userIdData;
				String data = iterator.next();
				if (StringUtils.contains(data, '|')) {
					userIdData = data.split("\\|");
					newUserId = userIdData[0];
					postCode = userIdData[1];
				} else {
					newUserId = data;
				}
				modelMapper.insertModelSysUser(UUID.randomUUID().toString(), modelId, newUserId, postCode);
			}
		}
	}

	/**
	 * 复制模型
	 */
	@Override
	public void copyModel(String modelId) throws IOException, XMLStreamException {
		Model sourceModel = repositoryService.getModel(modelId);
		byte[] modelEditorSource = repositoryService.getModelEditorSource(modelId);
		ObjectNode sourceObjectNode = null;
		if (modelEditorSource != null) {
			sourceObjectNode = (ObjectNode) objectMapper.readTree(sourceModel.getMetaInfo());
		}
		Model model = repositoryService.newModel();
		String key = UUID.randomUUID().toString();
		model.setKey(key);
		model.setName(sourceModel.getName());
		model.setCategory(sourceModel.getCategory());
		model.setVersion(1);
		model.setMenuCode(sourceModel.getMenuCode());
		model.setMetaInfo(sourceModel.getMetaInfo());
		model.setOrgId(sourceModel.getOrgId());
		model.setOrgIdCn(sourceModel.getOrgIdCn());
		model.setReferGroupId(sourceModel.getReferGroupId());
		model.setReferGroupIdArray(sourceModel.getReferGroupIdArray());
		model.setReferGroupNames(sourceModel.getReferGroupNames());
		model.setTenantId(sourceModel.getTenantId());
		model.setUserId(sourceModel.getUserId());
		model.setUsername(sourceModel.getUsername());
		repositoryService.saveModel(model);
		ObjectNode editorNode = null;
		if (sourceObjectNode != null) {
			editorNode = sourceObjectNode.deepCopy();
		}
		ObjectNode properties = objectMapper.createObjectNode();
		properties.put("process_id", model.getKey());
		properties.put("name", model.getName());
		if (editorNode != null) {
			editorNode.set("properties", properties);
			repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes("utf-8"));
		} else {
			repositoryService.addModelEditorSource(model.getId(), null);
		}
	}

	/**
	 * 部署模型
	 */
	@Override
	public void deployModel(String modelId) throws IOException, XMLStreamException {
		Model model = repositoryService.getModel(modelId);
		byte[] bytes = repositoryService.getModelEditorSource(model.getId());
		if (bytes == null) {
			throw new ActivitiException("模型数据为空，请先设计流程再部署");
		}
		InputStream xmlInputStream = new ByteArrayInputStream(bytes);
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
		xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
		InputStreamReader xmlInputStreamReader = new InputStreamReader(xmlInputStream, StandardCharsets.UTF_8);
		XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(xmlInputStreamReader);
		BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xmlStreamReader);
		if (bpmnModel.getProcesses().isEmpty()) {
			throw new ActivitiException("模型数据不符要求，请至少设计一条主线流程");
		}
		byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
		String processName = model.getName() + ".bpmn20.xml";
		Deployment deployment = repositoryService.createDeployment().name(model.getName()).category(model.getCategory())
				.addString(processName, new String(bpmnBytes, StandardCharsets.UTF_8)).disableSchemaValidation().deploy();
		model.setDeploymentId(deployment.getId());
		repositoryService.saveModel(model);
		xmlStreamReader.close();
		xmlInputStreamReader.close();
		xmlInputStream.close();
	}

	/**
	 * 删除模型
	 */
	@Override
	public void deleteModel(String[] modelId) {
		for (int i = 0; i < modelId.length; i++) {
			Model model = repositoryService.getModel(modelId[i]);
			if (StringUtils.isNotBlank(model.getDeploymentId())) {
				throw new ActivitiException("请先停用，再删除模型");
			}
			repositoryService.deleteModel(modelId[i]);
		}
	}

}
