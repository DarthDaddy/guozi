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

import com.chinatechstar.admin.entity.SysOrg;
import com.chinatechstar.admin.service.SysOrgService;
import com.chinatechstar.admin.vo.SysOrgVO;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.utils.ExcelUtils;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;
import org.springframework.web.multipart.MultipartFile;

/**
 * 机构信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/sysorg")
public class SysOrgController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysOrgService sysOrgService;

	/**
	 * 查询机构分页
	 * 
	 * @param sysOrgVO 机构前端参数
	 * @return
	 */
	@GetMapping(path = "/querySysOrg")
	public ListResult<Object> querySysOrg(SysOrgVO sysOrgVO) {
		Map<String, Object> data = sysOrgService.querySysOrg(sysOrgVO.getCurrentPage(), sysOrgVO.getPageSize(), sysOrgVO.getOrgName(), sysOrgVO.getOrgType(),
				sysOrgVO.getOrgDescription(), sysOrgVO.getId());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询机构的树数据
	 * 
	 * @return
	 */
	@GetMapping(path = "/querySysOrgTree")
	public ListResult<Object> querySysOrgTree() {
		LinkedHashMap<String, Object> data = sysOrgService.querySysOrgTree();
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询机构类型的下拉框数据
	 * 
	 * @return
	 */
	@GetMapping(path = "/queryOrgType")
	public ListResult<Object> queryOrgType() {
		LinkedHashMap<String, Object> data = sysOrgService.queryOrgType();
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询机构用户的树数据
	 * 
	 * @param roleId 角色ID
	 * @param assign 是否分配（0是未分配，1是已分配）
	 * @return
	 */
	@GetMapping(path = "/queryOrgUserTree")
	public ListResult<Object> queryOrgUserTree(@RequestParam(name = "roleId", required = true) Long roleId,
			@RequestParam(name = "assign", required = true) Short assign) {
		List<LinkedHashMap<String, Object>> data = sysOrgService.queryOrgUserTree(roleId, assign);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询模型信息的机构用户的树数据
	 * 
	 * @param modelId 模型ID
	 * @param assign  是否分配（0是未分配，1是已分配）
	 * @return
	 */
	@GetMapping(path = "/queryModelOrgUserTree")
	public ListResult<Object> queryModelOrgUserTree(@RequestParam(name = "modelId", required = true) String modelId,
			@RequestParam(name = "assign", required = true) Short assign) {
		List<LinkedHashMap<String, Object>> data = sysOrgService.queryModelOrgUserTree(modelId, assign);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增机构
	 * 
	 * @param sysOrg 机构对象
	 * @return
	 */
	@PostMapping(path = "/addSysOrg")
	public ActionResult addSysOrg(@Validated(InsertValidator.class) @RequestBody SysOrg sysOrg) {
		sysOrgService.insertSysOrg(sysOrg);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑机构
	 * 
	 * @param sysOrg 机构对象
	 * @return
	 */
	@PutMapping(path = "/updateSysOrg")
	public ActionResult updateSysOrg(@Validated(UpdateValidator.class) @RequestBody SysOrg sysOrg) {
		sysOrgService.updateSysOrg(sysOrg);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除机构
	 * 
	 * @param id 机构ID
	 * @return
	 */
	@PostMapping(path = "/deleteSysOrg")
	public ActionResult deleteSysOrg(@RequestParam(name = "id", required = true) Long[] id) {
		sysOrgService.deleteSysOrg(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出机构到Excel
	 * 
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportSysOrg")
	public void exportSysOrg(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			if (paramMap.get("isTemplate").equals("1")) { // 1为模板，0不为模板
				List<String> headList = Arrays.asList("机构名称", "机构类型", "机构描述", "排序", "上级机构ID");
				ExcelUtils.exportExcel(headList, null, "机构管理", response);
			} else {
				List<String> headList = Arrays.asList("ID", "机构名称", "机构类型", "机构类型名称", "机构描述", "排序", "上级机构ID", "创建时间");
				List<LinkedHashMap<String, Object>> dataList = sysOrgService.querySysOrgForExcel(paramMap);
				ExcelUtils.exportExcel(headList, dataList, "机构管理", response);
			}
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

	/**
	 * 导入机构
	 *
	 * @param file 文件资源
	 * @return
	 */
	@PostMapping(value = "/importSysOrg", consumes = {"multipart/form-data"})
	public ActionResult importSysOrg(@RequestParam(name = "file", required = true) MultipartFile file) {
		sysOrgService.importSysOrg(file);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出机构到Word
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportWordSysOrg")
	public void exportWordSysOrg(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		exportCommonSysOrg(response, paramMap, "Word");
	}

	/**
	 * 根据查询条件导出机构到PDF
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportPDFSysOrg")
	public void exportPDFSysOrg(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		exportCommonSysOrg(response, paramMap, "PDF");
	}

	/**
	 * 根据查询条件导出机构到Word或PDF
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 * @param flag     Word或PDF
	 */
	private void exportCommonSysOrg(HttpServletResponse response, @RequestParam Map<String, Object> paramMap, String flag) {
		try {
			List<String> headList = Arrays.asList("机构名称", "机构类型名称", "机构描述", "排序", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = sysOrgService.querySysOrgForExcel(paramMap);
			dataList.forEach(map -> {
				map.entrySet().removeIf(entry -> ("id".equals(entry.getKey()) || "orgType".equals(entry.getKey()) || "parentId".equals(entry.getKey())));
			});
			if (flag == "Word") {
				WordUtils.exportWord(headList, dataList, "机构管理", response);
			} else if (flag == "PDF") {
				PDFUtils.exportPDF(headList, dataList, "机构管理", response);
			}
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

}
