package com.chinatechstar.chart.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chinatechstar.chart.service.ChartService;
import com.chinatechstar.chart.vo.ChartVO;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;

/**
 * 图表信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/chart")
public class ChartController {

	@Autowired
	private ChartService chartService;

	/**
	 * 查询商品报表数据
	 * 
	 * @param chartVO 商品报表前端参数
	 * @return
	 */
	@GetMapping(path = "/queryProduct")
	public ListResult<Object> queryProduct(ChartVO chartVO) {
		Map<String, Object> data = chartService.queryProduct(chartVO.getCurrentPage(), chartVO.getPageSize(), chartVO.getSalesType());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询图表的分析页数据
	 * 
	 * @param period 时期
	 * @return
	 */
	@GetMapping(path = "/queryAnalysisChartData")
	public ListResult<Object> queryAnalysisChartData(@RequestParam(name = "period", required = false) String period) {
		LinkedHashMap<String, Object> data = chartService.queryAnalysisChartData(period);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询图表的监控页数据
	 * 
	 * @return
	 */
	@GetMapping(path = "/queryMonitorChartData")
	public ListResult<Object> queryMonitorChartData() {
		LinkedHashMap<String, Object> data = chartService.queryMonitorChartData();
		return ResultBuilder.buildListSuccess(data);
	}

}
