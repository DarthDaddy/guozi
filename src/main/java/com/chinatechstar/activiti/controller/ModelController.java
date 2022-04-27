package com.chinatechstar.activiti.controller;

import java.io.IOException;
import java.util.LinkedHashMap;

import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chinatechstar.activiti.entity.ModelEntity;
import com.chinatechstar.activiti.service.ModelService;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;

/**
 * 模型信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/model")
public class ModelController {

	@Autowired
	private ModelService modelService;

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
	@GetMapping(path = "/queryModel")
	public ListResult<Object> queryModel(@RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(name = "name", required = false) String name, @RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "orgId", required = false) String orgId, @RequestParam(name = "version", required = false) Integer version) {
		LinkedHashMap<String, Object> data = modelService.queryModel(currentPage, pageSize, name, category, orgId, version);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增模型
	 * 
	 * @param modelEntity 模型对象
	 * @return
	 */
	@PostMapping(path = "/addModel")
	public ActionResult addModel(@Validated(InsertValidator.class) @RequestBody ModelEntity modelEntity) {
		modelService.addModel(modelEntity.getName(), modelEntity.getCategory(), modelEntity.getDescription(), modelEntity.getOrgId(), modelEntity.getMenuCode(),
				modelEntity.getReferGroupId(), modelEntity.getUserId());
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑模型
	 * 
	 * @param modelEntity 模型对象
	 * @return
	 * @throws IOException
	 */
	@PutMapping(path = "/updateModel")
	public ActionResult updateModel(@Validated(UpdateValidator.class) @RequestBody ModelEntity modelEntity) throws IOException {
		modelService.updateModel(modelEntity.getModelId(), modelEntity.getName(), modelEntity.getCategory(), modelEntity.getOrgId(), modelEntity.getMenuCode(),
				modelEntity.getReferGroupId(), modelEntity.getUserId());
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 复制模型
	 * 
	 * @param modelId 模型ID
	 * @return
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	@PostMapping(path = "/copyModel")
	public ActionResult copyModel(@RequestParam(name = "modelId", required = true) String modelId) throws IOException, XMLStreamException {
		modelService.copyModel(modelId);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 部署模型
	 * 
	 * @param modelId 模型ID
	 * @return
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	@PostMapping(path = "/deployModel")
	public ActionResult deployModel(@RequestParam(name = "modelId", required = true) String modelId) throws IOException, XMLStreamException {
		modelService.deployModel(modelId);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除模型
	 * 
	 * @param modelId 模型ID
	 * @return
	 */
	@PostMapping(path = "/deleteModel")
	public ActionResult deleteModel(@RequestParam(name = "modelId", required = true) String[] modelId) {
		modelService.deleteModel(modelId);
		return ResultBuilder.buildActionSuccess();
	}

}
