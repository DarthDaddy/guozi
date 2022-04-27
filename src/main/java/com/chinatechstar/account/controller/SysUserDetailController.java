package com.chinatechstar.account.controller;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.chinatechstar.account.entity.SysUserDetail;
import com.chinatechstar.account.service.SysUserDetailService;
import com.chinatechstar.component.commons.client.ClientResponse;
import com.chinatechstar.component.commons.client.ResultCode;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateDetailValidator;

/**
 * 用户详细信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/sysuserdetail")
public class SysUserDetailController {

@Autowired
private SysUserDetailService sysUserDetailService;

/**
 * 查询当前用户的授权角色、菜单和按钮
 *
 * @param username 用户名
 * @param mobile   手机号
 * @param parentId 菜单信息的上级ID
 * @param postCode 岗位编码
 * @param userId   用户ID
 * @return
 */
@PostMapping(path = "/queryCurrentAuthority")
public String queryCurrentAuthority(@RequestParam(name = "username", required = false) String username,
@RequestParam(name = "mobile", required = false) String mobile, @RequestParam(name = "parentId", required = false) String parentId,
@RequestParam(name = "postCode", required = false) String postCode, @RequestParam(name = "userId", required = false) Long userId) {
		if (StringUtils.isBlank(username) && StringUtils.isBlank(mobile)) {
		throw new IllegalArgumentException("用户名或手机号不能为空");
		}
		LinkedHashMap<String, Object> authorityMap = new LinkedHashMap<>();
		authorityMap.put("status", "ok");
		authorityMap.put("type", "account");
		List<String> roleCode = sysUserDetailService.getRoleCodeBySysUser(username, mobile);
		authorityMap.put("currentAuthority", roleCode);
		List<LinkedHashMap<String, Object>> menuList = sysUserDetailService.querySysMenuAuthorityTree(username, mobile, parentId, postCode, userId);
		authorityMap.put("menu", menuList);
		List<String> menuButtonList = sysUserDetailService.queryRoleMenuButton(username, mobile);
		authorityMap.put("menuButton", menuButtonList);
		return JSON.toJSONString(authorityMap);
		}

/**
 * 注册用户
 *
 * @param sysUserDetail 用户详细信息
 * @return
 */
@PostMapping(path = "/registerAccount")
public ActionResult registerAccount(@Validated(InsertValidator.class) @RequestBody SysUserDetail sysUserDetail) {
		sysUserDetailService.insertSysUser(sysUserDetail);
		return ResultBuilder.buildActionSuccess();
		}

/**
 * 编辑用户详细信息
 *
 * @param sysUserDetail 用户详细信息
 * @return
 */
@PutMapping(path = "/updateSysUserDetail")
public ActionResult updateSysUserDetail(@Validated(UpdateDetailValidator.class) @RequestBody SysUserDetail sysUserDetail) {
		sysUserDetailService.updateSysUserDetail(sysUserDetail);
		return ResultBuilder.buildActionSuccess();
		}

/**
 * 根据字段编辑用户信息
 *
 * @param fieldValue 修改的字段值
 * @param field      修改的字段
 * @param id         用户ID
 * @return
 */
@PutMapping(path = "/updateSysUserInfo")
public ActionResult updateSysUserInfo(@RequestParam(name = "fieldValue", required = true) String fieldValue,
@RequestParam(name = "field", required = true) String field, @RequestParam(name = "id", required = true) Long id) {
		sysUserDetailService.updateSysUserInfo(fieldValue, field, id);
		return ResultBuilder.buildActionSuccess();
		}

/**
 * 修改用户密码
 *
 * @param password    原密码
 * @param newPassword 新密码
 * @param id          用户ID
 * @return
 */
@PutMapping(path = "/updatePassword")
public ActionResult updatePassword(@RequestParam(name = "password", required = true) String password,
@RequestParam(name = "newPassword", required = true) String newPassword, @RequestParam(name = "id", required = true) Long id) {
		sysUserDetailService.updatePassword(password, newPassword, id);
		return ResultBuilder.buildActionSuccess();
		}

/**
 * 找回密码
 *
 * @param email       邮箱
 * @param charCaptcha 验证码字符
 * @return
 */
@PostMapping(path = "/retrievePassword")
public ActionResult retrievePassword(@RequestParam(name = "email", required = true) String email,
@RequestParam(name = "charCaptcha", required = true) String charCaptcha) {
		sysUserDetailService.retrievePassword(email, charCaptcha);
		return ResultBuilder.buildActionSuccess();
		}

/**
 * 比对验证码
 *
 * @param charCaptcha 验证码字符
 * @return
 */
	/*@PostMapping(path = "/compareCaptcha")
	public ActionResult compareCaptcha(@RequestParam(name = "charCaptcha", required = true) String charCaptcha) {
		sysUserDetailService.compareCaptcha(charCaptcha);
		return ResultBuilder.buildActionSuccess();
	}*/

/**
 * 修改用户头像图片地址
 *
 * @param avatar 用户头像图片地址
 * @param id     用户ID
 */
@PostMapping(value = "/updateAvatar")
public ClientResponse updateAvatar(@RequestParam(name = "avatar", required = true) String avatar, @RequestParam(name = "id", required = true) Long id) {
		try {
		sysUserDetailService.updateSysUserInfo(avatar, "avatar", id);
		return new ClientResponse(ResultCode.SUCCESS);
		} catch (Exception e) {
		return new ClientResponse(ResultCode.FAILURE, e.toString());
		}
		}

		}
