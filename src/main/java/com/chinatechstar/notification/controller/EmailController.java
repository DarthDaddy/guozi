package com.chinatechstar.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chinatechstar.component.commons.client.ClientResponse;
import com.chinatechstar.notification.service.EmailService;

/**
 * 邮件的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/email")
public class EmailController {

	@Autowired
	private EmailService emailService;

	/**
	 * 发送邮件
	 * 
	 * @param toEmail 接收者的邮箱
	 * @param subject 邮件主题
	 * @param text    邮件内容
	 */
	@PostMapping(path = "/sendEmail")
	public ClientResponse sendEmail(@RequestParam(name = "toEmail", required = true) String toEmail,
			@RequestParam(name = "subject", required = true) String subject, @RequestParam(name = "text", required = false) String text) {
		return emailService.sendEmail(toEmail, subject, text);
	}

}
