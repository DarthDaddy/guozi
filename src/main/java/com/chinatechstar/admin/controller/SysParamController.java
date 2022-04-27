package com.chinatechstar.admin.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.chinatechstar.component.commons.utils.PDFUtils;
import com.chinatechstar.component.commons.utils.WordUtils;
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

import com.chinatechstar.admin.entity.SysParam;
import com.chinatechstar.admin.service.SysParamService;
import com.chinatechstar.admin.vo.SysParamVO;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.utils.ExcelUtils;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;
import org.springframework.web.multipart.MultipartFile;

/**
 * 参数信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/sysparam")
public class SysParamController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysParamService sysParamService;

	/**
	 * 查询参数分页
	 * 
	 * @param sysParamVO 参数功能前端参数
	 * @return
	 */
	@GetMapping(path = "/querySysParam")
	public ListResult<Object> querySysParam(SysParamVO sysParamVO) {
		Map<String, Object> data = sysParamService.querySysParam(sysParamVO.getCurrentPage(), sysParamVO.getPageSize(), sysParamVO.getParamName(),
				sysParamVO.getParamKey(), sysParamVO.getParamValue(), sysParamVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增参数
	 * 
	 * @param sysParam 参数对象
	 * @return
	 */
	@PostMapping(path = "/addSysParam")
	public ActionResult addSysParam(@Validated(InsertValidator.class) @RequestBody SysParam sysParam) {
		sysParamService.insertSysParam(sysParam);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑参数
	 * 
	 * @param sysParam 参数对象
	 * @return
	 */
	@PutMapping(path = "/updateSysParam")
	public ActionResult updateSysParam(@Validated(UpdateValidator.class) @RequestBody SysParam sysParam) {
		sysParamService.updateSysParam(sysParam);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除参数
	 * 
	 * @param id 参数ID
	 * @return
	 */
	@PostMapping(path = "/deleteSysParam")
	public ActionResult deleteSysParam(@RequestParam(name = "id", required = true) Long[] id) {
		sysParamService.deleteSysParam(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出参数到Excel
	 * 
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportSysParam")
	public void exportSysParam(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			if (paramMap.get("isTemplate").equals("1")) { // 1为模板，0不为模板
				List<String> headList = Arrays.asList("参数名称", "参数键名", "参数键值");
				ExcelUtils.exportExcel(headList, null, "参数管理", response);
			} else {
				List<String> headList = Arrays.asList("ID", "参数名称", "参数键名", "参数键值", "创建时间");
				List<LinkedHashMap<String, Object>> dataList = sysParamService.querySysParamForExcel(paramMap);
				ExcelUtils.exportExcel(headList, dataList, "参数管理", response);
			}
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

	/**
	 * 导入参数
	 *
	 * @param file 文件资源
	 * @return
	 */
	@PostMapping(value = "/importSysParam", consumes = {"multipart/form-data"})
	public ActionResult importSysParam(@RequestParam(name = "file", required = true) MultipartFile file) {
		sysParamService.importSysParam(file);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出参数到Word
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportWordSysParam")
	public void exportWordSysParam(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		exportCommonSysParam(response, paramMap, "Word");
	}

	/**
	 * 根据查询条件导出参数到PDF
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportPDFSysParam")
	public void exportPDFSysParam(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		exportCommonSysParam(response, paramMap, "PDF");
	}

	/**
	 * 根据查询条件导出参数到Word或PDF
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 * @param flag     Word或PDF
	 */
	private void exportCommonSysParam(HttpServletResponse response, @RequestParam Map<String, Object> paramMap, String flag) {
		try {
			List<String> headList = Arrays.asList("参数名称", "参数键名", "参数键值", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = sysParamService.querySysParamForExcel(paramMap);
			dataList.forEach(map -> {
				map.entrySet().removeIf(entry -> ("id".equals(entry.getKey())));
			});
			if (flag == "Word") {
				WordUtils.exportWord(headList, dataList, "参数管理", response);
			} else if (flag == "PDF") {
				PDFUtils.exportPDF(headList, dataList, "参数管理", response);
			}
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

}
