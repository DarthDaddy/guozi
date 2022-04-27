package com.chinatechstar.admin.entity;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.chinatechstar.component.commons.entity.TimeEntity;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * 租户信息的实体类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class SysTenant extends TimeEntity implements Serializable {

	private static final long serialVersionUID = 3763829790282234531L;
	@NotNull(groups = { UpdateValidator.class })
	private Long id;// ID
	@NotBlank(groups = { InsertValidator.class, UpdateValidator.class })
	@Size(max = 64, min = 1, groups = { InsertValidator.class, UpdateValidator.class })
	private String tenantCode;// 租户编码
	@NotBlank(groups = { InsertValidator.class, UpdateValidator.class })
	@Size(max = 64, min = 1, groups = { InsertValidator.class, UpdateValidator.class })
	private String tenantName;// 租户名称
	@Size(max = 64, groups = { InsertValidator.class, UpdateValidator.class })
	private String tenantContact;// 联系人
	@Email(groups = { InsertValidator.class, UpdateValidator.class })
	@Size(max = 100, groups = { InsertValidator.class, UpdateValidator.class })
	private String tenantEmail;// 联系邮箱
	@Size(max = 20, groups = { InsertValidator.class, UpdateValidator.class })
	private String tenantTel;// 联系电话
	private  String tenantLogo;//头像
	@Size(max = 20, groups = { InsertValidator.class, UpdateValidator.class })
	private String tenantSite;//注册地址
	@Size(max = 20, groups = { InsertValidator.class, UpdateValidator.class })
	private String 	tenantLand;//生产经营所在地
	@Size(max = 20, groups = { InsertValidator.class, UpdateValidator.class })
	private String tenantType;//行业类别
	@Size(max = 20, groups = { InsertValidator.class, UpdateValidator.class })
	private String areaCode;//行政区划代码
	private String tenantPlan;//平面图
	private String tenantRain;//雨污网管图
	private String tenantArea;//企业位置

	public String getTenantPlan() {
		return tenantPlan;
	}

	public void setTenantPlan(String tenantPlan) {
		this.tenantPlan = tenantPlan;
	}

	public String getTenantRain() {
		return tenantRain;
	}

	public void setTenantRain(String tenantRain) {
		this.tenantRain = tenantRain;
	}

	public String getTenantArea() {
		return tenantArea;
	}

	public void setTenantArea(String tenantArea) {
		this.tenantArea = tenantArea;
	}

	public String getTenantLogo() {
		return tenantLogo;
	}

	public void setTenantLogo(String tenantLogo) {
		this.tenantLogo = tenantLogo;
	}

	public String getTenantSite() {
		return tenantSite;
	}

	public void setTenantSite(String tenantSite) {
		this.tenantSite = tenantSite;
	}

	public String getTenantLand() {
		return tenantLand;
	}

	public void setTenantLand(String tenantLand) {
		this.tenantLand = tenantLand;
	}

	public String getTenantType() {
		return tenantType;
	}

	public void setTenantType(String tenantType) {
		this.tenantType = tenantType;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SysTenant item = (SysTenant) o;
		return Objects.equal(id, item.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", id).add("tenantCode", tenantCode).add("tenantName", tenantName).add("tenantContact", tenantContact)
				.add("tenantEmail", tenantEmail).add("tenantTel", tenantTel).add("createTime", super.getCreateTime()).toString();
	}

}
