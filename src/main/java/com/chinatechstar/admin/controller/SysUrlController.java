package com.chinatechstar.admin.controller;

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

import com.chinatechstar.admin.entity.SysUrl;
import com.chinatechstar.admin.service.SysUrlService;
import com.chinatechstar.admin.vo.SysUrlVO;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.utils.ExcelUtils;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;

/**
 * 接口信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/sysurl")
public class SysUrlController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysUrlService sysUrlService;

	/**
	 * 查询接口分页
	 * 
	 * @param sysUrlVO 接口前端参数
	 * @return
	 */
	@GetMapping(path = "/querySysUrl")
	public ListResult<Object> querySysUrl(SysUrlVO sysUrlVO) {
		Map<String, Object> data = sysUrlService.querySysUrl(sysUrlVO.getCurrentPage(), sysUrlVO.getPageSize(), sysUrlVO.getUrl(), sysUrlVO.getDescription(),
				sysUrlVO.getMenuCode(), sysUrlVO.getRoleId(), sysUrlVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 根据接口ID查询对应的角色ID
	 * 
	 * @param urlId 接口ID
	 * @return
	 */
	@GetMapping(path = "/queryRoleIdByUrlId")
	public ListResult<Object> queryRoleIdByUrlId(@RequestParam(name = "urlId", required = true) Long urlId) {
		List<String> data = sysUrlService.queryRoleIdByUrlId(urlId);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 根据角色ID查询对应的接口ID
	 * 
	 * @param roleId 角色ID
	 * @return
	 */
	@GetMapping(value = "/queryUrlIdByRoleId")
	public List<String> queryUrlIdByRoleId(@RequestParam(name = "roleId", required = true) Long roleId) {
		return sysUrlService.queryUrlIdByRoleId(roleId);
	}

	/**
	 * 将接口授权给角色
	 * 
	 * @param urlId  接口ID
	 * @param roleId 角色ID
	 * @return
	 */
	@PostMapping(path = "/authorizeRoleToUrl")
	public ActionResult authorizeRoleToUrl(@RequestParam(name = "urlId", required = true) Long urlId,
			@RequestParam(name = "roleId", required = true) Long[] roleId) {
		sysUrlService.insertUrlIdRoleId(urlId, roleId);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 将接口授权给角色
	 * 
	 * @param urlId  接口ID
	 * @param roleId 角色ID
	 * @return
	 */
	@PostMapping(path = "/authorizeUrlToRole")
	public ActionResult authorizeUrlToRole(@RequestParam(name = "urlId", required = true) Long[] urlId,
			@RequestParam(name = "roleId", required = true) Long roleId) {
		sysUrlService.insertRoleIdUrlId(urlId, roleId);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 新增接口
	 * 
	 * @param sysUrl 接口对象
	 * @return
	 */
	@PostMapping(path = "/addSysUrl")
	public ActionResult addSysUrl(@Validated(InsertValidator.class) @RequestBody SysUrl sysUrl) {
		sysUrlService.insertSysUrl(sysUrl);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑接口
	 * 
	 * @param sysUrl 接口对象
	 * @return
	 */
	@PutMapping(path = "/updateSysUrl")
	public ActionResult updateSysUrl(@Validated(UpdateValidator.class) @RequestBody SysUrl sysUrl) {
		sysUrlService.updateSysUrl(sysUrl);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除接口
	 * 
	 * @param id 接口ID
	 * @return
	 */
	@PostMapping(path = "/deleteSysUrl")
	public ActionResult deleteSysUrl(@RequestParam(name = "id", required = true) Long[] id) {
		sysUrlService.deleteSysUrl(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出接口
	 * 
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportSysUrl")
	public void exportSysUrl(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			List<String> headList = Arrays.asList("ID", "URL", "描述", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = sysUrlService.querySysUrlForExcel(paramMap);
			ExcelUtils.exportExcel(headList, dataList, "接口权限", response);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

}
