package com.chinatechstar.component.commons.exception;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 找不到资源的统一异常处理
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1087806513635138814L;

	private final String resourceName;
	private final Serializable id;

	public ResourceNotFoundException(String resourceName, Serializable id) {
		super(resourceName + " 找不到.");
		this.resourceName = resourceName;
		this.id = id;
	}

	public String getResourceName() {
		return resourceName;
	}

	public Serializable getId() {
		return id;
	}

}
