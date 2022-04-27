package com.chinatechstar.statistics.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 短信发送记录的实体类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Document(collection = "mscode_sms")
public class Sms implements Serializable {

	private static final long serialVersionUID = 2912649019637842287L;
	private String mobile;// 手机号
	private Short status;// 发送状态 0：失败 1：成功
	private LocalDateTime sendTime; // 发送时间

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public LocalDateTime getSendTime() {
		return sendTime;
	}

	public void setSendTime(LocalDateTime sendTime) {
		this.sendTime = sendTime;
	}

}
