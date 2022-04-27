package com.chinatechstar.generator.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.chinatechstar.generator.entity.Generator;
import com.chinatechstar.generator.service.GeneratorService;
import com.chinatechstar.generator.vo.GeneratorFieldVO;
import com.chinatechstar.generator.vo.GeneratorTableVO;
import com.chinatechstar.generator.vo.GeneratorVO;

/**
 * 代码信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/generator")
public class GeneratorController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GeneratorService generatorService;

	/**
	 * 查询代码信息分页
	 * 
	 * @param generatorVO 代码前端参数
	 * @return
	 */
	@GetMapping(path = "/queryGenerator")
	public ListResult<Object> queryGenerator(GeneratorVO generatorVO) {
		Map<String, Object> data = generatorService.queryGenerator(generatorVO.getCurrentPage(), generatorVO.getPageSize(), generatorVO.getEntityName(),
				generatorVO.getServiceName(), generatorVO.getPackageName(), generatorVO.getTableName(), generatorVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 根据代码信息ID查询对应的实体字段
	 * 
	 * @param generatorId 代码信息ID
	 * @return
	 */
	@GetMapping(path = "/queryFieldByGeneratorId")
	public ListResult<Object> queryFieldByGeneratorId(@Validated(UpdateValidator.class) GeneratorFieldVO generatorFieldVO) {
		Map<String, Object> data = generatorService.queryFieldByGeneratorId(generatorFieldVO);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询数据表信息分页
	 * 
	 * @param generatorTableVO 数据表前端参数
	 * @return
	 */
	@GetMapping(path = "/queryGeneratorTable")
	public ListResult<Object> queryGeneratorTable(GeneratorTableVO generatorTableVO) {
		Map<String, Object> data = generatorService.queryGeneratorTable(generatorTableVO.getCurrentPage(), generatorTableVO.getPageSize(),
				generatorTableVO.getTableName(), generatorTableVO.getTableComment(), generatorTableVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增代码信息
	 * 
	 * @param generator 代码信息对象
	 * @return
	 */
	@PostMapping(path = "/addGenerator")
	public ActionResult addGenerator(@Validated(InsertValidator.class) @RequestBody Generator generator) {
		generatorService.insertGenerator(generator);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑代码信息
	 * 
	 * @param generator 代码信息对象
	 * @return
	 */
	@PutMapping(path = "/updateGenerator")
	public ActionResult updateGenerator(@Validated(UpdateValidator.class) @RequestBody Generator generator) {
		generatorService.updateGenerator(generator);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除代码信息
	 * 
	 * @param id 代码信息ID
	 * @return
	 */
	@PostMapping(path = "/deleteGenerator")
	public ActionResult deleteGenerator(@RequestParam(name = "id", required = true) Long[] id) {
		generatorService.deleteGenerator(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出代码信息
	 * 
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportGenerator")
	public void exportGenerator(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			List<String> headList = Arrays.asList("ID", "包名", "实体名", "表名", "服务名", "创建时间", "实体字段");
			List<LinkedHashMap<String, Object>> dataList = generatorService.queryGeneratorForExcel(paramMap);
			ExcelUtils.exportExcel(headList, dataList, "代码信息管理", response);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

	/**
	 * 根据查询条件导出数据表信息
	 * 
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportGeneratorTable")
	public void exportGeneratorTable(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			List<String> headList = Arrays.asList("表名", "表注释", "创建时间", "表字段");
			List<LinkedHashMap<String, Object>> dataList = generatorService.queryGeneratorTableForExcel(paramMap);
			ExcelUtils.exportExcel(headList, dataList, "数据表信息", response);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

	/**
	 * 生成正向代码资源
	 * 
	 * @param id   代码信息ID
	 * @param type 模板类型
	 */
	@PostMapping(value = "/generateResource")
	public ResponseEntity<byte[]> generateResource(@RequestParam(name = "id", required = true) Long[] id,
			@RequestParam(name = "type", required = true) String type) {
		ResponseEntity<byte[]> responseEntity = null;
		try {
			byte[] byteArray = generatorService.generateResource(id, type);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseEntity = new ResponseEntity<>(byteArray, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
		return responseEntity;
	}

	/**
	 * 生成反向代码资源
	 * 
	 * @param generatorTableVO 数据表前端参数
	 */
	@PostMapping(value = "/generateTableResource")
	public ResponseEntity<byte[]> generateTableResource(@RequestBody GeneratorTableVO generatorTableVO) {
		ResponseEntity<byte[]> responseEntity = null;
		try {
			byte[] byteArray = generatorService.generateTableResource(generatorTableVO.getTableName(), generatorTableVO.getColumnName(),
					generatorTableVO.getPackageName(), generatorTableVO.getEntityName(), generatorTableVO.getServiceName(), generatorTableVO.getType());
			HttpHeaders responseHeaders = new HttpHeaders();
			responseEntity = new ResponseEntity<>(byteArray, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
		return responseEntity;
	}

}
