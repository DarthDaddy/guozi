package com.chinatechstar.activiti.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONArray;
import com.chinatechstar.component.commons.validator.InsertValidator;

/**
 * 请假信息的实体类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class Leave implements Serializable {

	private static final long serialVersionUID = 7706521973024688277L;
	@NotBlank(groups = { InsertValidator.class })
	private String processDefinitionId;// 流程定义ID
	@NotBlank(groups = { InsertValidator.class })
	private String candidate;// 审批人员
	@NotNull(groups = { InsertValidator.class })
	private Date startTime;// 开始时间
	@NotNull(groups = { InsertValidator.class })
	private Date endTime;// 结束时间
	@NotBlank(groups = { InsertValidator.class })
	private String description;// 备注
	private JSONArray customForm;// 自定义字段

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getCandidate() {
		return candidate;
	}

	public void setCandidate(String candidate) {
		this.candidate = candidate;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public JSONArray getCustomForm() {
		return customForm;
	}

	public void setCustomForm(JSONArray customForm) {
		this.customForm = customForm;
	}

}
