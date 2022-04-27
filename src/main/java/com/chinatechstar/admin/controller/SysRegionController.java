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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chinatechstar.admin.entity.SysRegion;
import com.chinatechstar.admin.service.SysRegionService;
import com.chinatechstar.admin.vo.SysRegionVO;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.utils.ExcelUtils;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;
import org.springframework.web.multipart.MultipartFile;

/**
 * 区域信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/sysregion")
public class SysRegionController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysRegionService sysRegionService;

	/**
	 * 查询区域分页
	 * 
	 * @param sysRegionVO 区域前端参数
	 * @return
	 */
	@GetMapping(path = "/querySysRegion")
	public ListResult<Object> querySysRegion(SysRegionVO sysRegionVO) {
		Map<String, Object> data = sysRegionService.querySysRegion(sysRegionVO.getCurrentPage(), sysRegionVO.getPageSize(), sysRegionVO.getRegionName(),
				sysRegionVO.getRegionCode(), sysRegionVO.getRegionType());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询区域的树数据
	 * 
	 * @return
	 */
	@GetMapping(path = "/querySysRegionTree")
	public ListResult<Object> querySysRegionTree() {
		LinkedHashMap<String, Object> data = sysRegionService.querySysRegionTree();
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询区域类型的下拉框数据
	 * 
	 * @return
	 */
	@GetMapping(path = "/queryRegionType")
	public ListResult<Object> queryRegionType() {
		LinkedHashMap<String, Object> data = sysRegionService.queryRegionType();
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询全部省份数据
	 * 
	 * @return
	 */
	@GetMapping(path = "/queryProvince")
	public ListResult<Object> queryProvince() {
		LinkedHashMap<String, Object> data = sysRegionService.queryProvince();
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 根据省份代码查询对应地市数据
	 * 
	 * @return
	 */
	@GetMapping(path = "/queryCity/{province}")
	public ListResult<Object> queryCity(@PathVariable String province) {
		LinkedHashMap<String, Object> data = sysRegionService.queryCity(province);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增区域
	 * 
	 * @param sysRegion 区域对象
	 * @return
	 */
	@PostMapping(path = "/addSysRegion")
	public ActionResult addSysRegion(@Validated(InsertValidator.class) @RequestBody SysRegion sysRegion) {
		sysRegionService.insertSysRegion(sysRegion);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑区域
	 * 
	 * @param sysRegion 区域对象
	 * @return
	 */
	@PutMapping(path = "/updateSysRegion")
	public ActionResult updateSysRegion(@Validated(UpdateValidator.class) @RequestBody SysRegion sysRegion) {
		sysRegionService.updateSysRegion(sysRegion);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除区域
	 * 
	 * @param regionCode 区域代码
	 * @return
	 */
	@PostMapping(path = "/deleteSysRegion")
	public ActionResult deleteSysRegion(@RequestParam(name = "regionCode", required = true) String[] regionCode) {
		sysRegionService.deleteSysRegion(regionCode);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出区域到Excel
	 * 
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportSysRegion")
	public void exportSysRegion(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			if (paramMap.get("isTemplate").equals("1")) { // 1为模板，0不为模板
				List<String> headList = Arrays.asList("区域名称", "区域代码", "区域类型", "上级区域代码");
				ExcelUtils.exportExcel(headList, null, "区域管理", response);
			} else {
				List<String> headList = Arrays.asList("ID", "区域名称", "区域代码", "区域类型", "区域类型名称", "上级区域代码", "上级区域ID", "创建时间");
				List<LinkedHashMap<String, Object>> dataList = sysRegionService.querySysRegionForExcel(paramMap);
				ExcelUtils.exportExcel(headList, dataList, "区域管理", response);
			}
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

	/**
	 * 导入区域
	 *
	 * @param file 文件资源
	 * @return
	 */
	@PostMapping(value = "/importSysRegion", consumes = {"multipart/form-data"})
	public ActionResult importSysRegion(@RequestParam(name = "file", required = true) MultipartFile file) {
		sysRegionService.importSysRegion(file);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出区域到Word
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportWordSysRegion")
	public void exportWordSysRegion(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		exportCommonSysRegion(response, paramMap, "Word");
	}

	/**
	 * 根据查询条件导出区域到PDF
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportPDFSysRegion")
	public void exportPDFSysRegion(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		exportCommonSysRegion(response, paramMap, "PDF");
	}

	/**
	 * 根据查询条件导出区域到Word或PDF
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 * @param flag     Word或PDF
	 */
	private void exportCommonSysRegion(HttpServletResponse response, @RequestParam Map<String, Object> paramMap, String flag) {
		try {
			List<String> headList = Arrays.asList("区域名称", "区域代码", "区域类型名称", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = sysRegionService.querySysRegionForExcel(paramMap);
			dataList.forEach(map -> {
				map.entrySet().removeIf(entry -> ("id".equals(entry.getKey()) || "regionType".equals(entry.getKey()) || "parentRegionCode".equals(entry.getKey()) || "parentId".equals(entry.getKey())));
			});
			if (flag == "Word") {
				WordUtils.exportWord(headList, dataList, "区域管理", response);
			} else if (flag == "PDF") {
				PDFUtils.exportPDF(headList, dataList, "区域管理", response);
			}
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

}
