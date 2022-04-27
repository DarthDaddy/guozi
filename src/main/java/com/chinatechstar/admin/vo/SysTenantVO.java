package com.chinatechstar.admin.vo;

import java.io.Serializable;

import com.chinatechstar.component.commons.vo.CommonVO;

/**
 * 租户信息的参数类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class SysTenantVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = 5949020473992423638L;
	private String tenantCode; // 租户编码
	private String tenantName;// 租户名称
	private String tenantContact;// 联系人
	private String tenantEmail;// 联系邮箱
	private String tenantTel;// 联系电话

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getTenantContact() {
		return tenantContact;
	}

	public void setTenantContact(String tenantContact) {
		this.tenantContact = tenantContact;
	}

	public String getTenantEmail() {
		return tenantEmail;
	}

	public void setTenantEmail(String tenantEmail) {
		this.tenantEmail = tenantEmail;
	}

	public String getTenantTel() {
		return tenantTel;
	}

	public void setTenantTel(String tenantTel) {
		this.tenantTel = tenantTel;
	}

}
