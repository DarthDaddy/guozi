package com.chinatechstar.notification.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.chinatechstar.component.commons.entity.TimeEntity;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * 消息通知的实体类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class Notification extends TimeEntity implements Serializable {

	private static final long serialVersionUID = 1841178213058234759L;
	@NotNull(groups = { UpdateValidator.class })
	private Long id;// ID
	@NotBlank(groups = { InsertValidator.class, UpdateValidator.class })
	@Size(max = 100, min = 1, groups = { InsertValidator.class, UpdateValidator.class })
	private String title;// 标题
	@NotBlank(groups = { InsertValidator.class, UpdateValidator.class })
	private String content;// 内容
	@NotBlank(groups = { InsertValidator.class, UpdateValidator.class })
	@Size(max = 32, min = 1, groups = { InsertValidator.class, UpdateValidator.class })
	private String type;// 类型
	private String priority;// 优先级
	@NotBlank(groups = { InsertValidator.class })
	@Size(max = 64, min = 3, groups = { InsertValidator.class })
	private String publisher;// 发布者
	private Date startTime; // 开始时间
	private Date endTime; // 结束时间
	private String[] username;// 发布对象的用户名
	private String tenantCode;// 租户编码

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
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

	public String[] getUsername() {
		return username;
	}

	public void setUsername(String[] username) {
		this.username = username;
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
		Notification item = (Notification) o;
		return Objects.equal(id, item.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", id).add("title", title).add("content", content).add("type", type).add("priority", priority)
				.add("publisher", publisher).add("startTime", startTime).add("endTime", endTime).add("tenantCode", tenantCode)
				.add("createTime", super.getCreateTime()).toString();
	}

}
