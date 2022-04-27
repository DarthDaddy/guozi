package com.chinatechstar.admin.entity;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.chinatechstar.component.commons.entity.TimeEntity;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * 用户与岗位关联信息的实体类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class SysUserPost extends TimeEntity implements Serializable {

	private static final long serialVersionUID = 7489581371123869271L;
	@NotNull(groups = { UpdateValidator.class })
	private Long id;// ID
	@NotNull(groups = { InsertValidator.class })
	private Long userId;// 用户ID
	@NotBlank(groups = { InsertValidator.class, UpdateValidator.class })
	@Size(max = 32, min = 1, groups = { InsertValidator.class, UpdateValidator.class })
	private String postCode;// 岗位编码
	private String postName;// 岗位上下级名称
	@Size(max = 256, groups = { InsertValidator.class, UpdateValidator.class })
	private String postDescription;// 岗位描述
	@NotNull(groups = { InsertValidator.class, UpdateValidator.class })
	private Short postType;// 岗位类型 1:主岗位，2:副岗位
	@NotNull(groups = { InsertValidator.class, UpdateValidator.class })
	private Short status;// 状态 0：隐藏 1：显示
	private String tenantCode;// 租户编码

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getPostDescription() {
		return postDescription;
	}

	public void setPostDescription(String postDescription) {
		this.postDescription = postDescription;
	}

	public Short getPostType() {
		return postType;
	}

	public void setPostType(Short postType) {
		this.postType = postType;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SysUserPost item = (SysUserPost) o;
		return Objects.equal(id, item.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", id).add("userId", userId).add("postCode", postCode).add("postName", postName)
				.add("postDescription", postDescription).add("postType", postType).add("status", status).add("tenantCode", tenantCode)
				.add("createTime", super.getCreateTime()).toString();
	}

}
