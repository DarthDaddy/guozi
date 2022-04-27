package com.chinatechstar.notification.vo;

import java.io.Serializable;

import com.chinatechstar.component.commons.vo.CommonVO;

/**
 * 消息通知的参数类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class NotificationVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = 203348332782322740L;
	private String title;// 标题
	private String content;// 内容
	private String type;// 类型
	private String priority;// 优先级
	private String publisher;// 发布者

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

}
