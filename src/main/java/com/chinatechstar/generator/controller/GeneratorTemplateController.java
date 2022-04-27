package com.chinatechstar.generator.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.utils.ExcelUtils;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;
import com.chinatechstar.generator.entity.GeneratorTemplate;
import com.chinatechstar.generator.service.GeneratorTemplateService;
import com.chinatechstar.generator.vo.GeneratorTemplateVO;

/**
 * 模板信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/generatortemplate")
public class GeneratorTemplateController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GeneratorTemplateService generatorTemplateService;

	/**
	 * 查询模板信息分页
	 * 
	 * @param generatorTemplateVO 模板前端参数
	 * @return
	 */
	@GetMapping(path = "/queryGeneratorTemplate")
	public ListResult<Object> queryGeneratorTemplate(GeneratorTemplateVO generatorTemplateVO) {
		Map<String, Object> data = generatorTemplateService.queryGeneratorTemplate(generatorTemplateVO.getCurrentPage(), generatorTemplateVO.getPageSize(),
				generatorTemplateVO.getType(), generatorTemplateVO.getItem(), generatorTemplateVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询模板内容
	 * 
	 * @param generatorTemplateVO 模板前端参数
	 * @return
	 */
	@GetMapping(path = "/queryGeneratorTemplateContent")
	public ListResult<Object> queryGeneratorTemplateContent(GeneratorTemplateVO generatorTemplateVO) {
		String data = generatorTemplateService.queryGeneratorTemplateContent(generatorTemplateVO.getType(), generatorTemplateVO.getItem());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增模板信息
	 * 
	 * @param generatorTemplate 模板信息对象
	 * @return
	 */
	@PostMapping(path = "/addGeneratorTemplate")
	public ActionResult addGeneratorTemplate(@Validated(InsertValidator.class) @RequestBody GeneratorTemplate generatorTemplate) {
		generatorTemplateService.insertGeneratorTemplate(generatorTemplate);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑模板信息
	 * 
	 * @param generatorTemplate 模板信息对象
	 * @return
	 */
	@PutMapping(path = "/updateGeneratorTemplate")
	public ActionResult updateGeneratorTemplate(@Validated(UpdateValidator.class) @RequestBody GeneratorTemplate generatorTemplate) {
		generatorTemplateService.updateGeneratorTemplate(generatorTemplate);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑模板内容
	 * 
	 * @param generatorTemplate 模板信息对象
	 * @return
	 */
	@PutMapping(path = "/updateGeneratorTemplateContent")
	public ActionResult updateGeneratorTemplateContent(@Validated(UpdateValidator.class) @RequestBody GeneratorTemplate generatorTemplate) {
		generatorTemplateService.updateGeneratorTemplateContent(generatorTemplate);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除模板信息
	 * 
	 * @param type 模板类型
	 * @return
	 */
	@PostMapping(path = "/deleteGeneratorTemplate")
	public ActionResult deleteGeneratorTemplate(@RequestParam(name = "type", required = true) String[] type) {
		generatorTemplateService.deleteGeneratorTemplate(type);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出模板信息
	 * 
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportGeneratorTemplate")
	public void exportGeneratorTemplate(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			List<String> headList = Arrays.asList("ID", "模板类型", "模板类型名称", "模板项目", "模板项目名称", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = generatorTemplateService.queryGeneratorTemplateForExcel(paramMap);
			ExcelUtils.exportExcel(headList, dataList, "模板管理", response);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

}
