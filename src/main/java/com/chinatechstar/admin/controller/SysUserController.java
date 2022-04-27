package com.chinatechstar.admin.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chinatechstar.admin.entity.SysUser;
import com.chinatechstar.admin.service.SysUserService;
import com.chinatechstar.admin.vo.SysUserVO;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.utils.ExcelUtils;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Api(value = "用户信息的控制层", tags = { "用户信息" })
@RestController
@RequestMapping("/sysuser")
public class SysUserController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SessionRegistry sessionRegistry;

	/**
	 * 注册一个新的会话
	 * 
	 * @param request 请求对象
	 * @return
	 */
	@PostMapping(path = "/registerNewSession")
	public ActionResult registerNewSession(HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		sessionRegistry.registerNewSession(request.getSession().getId(), authentication.getPrincipal());
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 查询在线用户分页
	 * 
	 * @param sysUserVO 用户前端参数
	 * @return
	 */
	@GetMapping(path = "/queryOnlineSysUser")
	public ListResult<Object> queryOnlineSysUser(SysUserVO sysUserVO) {
		Map<String, Object> data = sysUserService.queryOnlineSysUser(sysUserVO.getCurrentPage(), sysUserVO.getPageSize(), sysUserVO.getUsername(),
				sysUserVO.getNickname(), sysUserVO.getMobile(), sysUserVO.getRoleId(), sysUserVO.getOrgId(), sysUserVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询用户分页
	 * 
	 * @param sysUserVO 用户前端参数
	 * @return
	 */
	@ApiOperation(value = "查询用户分页", tags = { "用户信息" })
	@GetMapping(path = "/querySysUser")
	public ListResult<Object> querySysUser(SysUserVO sysUserVO) {
		Map<String, Object> data = sysUserService.querySysUser(sysUserVO.getCurrentPage(), sysUserVO.getPageSize(), sysUserVO.getUsername(),
				sysUserVO.getStatus(), sysUserVO.getOrgId(), sysUserVO.getRoleId(), sysUserVO.getNickname(), sysUserVO.getMobile(), sysUserVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询用户名的下拉框数据
	 * 
	 * @return
	 */
	@GetMapping(path = "/queryUsername")
	public ListResult<Object> queryUsername() {
		List<LinkedHashMap<String, Object>> data = sysUserService.queryUsername(null);// 查询全部用户名
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增用户
	 * 
	 * @param sysUser 用户对象
	 * @return
	 */
	@PostMapping(path = "/addSysUser")
	public ActionResult addSysUser(@Validated(InsertValidator.class) @RequestBody SysUser sysUser) {
		sysUserService.insertSysUser(sysUser);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 将对应的角色授予用户
	 * 
	 * @param sysUserVO 用户前端参数
	 * @return
	 */
	@PostMapping(path = "/authorizeRoleToUser")
	public ActionResult authorizeRoleToUser(@Validated(InsertValidator.class) @RequestBody SysUserVO sysUserVO) {
		sysUserService.insertRoleIdUserId(sysUserVO.getRoleId(), sysUserVO.getUserId(), sysUserVO.getPostCode());
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑用户
	 * 
	 * @param sysUser 用户对象
	 * @return
	 */
	@PutMapping(path = "/updateSysUser")
	public ActionResult updateSysUser(@Validated(UpdateValidator.class) @RequestBody SysUser sysUser) {
		sysUserService.updateSysUser(sysUser);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除用户
	 * 
	 * @param id 用户ID
	 * @return
	 */
	@PostMapping(path = "/deleteSysUser")
	public ActionResult deleteSysUser(@RequestParam(name = "id", required = true) Long[] id) {
		sysUserService.deleteSysUser(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出用户
	 * 
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportSysUser")
	public void exportSysUser(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			List<String> headList = Arrays.asList("ID", "用户名", "昵称", "邮箱", "手机号", "手机号国家代码", "角色ID", "角色名称", "机构ID", "机构名称", "状态", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = sysUserService.querySysUserForExcel(paramMap);
			ExcelUtils.exportExcel(headList, dataList, "用户管理", response);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

	/**
	 * 根据用户ID查询用户名的数据列表
	 * 
	 * @param sysUserId 用户ID
	 * @return
	 */
	@GetMapping(value = "/queryUsernameBySysUserId")
	public List<LinkedHashMap<String, Object>> queryUsernameBySysUserId(@RequestParam(name = "sysUserId", required = false) Long[] sysUserId) {
		return sysUserService.queryUsername(sysUserId);
	}

	/**
	 * 根据用户名查询用户ID的数据列表
	 * 
	 * @return
	 */
	@GetMapping(value = "/querySysUserIdByUsername")
	public List<Long> querySysUserIdByUsername(@RequestParam(name = "username", required = false) String[] username) {
		return sysUserService.querySysUserId(username);
	}

}
