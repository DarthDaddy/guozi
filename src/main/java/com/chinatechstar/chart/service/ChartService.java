package com.chinatechstar.chart.service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 图表信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface ChartService {

	/**
	 * 查询商品报表数据
	 * 
	 * @param currentPage 当前页数
	 * @param pageSize    每页记录数
	 * @param salesType   销售类别
	 * @return
	 */
	Map<String, Object> queryProduct(Integer currentPage, Integer pageSize, String salesType);

	/**
	 * 查询图表的分析页数据
	 * 
	 * @param period 时期
	 * @return
	 */
	LinkedHashMap<String, Object> queryAnalysisChartData(String period);

	/**
	 * 查询图表的监控页数据
	 * 
	 * @return
	 */
	LinkedHashMap<String, Object> queryMonitorChartData();

}
