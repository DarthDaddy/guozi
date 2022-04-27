package com.chinatechstar.activiti.entity;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.chinatechstar.component.commons.validator.UpdateValidator;

/**
 * 模型信息的实体类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class ModelEntity implements Serializable {

	private static final long serialVersionUID = 6103596578742231080L;
	@NotBlank(groups = { UpdateValidator.class })
	private String modelId;// 模型ID
	@NotBlank(groups = { UpdateValidator.class })
	private String name;// 模型名称
	@NotBlank(groups = { UpdateValidator.class })
	private String category;// 模型类别
	@NotBlank(groups = { UpdateValidator.class })
	private String orgId;// 机构ID
	private String menuCode;// 菜单编码
	private String description;// 模型描述
	private String[] referGroupId;// 可引用机构ID
	private String[][] userId;// 可引用人ID

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getReferGroupId() {
		return referGroupId;
	}

	public void setReferGroupId(String[] referGroupId) {
		this.referGroupId = referGroupId;
	}

	public String[][] getUserId() {
		return userId;
	}

	public void setUserId(String[][] userId) {
		this.userId = userId;
	}

}
