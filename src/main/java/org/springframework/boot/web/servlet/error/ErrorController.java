package org.springframework.boot.web.servlet.error;

/**
 * Spring 标记接口用于 {@link Controller @Controller} 渲染错误。主要用于不需要安全认证的错误路径。
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@FunctionalInterface
public interface ErrorController {

	/**
	 * Returns the path of the error page.
	 * 
	 * @return the error path
	 */
	String getErrorPath();

}
