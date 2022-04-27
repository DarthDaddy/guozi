package com.chinatechstar.gateway.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.chinatechstar.cache.redis.constants.ApplicationConstants;
import com.chinatechstar.cache.redis.util.RedisUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * MSCode的入口网关过滤器
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Component
public class GatewayFilter extends ZuulFilter {

	private static Logger logger = LoggerFactory.getLogger(GatewayFilter.class);
	@Autowired
	private RedisUtils redisUtils;

	/**
	 * 过滤器的类型，"pre"指在请求之前执行
	 */
	@Override
	public String filterType() {
		return "pre";
	}

	/**
	 * 过滤器执行的顺序
	 */
	@Override
	public int filterOrder() {
		return 1;
	}

	/**
	 * 过滤器是否生效
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	/**
	 * 执行过滤器
	 */
	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();

		String url = request.getRequestURL().toString();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (url.indexOf("/api/uaa/oauth/token") == -1 && url.indexOf("/api/file/static/upload") == -1
				&& url.indexOf("/api/account/sysuserdetail/compareCaptcha") == -1 && url.indexOf("swagger") == -1 && url.indexOf("/v2/api-docs") == -1
				&& url.indexOf("druid") == -1) {
			String partialUrl = url.substring(url.lastIndexOf("api") + 3);// 截取api后面的url
			String[] partialUrlArray = partialUrl.split("/");
			String newPartialUrl = "/" + partialUrlArray[1] + "/" + partialUrlArray[2] + "/" + partialUrlArray[3];// 再处理待比较url，以支持路径包含传参，格式是：模块路径 + 类路径 + 方法路径
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < authentication.getAuthorities().size(); i++) {
				stringBuilder.append(redisUtils.get(ApplicationConstants.URL_ROLECODE_PREFIX + authentication.getAuthorities().toArray()[i]));
			}
			if (stringBuilder.indexOf(newPartialUrl) == -1) {
				unauthorizedOperate(context);
			}
		}

		return null; // 正常执行，调用服务接口
	}

	/**
	 * 处理无权限的URL
	 * 
	 * @param context 请求上下文对象
	 */
	private void unauthorizedOperate(RequestContext context) {
		try {
			context.setSendZuulResponse(false);
			context.getResponse().sendError(HttpServletResponse.SC_UNAUTHORIZED, "请求的URL无权限");
		} catch (IOException e) {
			logger.warn(e.toString());
		}
	}

}
