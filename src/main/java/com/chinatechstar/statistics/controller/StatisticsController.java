package com.chinatechstar.statistics.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chinatechstar.component.commons.client.ClientResponse;
import com.chinatechstar.statistics.service.StatisticsService;

/**
 * 统计的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

	@Autowired
	private StatisticsService statisticsService;

	/**
	 * 插入短信发送记录
	 * 
	 * @param mobile 手机号
	 * @param status 发送状态
	 * @return
	 */
	@PostMapping(value = "/insertSmsStatistics")
	public ClientResponse insertSmsStatistics(@RequestParam(name = "mobile", required = true) String mobile,
			@RequestParam(name = "status", required = true) Short status) {
		return statisticsService.insertSmsStatistics(mobile, status);
	}

	/**
	 * 查询短信发送记录
	 * 
	 * @return
	 */
	@GetMapping(value = "/querySmsRecord")
	public List<Map<String, Object>> querySmsRecord() {
		return statisticsService.querySmsRecord();
	}

}
