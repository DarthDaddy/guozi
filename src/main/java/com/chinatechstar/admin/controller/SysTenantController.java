package com.chinatechstar.admin.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.chinatechstar.component.commons.utils.CurrentUserUtils;
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

import com.chinatechstar.admin.entity.SysTenant;
import com.chinatechstar.admin.service.SysTenantService;
import com.chinatechstar.admin.vo.SysTenantVO;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.utils.ExcelUtils;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;
import org.springframework.web.multipart.MultipartFile;

/**
 * 租户信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/systenant")
public class SysTenantController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysTenantService sysTenantService;

	@GetMapping("querySysTenantVo")
	public ListResult<Object> querySysTenantVo(){
		List<SysTenantVO> sysTenantVO=sysTenantService.querySysTenantVo();
		return ResultBuilder.buildListSuccess ( sysTenantVO);
	}

	/**
	 * 查询租户分页
	 * 
	 * @param sysTenantVO 租户前端参数
	 * @return
	 */
	@GetMapping(path = "/querySysTenant")
	public ListResult<Object> querySysTenant(SysTenantVO sysTenantVO) {
		Map<String, Object> data = sysTenantService.querySysTenant(sysTenantVO.getCurrentPage(), sysTenantVO.getPageSize(), sysTenantVO.getTenantCode(),
				sysTenantVO.getTenantName(), sysTenantVO.getTenantContact(), sysTenantVO.getTenantEmail(), sysTenantVO.getTenantTel(), sysTenantVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 租户列表
	 * @return
	 */
    @GetMapping("querySysTenantLists")
	public ListResult<Object> querySysTenantLists(){
		List<SysTenant> sysTenantList = sysTenantService.querySysTenantList();
		return ResultBuilder.buildListSuccess ( sysTenantList );
	}


	/**
	 * 根据当前用户获取企业信息
	 * @return
	 */
	@GetMapping("querySysTenantByCurrent")
	public ListResult<Object> querySysTenantByCurrent(){
		System.out.println (CurrentUserUtils.getOAuth2AuthenticationInfo ().get ( "tenantCode" ));
		List<SysTenant> sysTenant=sysTenantService.querySysTenantByCurrent(CurrentUserUtils.getOAuth2AuthenticationInfo ().get ( "tenantCode" ));
		return ResultBuilder.buildListSuccess ( sysTenant );
	}
	/**
	 * 获取租户列表无分页
	 * @return
	 */
	@GetMapping(path = "querySysTenantList")
	public ListResult<Object> querySysTenantList(){
		List<SysTenantVO> sysTenants=sysTenantService.querySysTenants();
		return ResultBuilder.buildListSuccess ( sysTenants );
	}

	/**
	 * 新增租户
	 * 
	 * @param sysTenant 租户对象
	 * @return
	 */
	@PostMapping(path = "/addSysTenant")
	public ActionResult addSysTenant(@Validated(InsertValidator.class) @RequestBody SysTenant sysTenant) {
		sysTenantService.insertSysTenant(sysTenant);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑租户
	 * 
	 * @param sysTenant 租户对象
	 * @return
	 */
	@PutMapping(path = "/updateSysTenant")
	public ActionResult updateSysTenant(@Validated(UpdateValidator.class) @RequestBody SysTenant sysTenant) {
		sysTenantService.updateSysTenant(sysTenant);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除租户
	 * 
	 * @param id 租户ID
	 * @return
	 */
	@PostMapping(path = "/deleteSysTenant")
	public ActionResult deleteSysTenant(@RequestParam(name = "id", required = true) Long[] id) {
		sysTenantService.deleteSysTenant(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出租户到Excel
	 * 
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportSysTenant")
	public void exportSysTenant(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			if (paramMap.get("isTemplate").equals("1")) { // 1为模板，0不为模板
				List<String> headList = Arrays.asList("租户编码", "租户名称", "联系人", "联系邮箱", "联系电话");
				ExcelUtils.exportExcel(headList, null, "租户管理", response);
			} else {
				List<String> headList = Arrays.asList("ID", "租户编码", "租户名称", "联系人", "联系邮箱", "联系电话", "创建时间");
				List<LinkedHashMap<String, Object>> dataList = sysTenantService.querySysTenantForExcel(paramMap);
				ExcelUtils.exportExcel(headList, dataList, "租户管理", response);
			}
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

	/**
	 * 导入租户
	 *
	 * @param file 文件资源
	 * @return
	 */
	@PostMapping(value = "/importSysTenant", consumes = {"multipart/form-data"})
	public ActionResult importSysTenant(@RequestParam(name = "file", required = true) MultipartFile file) {
		sysTenantService.importSysTenant(file);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出租户到Word
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportWordSysTenant")
	public void exportWordSysTenant(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		exportCommonSysTenant(response, paramMap, "Word");
	}

	/**
	 * 根据查询条件导出租户到PDF
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportPDFSysTenant")
	public void exportPDFSysTenant(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		exportCommonSysTenant(response, paramMap, "PDF");
	}

	/**
	 * 根据查询条件导出租户到Word或PDF
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 * @param flag     Word或PDF
	 */
	private void exportCommonSysTenant(HttpServletResponse response, @RequestParam Map<String, Object> paramMap, String flag) {
		try {
			List<String> headList = Arrays.asList("租户编码", "租户名称", "联系人", "联系邮箱", "联系电话", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = sysTenantService.querySysTenantForExcel(paramMap);
			dataList.forEach(map -> {
				map.entrySet().removeIf(entry -> ("id".equals(entry.getKey())));
			});
			if (flag == "Word") {
				WordUtils.exportWord(headList, dataList, "租户管理", response);
			} else if (flag == "PDF") {
				PDFUtils.exportPDF(headList, dataList, "租户管理", response);
			}
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

}
