package com.chinatechstar.component.commons.exception;

import com.chinatechstar.component.commons.client.ResultCode;

/**
 * 业务服务的统一异常处理
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 2359767895161832954L;
	private final ResultCode resultCode; // 微服务之间的调用返回结果

	public ResultCode getResultCode() {
		return resultCode;
	}

	public ServiceException(String message) {
		super(message);
		this.resultCode = ResultCode.FAILURE;
	}

	public ServiceException(ResultCode resultCode) {
		super(resultCode.getMsg());
		this.resultCode = resultCode;
	}

	public ServiceException(ResultCode resultCode, String msg) {
		super(msg);
		this.resultCode = resultCode;
	}

	public ServiceException(ResultCode resultCode, Throwable cause) {
		super(cause);
		this.resultCode = resultCode;
	}

	public ServiceException(String msg, Throwable cause) {
		super(msg, cause);
		this.resultCode = ResultCode.FAILURE;
	}

}
