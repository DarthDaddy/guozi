package com.chinatechstar.notification.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.chinatechstar.notification.service.AliyunSmsService;

/**
 * 阿里云短信的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class AliyunSmsServiceImpl implements AliyunSmsService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final String SMS_REGIONID = "aliyun.sms.regionId";

	@Autowired
	private Environment environment;

	/**
	 * 获取阿里云短信的默认客户端
	 * 
	 * @return
	 */
	private IAcsClient getDefaultAcsClient() {
		DefaultProfile profile = DefaultProfile.getProfile(environment.getProperty(SMS_REGIONID), environment.getProperty("aliyun.sms.accessKeyId"),
				environment.getProperty("aliyun.sms.accessSecret"));
		DefaultProfile.addEndpoint(environment.getProperty(SMS_REGIONID), environment.getProperty("aliyun.sms.product"),
				environment.getProperty("aliyun.sms.domain"));
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000"); // 超时时间
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		return new DefaultAcsClient(profile);
	}

	/**
	 * 封装阿里云短信的公共Request
	 * 
	 * @return
	 */
	private CommonRequest commonRequest() {
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain(environment.getProperty("aliyun.sms.domain"));
		request.setSysVersion("2017-05-25");
		request.setSysAction("SendSms");
		request.putQueryParameter("RegionId", environment.getProperty(SMS_REGIONID));
		return request;
	}

	/**
	 * 阿里云发送短信
	 */
	@Override
	public boolean sendSms(String phoneNumber, String signName, String templateCode, String templateParam) {
		CommonRequest request = this.commonRequest();
		request.putQueryParameter("PhoneNumbers", phoneNumber);
		request.putQueryParameter("SignName", signName);
		request.putQueryParameter("TemplateCode", templateCode);
		request.putQueryParameter("TemplateParam", templateParam);
		try {
			CommonResponse response = this.getDefaultAcsClient().getCommonResponse(request);
			logger.info(response.getData());
			return true;
		} catch (Exception e) {
			logger.warn(e.toString());
		}
		return false;
	}

	/**
	 * 获取随机数验证码
	 */
	@Override
	public String getRandomCode(int digits) {
		StringBuilder sBuilder = new StringBuilder();
		Random rd = new Random(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
		for (int i = 0; i < digits; ++i) {
			sBuilder.append(rd.nextInt(9));
		}
		return sBuilder.toString();
	}

}
