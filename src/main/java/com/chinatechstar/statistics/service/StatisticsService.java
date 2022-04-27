package com.chinatechstar.statistics.service;

import java.util.List;
import java.util.Map;

import com.chinatechstar.component.commons.client.ClientResponse;

/**
 * 统计的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface StatisticsService {

	/**
	 * 插入短信发送记录
	 * 
	 * @param mobile 手机号
	 * @param status 发送状态
	 * @return
	 */
	ClientResponse insertSmsStatistics(String mobile, Short status);

	/**
	 * 查询短信发送记录
	 * 
	 * @return
	 */
	List<Map<String, Object>> querySmsRecord();

}
