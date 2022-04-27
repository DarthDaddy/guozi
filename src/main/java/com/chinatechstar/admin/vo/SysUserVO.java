package com.chinatechstar.admin.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.vo.CommonVO;

/**
 * 用户信息的参数类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class SysUserVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = -228357463766836975L;
	private String username;
	private String status;
	private Long[] roleId;// 角色ID
	@NotNull(groups = { InsertValidator.class })
	private Long userId;// 用户ID
	private String postCode;// 岗位编码
	private Long orgId;// 机构ID
	private String nickname;// 昵称
	private String mobile;// 手机号

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long[] getRoleId() {
		return roleId;
	}

	public void setRoleId(Long[] roleId) {
		this.roleId = roleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
