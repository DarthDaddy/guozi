package com.chinatechstar.statistics.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.chinatechstar.component.commons.client.ClientResponse;
import com.chinatechstar.component.commons.client.ResultCode;
import com.chinatechstar.statistics.entity.Sms;
import com.chinatechstar.statistics.repository.StatisticsRepository;
import com.chinatechstar.statistics.service.StatisticsService;

/**
 * 统计的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class StatisticsServiceImpl implements StatisticsService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private StatisticsRepository statisticsRepository;

	/**
	 * 插入短信发送记录
	 */
	@Override
	public ClientResponse insertSmsStatistics(String mobile, Short status) {
		try {
			JSONObject object = new JSONObject();
			object.put("mobile", mobile);
			object.put("status", status);
			object.put("sendTime", new Date());
			mongoTemplate.insert(object, "mscode_sms");
			logger.info("{} 用户短信发送记录已插入 ", mobile);
			return new ClientResponse(ResultCode.SUCCESS);
		} catch (Exception e) {
			return new ClientResponse(ResultCode.FAILURE, e.toString());
		}
	}

	/**
	 * 查询短信发送记录
	 */
	@Override
	public List<Map<String, Object>> querySmsRecord() {
		List<Sms> smsList = statisticsRepository.findAll();
		List<Map<String, Object>> resultList = new ArrayList<>();
		for (int i = 0; i < smsList.size(); i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("mobile", smsList.get(i).getMobile());
			map.put("sendTime", smsList.get(i).getSendTime());
			map.put("status", smsList.get(i).getStatus());
			resultList.add(map);
		}
		return resultList;
	}

}
