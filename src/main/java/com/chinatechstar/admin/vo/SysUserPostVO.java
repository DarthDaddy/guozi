package com.chinatechstar.admin.vo;

import java.io.Serializable;

import com.chinatechstar.component.commons.vo.CommonVO;

/**
 * 用户与岗位关联信息的参数类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class SysUserPostVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = -3240949575175545937L;
	private Long userId;// 用户ID
	private String postCode;// 岗位编码
	private String postName;// 岗位名称
	private Short postType;// 岗位类型 1:主岗位，2:副岗位
	private Short status;// 状态 0：隐藏 1：显示
	private String postDescription;// 岗位描述

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

	public String getPostDescription() {
		return postDescription;
	}

	public void setPostDescription(String postDescription) {
		this.postDescription = postDescription;
	}

}
