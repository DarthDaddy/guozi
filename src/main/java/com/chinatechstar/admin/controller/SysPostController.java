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

import com.chinatechstar.admin.entity.SysPost;
import com.chinatechstar.admin.service.SysPostService;
import com.chinatechstar.admin.vo.SysPostVO;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.utils.ExcelUtils;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;
import org.springframework.web.multipart.MultipartFile;

/**
 * 岗位信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/syspost")
public class SysPostController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysPostService sysPostService;

	/**
	 * 查询岗位分页
	 * 
	 * @param sysPostVO 岗位前端参数
	 * @return
	 */
	@GetMapping(path = "/querySysPost")
	public ListResult<Object> querySysPost(SysPostVO sysPostVO) {
		Map<String, Object> data = sysPostService.querySysPost(sysPostVO.getCurrentPage(), sysPostVO.getPageSize(), sysPostVO.getPostCode(),
				sysPostVO.getPostName());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询岗位的树数据
	 * 
	 * @return
	 */
	@GetMapping(path = "/querySysPostTree")
	public ListResult<Object> querySysPostTree() {
		List<LinkedHashMap<String, Object>> data = sysPostService.querySysPostTree();
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增岗位
	 * 
	 * @param sysPost 岗位对象
	 * @return
	 */
	@PostMapping(path = "/addSysPost")
	public ActionResult addSysPost(@Validated(InsertValidator.class) @RequestBody SysPost sysPost) {
		sysPostService.insertSysPost(sysPost);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑岗位
	 * 
	 * @param sysPost 岗位对象
	 * @return
	 */
	@PutMapping(path = "/updateSysPost")
	public ActionResult updateSysPost(@Validated(UpdateValidator.class) @RequestBody SysPost sysPost) {
		sysPostService.updateSysPost(sysPost);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除岗位
	 * 
	 * @param id 岗位ID
	 * @return
	 */
	@PostMapping(path = "/deleteSysPost")
	public ActionResult deleteSysPost(@RequestParam(name = "id", required = true) Long[] id) {
		sysPostService.deleteSysPost(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出岗位到Excel
	 * 
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportSysPost")
	public void exportSysPost(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			if (paramMap.get("isTemplate").equals("1")) { // 1为模板，0不为模板
				List<String> headList = Arrays.asList("岗位编码", "岗位名称", "岗位排序", "上级岗位ID");
				ExcelUtils.exportExcel(headList, null, "岗位管理", response);
			} else {
				List<String> headList = Arrays.asList("ID", "岗位编码", "岗位名称", "岗位排序", "上级岗位ID", "创建时间");
				List<LinkedHashMap<String, Object>> dataList = sysPostService.querySysPostForExcel(paramMap);
				ExcelUtils.exportExcel(headList, dataList, "岗位管理", response);
			}
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

	/**
	 * 导入岗位
	 *
	 * @param file 文件资源
	 * @return
	 */
	@PostMapping(value = "/importSysPost", consumes = {"multipart/form-data"})
	public ActionResult importSysPost(@RequestParam(name = "file", required = true) MultipartFile file) {
		sysPostService.importSysPost(file);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出岗位到Word
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportWordSysPost")
	public void exportWordSysPost(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		exportCommonSysPost(response, paramMap, "Word");
	}

	/**
	 * 根据查询条件导出岗位到PDF
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportPDFSysPost")
	public void exportPDFSysPost(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		exportCommonSysPost(response, paramMap, "PDF");
	}

	/**
	 * 根据查询条件导出岗位到Word或PDF
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 * @param flag     Word或PDF
	 */
	private void exportCommonSysPost(HttpServletResponse response, @RequestParam Map<String, Object> paramMap, String flag) {
		try {
			List<String> headList = Arrays.asList("岗位编码", "岗位名称", "岗位排序", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = sysPostService.querySysPostForExcel(paramMap);
			dataList.forEach(map -> {
				map.entrySet().removeIf(entry -> ("id".equals(entry.getKey()) || "parentId".equals(entry.getKey())));
			});
			if (flag == "Word") {
				WordUtils.exportWord(headList, dataList, "岗位管理", response);
			} else if (flag == "PDF") {
				PDFUtils.exportPDF(headList, dataList, "岗位管理", response);
			}
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

}
