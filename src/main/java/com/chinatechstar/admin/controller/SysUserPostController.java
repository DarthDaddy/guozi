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

import com.chinatechstar.admin.entity.SysUserPost;
import com.chinatechstar.admin.service.SysUserPostService;
import com.chinatechstar.admin.vo.SysUserPostVO;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.utils.ExcelUtils;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户与岗位关联信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/sysuserpost")
public class SysUserPostController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysUserPostService sysUserPostService;

	/**
	 * 查询用户与岗位关联信息分页
	 * 
	 * @param sysUserPostVO 用户与岗位关联信息前端参数
	 * @return
	 */
	@GetMapping(path = "/querySysUserPost")
	public ListResult<Object> querySysUserPost(SysUserPostVO sysUserPostVO) {
		Map<String, Object> data = sysUserPostService.querySysUserPost(sysUserPostVO.getCurrentPage(), sysUserPostVO.getPageSize(), sysUserPostVO.getUserId(),
				sysUserPostVO.getPostCode(), sysUserPostVO.getPostName(), sysUserPostVO.getPostType(), sysUserPostVO.getStatus(),
				sysUserPostVO.getPostDescription(), sysUserPostVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询用户与岗位关联信息的下拉框数据
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	@GetMapping(path = "/querySysUserPostTree")
	public ListResult<Object> querySysUserPostTree(@RequestParam(name = "userId", required = true) String userId) {
		List<LinkedHashMap<String, Object>> data = sysUserPostService.querySysUserPostTree(userId);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增用户与岗位关联信息
	 * 
	 * @param sysUserPost 用户与岗位关联对象
	 * @return
	 */
	@PostMapping(path = "/addSysUserPost")
	public ActionResult addSysUserPost(@Validated(InsertValidator.class) @RequestBody SysUserPost sysUserPost) {
		sysUserPostService.insertSysUserPost(sysUserPost);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑用户与岗位关联信息
	 * 
	 * @param sysUserPost 用户与岗位关联对象
	 * @return
	 */
	@PutMapping(path = "/updateSysUserPost")
	public ActionResult updateSysUserPost(@Validated(UpdateValidator.class) @RequestBody SysUserPost sysUserPost) {
		sysUserPostService.updateSysUserPost(sysUserPost);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除用户与岗位关联信息
	 * 
	 * @param id 用户与岗位关联ID
	 * @return
	 */
	@PostMapping(path = "/deleteSysUserPost")
	public ActionResult deleteSysUserPost(@RequestParam(name = "id", required = true) Long[] id) {
		sysUserPostService.deleteSysUserPost(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出用户与岗位关联信息到Excel
	 * 
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportSysUserPost")
	public void exportSysUserPost(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			if (paramMap.get("isTemplate").equals("1")) { // 1为模板，0不为模板
				List<String> headList = Arrays.asList("用户ID", "岗位编码", "岗位描述", "岗位类型", "状态");
				ExcelUtils.exportExcel(headList, null, "用户与岗位关联信息", response);
			} else {
				List<String> headList = Arrays.asList("ID", "用户ID", "岗位编码", "岗位上下级名称", "岗位描述", "创建时间");
				List<LinkedHashMap<String, Object>> dataList = sysUserPostService.querySysUserPostForExcel(paramMap);
				dataList.forEach(map -> {
					map.entrySet().removeIf(entry -> ("postType".equals(entry.getKey()) || "status".equals(entry.getKey())));
				});
				ExcelUtils.exportExcel(headList, dataList, "用户与岗位关联信息", response);
			}
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

	/**
	 * 导入用户与岗位关联信息
	 *
	 * @param file 文件资源
	 * @return
	 */
	@PostMapping(value = "/importSysUserPost", consumes = {"multipart/form-data"})
	public ActionResult importSysUserPost(@RequestParam(name = "file", required = true) MultipartFile file) {
		sysUserPostService.importSysUserPost(file);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出用户与岗位关联信息到Word
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportWordSysUserPost")
	public void exportWordSysUserPost(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		exportCommonSysUserPost(response, paramMap, "Word");
	}

	/**
	 * 根据查询条件导出用户与岗位关联信息到PDF
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportPDFSysUserPost")
	public void exportPDFSysUserPost(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		exportCommonSysUserPost(response, paramMap, "PDF");
	}

	/**
	 * 根据查询条件导出用户与岗位关联信息到Word或PDF
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 * @param flag     Word或PDF
	 */
	private void exportCommonSysUserPost(HttpServletResponse response, @RequestParam Map<String, Object> paramMap, String flag) {
		try {
			List<String> headList = Arrays.asList("ID", "岗位编码", "岗位上下级名称", "岗位描述", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = sysUserPostService.querySysUserPostForExcel(paramMap);
			dataList.forEach(map -> {
				map.entrySet().removeIf(entry -> ("userId".equals(entry.getKey()) || "postType".equals(entry.getKey()) || "status".equals(entry.getKey())));
			});
			if (flag == "Word") {
				WordUtils.exportWord(headList, dataList, "用户与岗位关联信息", response);
			} else if (flag == "PDF") {
				PDFUtils.exportPDF(headList, dataList, "用户与岗位关联信息", response);
			}
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

}
