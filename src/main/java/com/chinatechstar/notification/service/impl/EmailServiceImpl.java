package com.chinatechstar.notification.service.impl;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.component.commons.client.ClientResponse;
import com.chinatechstar.component.commons.client.ResultCode;
import com.chinatechstar.notification.service.EmailService;

/**
 * 邮件的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class EmailServiceImpl implements EmailService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private Environment environment;

	/**
	 * 发送邮件
	 */
	@Override
	public ClientResponse sendEmail(String toEmail, String subject, String text) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(environment.getProperty("spring.mail.username"));
			mimeMessageHelper.setTo(toEmail);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(text);
			mailSender.send(mimeMessage);
			logger.info("邮件已发送到： {}", toEmail);
			return new ClientResponse(ResultCode.SUCCESS);
		} catch (Exception e) {
			return new ClientResponse(ResultCode.FAILURE, e.toString());
		}
	}

}
