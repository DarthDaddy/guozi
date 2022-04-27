package com.chinatechstar.notification.service;

import com.chinatechstar.component.commons.client.ClientResponse;

/**
 * 邮件的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface EmailService {

	/**
	 * 发送邮件
	 * 
	 * @param toEmail 接收者的邮箱
	 * @param subject 邮件主题
	 * @param text    邮件内容
	 */
	ClientResponse sendEmail(String toEmail, String subject, String text);

}
