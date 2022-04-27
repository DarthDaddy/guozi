package com.chinatechstar.component.commons.result;

import java.io.Serializable;

/**
 * HTTP的返回响应类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class ListResult<T> implements Serializable {

	private static final long serialVersionUID = -3755143195269176337L;
	private int status;// 状态编码
	private T data;// 数据 NOSONAR
	private String message;// 消息

	public ListResult() {
		super();
	}

	public ListResult(int status, T data, String message) {
		super();
		this.status = status;
		this.data = data;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
