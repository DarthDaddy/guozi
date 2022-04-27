package com.chinatechstar.admin.vo;

import java.io.Serializable;

import com.chinatechstar.component.commons.vo.CommonVO;

/**
 * 接口信息的参数类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class SysUrlVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = -1887383111996273369L;
	private String url;// URL
	private String description;// 描述
	private String menuCode; // 菜单编码
	private Long[] roleId;// 角色ID

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public Long[] getRoleId() {
		return roleId;
	}

	public void setRoleId(Long[] roleId) {
		this.roleId = roleId;
	}

}
