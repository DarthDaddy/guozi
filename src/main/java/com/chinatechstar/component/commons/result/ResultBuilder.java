package com.chinatechstar.component.commons.result;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * 构建结果返回信息
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class ResultBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResultBuilder.class);
	private static final String SUCCESS = "success";

	private ResultBuilder() {

	}

	public static synchronized ListResult<Object> buildListSuccess(String data) {
		ListResult<Object> listResult = new ListResult<>();
		try {
			listResult.setData(data);
			listResult.setStatus(HttpStatus.OK.value());
			listResult.setMessage(SUCCESS);
		} catch (Exception e) {
			ResultBuilder.buildListWarn(listResult, e);
		}
		return listResult;
	}

	public static synchronized ListResult<Object> buildListSuccess(Map<String, Object> data) {
		ListResult<Object> listResult = new ListResult<>();
		try {
			listResult.setData(data);
			listResult.setStatus(HttpStatus.OK.value());
			listResult.setMessage(SUCCESS);
		} catch (Exception e) {
			ResultBuilder.buildListWarn(listResult, e);
		}
		return listResult;
	}

	public static synchronized ListResult<Object> buildListSuccess(List<?> data) {
		ListResult<Object> listResult = new ListResult<>();
		try {
			listResult.setData(data);
			listResult.setStatus(HttpStatus.OK.value());
			listResult.setMessage(SUCCESS);
		} catch (Exception e) {
			ResultBuilder.buildListWarn(listResult, e);
		}
		return listResult;
	}

	public static synchronized void buildListWarn(ListResult<Object> result, Exception e) {
		result.setData(Collections.emptyMap());
		result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		result.setMessage(e.getMessage());
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn(e.toString());
		}
	}

	public static synchronized ActionResult buildActionSuccess() {
		ActionResult actionResult = new ActionResult();
		try {
			actionResult.setStatus(HttpStatus.OK.value());
			actionResult.setMessage(SUCCESS);
		} catch (Exception e) {
			ResultBuilder.buildActionWarn(actionResult, e);
		}
		return actionResult;
	}

	public static synchronized void buildActionWarn(ActionResult result, Exception e) {
		result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		result.setMessage(e.getMessage());
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn(e.toString());
		}
	}

}
