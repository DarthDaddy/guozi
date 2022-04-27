package com.chinatechstar.notification.service;

/**
 * 阿里云短信的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface AliyunSmsService {

	/**
	 * 阿里云发送短信
	 * 
	 * @param phoneNumber   手机号
	 * @param signName      阿里云短信签名
	 * @param templateCode  阿里云短信模板Code
	 * @param templateParam JSON模板参数字符串
	 * @return
	 */
	boolean sendSms(String phoneNumber, String signName, String templateCode, String templateParam);

	/**
	 * 获取随机数验证码
	 * 
	 * @param digits 几位随机数
	 * @return
	 */
	String getRandomCode(int digits);

}
