package com.chinatechstar.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chinatechstar.notification.service.AliyunSmsService;
import com.chinatechstar.statistics.controller.StatisticsController;

/**
 * 阿里云短信的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/sms")
public class AliyunSmsController {

	@Autowired
	private AliyunSmsService aliyunSmsService;
	@Autowired
	private StatisticsController statisticsClient;

	/**
	 * 阿里云发送短信
	 * 
	 * @param phoneNumber   手机号
	 * @param signName      阿里云短信签名
	 * @param templateCode  阿里云短信模板Code
	 * @param templateParam JSON模板参数字符串
	 * @return
	 */
	@PostMapping(path = "/sendSms")
	public boolean sendSms(@RequestParam(name = "phoneNumber", required = true) String phoneNumber,
			@RequestParam(name = "signName", required = true) String signName, @RequestParam(name = "templateCode", required = true) String templateCode,
			@RequestParam(name = "templateParam", required = true) String templateParam) {
		boolean result = aliyunSmsService.sendSms(phoneNumber, signName, templateCode, templateParam);
		if (result) {
			statisticsClient.insertSmsStatistics(phoneNumber, (short) 1);
		} else {
			statisticsClient.insertSmsStatistics(phoneNumber, (short) 0);
		}
		return result;
	}

	/**
	 * 获取随机数验证码
	 * 
	 * @param digits 几位随机数
	 * @return
	 */
	@GetMapping(path = "/getRandomCode")
	public String getRandomCode(@RequestParam(name = "digits", required = true) int digits) {
		return aliyunSmsService.getRandomCode(digits);
	}

}
