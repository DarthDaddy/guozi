package com.chinatechstar.account.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.chinatechstar.cache.redis.constants.ApplicationConstants;
import com.chinatechstar.cache.redis.util.RedisUtils;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.utils.CaptchaUtils;
import com.chinatechstar.notification.controller.AliyunSmsController;

/**
 * 验证码的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private AliyunSmsController aliyunSmsClient;

	/**
	 * 生成图像验证码
	 * 
	 * @param response 响应对象
	 * @return
	 */
	/*@GetMapping(path = "/generateImageCaptcha")
	public ResponseEntity<Resource> generateImageCaptcha(HttpServletResponse response) {
		ResponseEntity<Resource> responseEntity = null;
		try {
			String charCaptcha = CaptchaUtils.generateCharCaptcha();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			redisUtils.psetex(ApplicationConstants.CHAR_CAPTCHA_PREFIX + authentication.getName(), charCaptcha);
			byte[] bytes = CaptchaUtils.generateImageCaptcha(charCaptcha);
			Resource resource = new ByteArrayResource(bytes);
			responseEntity = new ResponseEntity<>(resource, CaptchaUtils.getResponseHeaders(), HttpStatus.OK);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
		return responseEntity;
	}*/

	/**
	 * 获取短信验证码并发送短信
	 * 
	 * @param mobile 手机号
	 * @return
	 */
	@GetMapping(path = "/getSmsCaptcha")
	public ActionResult getSmsCaptcha(@RequestParam(name = "mobile", required = true) String mobile) {
		String smsCaptcha = aliyunSmsClient.getRandomCode(6);
		String phoneNumber = mobile; // 手机号
		String signName = "阿里云短信签名"; // 阿里云短信签名
		String templateCode = "阿里云短信模板Code"; // 阿里云短信模板Code
		JSONObject jsonObject = new JSONObject();// JSON模板参数字符串
		logger.info("短信验证码为：{}", smsCaptcha);
		jsonObject.put("code", smsCaptcha);
		redisUtils.psetex(ApplicationConstants.SMS_CAPTCHA_PREFIX + mobile, smsCaptcha);
		aliyunSmsClient.sendSms(phoneNumber, signName, templateCode, jsonObject.toString());
		return ResultBuilder.buildActionSuccess();
	}

}
