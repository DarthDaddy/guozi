package com.chinatechstar.admin.vo;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.vo.CommonVO;

/**
 * 角色信息的参数类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class SysRoleVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = 4928960051913469176L;
	private String roleName;// 角色名称
	@NotNull(groups = { InsertValidator.class })
	private Long roleId;// 角色ID
	@NotEmpty(groups = { InsertValidator.class })
	private String[][] userId;// 用户ID
	private String roleCode;// 角色编码
	private String roleDescription;// 角色描述

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String[][] getUserId() {
		return userId;
	}

	public void setUserId(String[][] userId) {
		this.userId = userId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

}
