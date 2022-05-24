package com.chinatechstar.admin.vo;

import java.io.Serializable;
import java.util.List;

import com.chinatechstar.component.commons.vo.CommonVO;
import com.chinatechstar.file.vo.FileVO;

/**
 * 租户信息的参数类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class SysTenantVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = 5949020473992423638L;
	private Long id;
	private String tenantCode; // 租户编码
	private String tenantName;// 租户名称
	private String tenantContact;// 联系人
	private String tenantEmail;// 联系邮箱
	private String tenantTel;// 联系电话
	private  String tenantLogo;//头像
	private String tenantSite;//注册地址
	private String 	tenantLand;//生产经营所在地
	private String tenantType;//行业类别
	private String areaCode;//行政区划代码
	private String tenantPlan;//平面图
	private String tenantRain;//雨污网管图
	private String tenantArea;//企业位置
	private List<FileVO> fileList;

	public List<FileVO> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileVO> fileList) {
		this.fileList = fileList;
	}

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

}
