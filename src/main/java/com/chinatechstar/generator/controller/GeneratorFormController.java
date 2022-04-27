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
import com.chinatechstar.generator.entity.GeneratorForm;
import com.chinatechstar.generator.service.GeneratorFormService;
import com.chinatechstar.generator.vo.GeneratorFormVO;

/**
 * 表单信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/generatorform")
public class GeneratorFormController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GeneratorFormService generatorFormService;

	/**
	 * 查询表单分页
	 * 
	 * @param generatorFormVO 表单功能前端参数
	 * @return
	 */
	@GetMapping(path = "/queryGeneratorForm")
	public ListResult<Object> queryGeneratorForm(GeneratorFormVO generatorFormVO) {
		Map<String, Object> data = generatorFormService.queryGeneratorForm(generatorFormVO.getCurrentPage(), generatorFormVO.getPageSize(),
				generatorFormVO.getFormDescription(), generatorFormVO.getFormContent(), generatorFormVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增表单
	 * 
	 * @param generatorForm 表单对象
	 * @return
	 */
	@PostMapping(path = "/addGeneratorForm")
	public ActionResult addGeneratorForm(@Validated(InsertValidator.class) @RequestBody GeneratorForm generatorForm) {
		generatorFormService.insertGeneratorForm(generatorForm);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑表单
	 * 
	 * @param generatorForm 表单对象
	 * @return
	 */
	@PutMapping(path = "/updateGeneratorForm")
	public ActionResult updateGeneratorForm(@Validated(UpdateValidator.class) @RequestBody GeneratorForm generatorForm) {
		generatorFormService.updateGeneratorForm(generatorForm);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除表单
	 * 
	 * @param id 表单ID
	 * @return
	 */
	@PostMapping(path = "/deleteGeneratorForm")
	public ActionResult deleteGeneratorForm(@RequestParam(name = "id", required = true) Long[] id) {
		generatorFormService.deleteGeneratorForm(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出表单
	 * 
	 * @param response 响应对象
	 * @param paramMap 表单Map
	 */
	@PostMapping(path = "/exportGeneratorForm")
	public void exportGeneratorForm(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			List<String> headList = Arrays.asList("ID", "表单描述", "表单内容", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = generatorFormService.queryGeneratorFormForExcel(paramMap);
			ExcelUtils.exportExcel(headList, dataList, "表单管理", response);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

}
