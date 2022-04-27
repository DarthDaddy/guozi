package com.chinatechstar.chart.service.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinatechstar.component.commons.utils.CurrentUserUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.admin.controller.SysRoleController;
import com.chinatechstar.chart.mapper.ChartMapper;
import com.chinatechstar.chart.service.ChartService;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import com.chinatechstar.component.commons.utils.CollectionUtils;

/**
 * 图表信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class ChartServiceImpl implements ChartService {

	private static final String PROFIT = "profit";

	@Autowired
	private ChartMapper chartMapper;
	@Autowired
	private SysRoleController sysRoleServiceClient;

	/**
	 * 查询商品报表数据
	 */
	@Override
	public Map<String, Object> queryProduct(Integer currentPage, Integer pageSize, String salesType) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("salesType", salesType);
		paramMap.put("tenantCode", CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		List<LinkedHashMap<String, Object>> resultList = chartMapper.queryProduct(paramMap);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String roleData = sysRoleServiceClient.queryRoleData("groupchart", authentication.getName());
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), (long) resultList.size(), currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, (long) resultList.size(), currentPage, pageSize);
		}
	}

	/**
	 * 查询图表的分析页数据
	 */
	@Override
	public LinkedHashMap<String, Object> queryAnalysisChartData(String period) {
		String startTime = null;
		String endTime = null;
		String[] periodArray = period.split(",");
		if (periodArray.length == 2) {
			startTime = periodArray[0];
			endTime = periodArray[1];
		} else {
			startTime = period;
		}

		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
		List<Map<String, Object>> visitsList = chartMapper.queryVisitsList(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		List<Map<String, Object>> visitsTrendList = chartMapper.queryVisitsTrendList(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		List<Map<String, Object>> paymentQuantityList = chartMapper.queryPaymentQuantityList(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		List<Map<String, Object>> searchList = chartMapper.querySearchList(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		List<Map<String, Object>> cvrList = chartMapper.queryCvrList(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		List<Map<String, Object>> flowList = chartMapper.queryFlowList(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		List<Map<String, Object>> salesList = chartMapper.querySalesList(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		List<Map<String, Object>> salesTypeList = chartMapper.querySalesTypeList(null, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		List<Map<String, Object>> salesTypeOnlineList = chartMapper.querySalesTypeList("线上", CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		List<Map<String, Object>> salesTypeOfflineList = chartMapper.querySalesTypeList("门店", CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		List<Map<String, Object>> salesRankingList = chartMapper.querySalesRankingList(startTime, endTime, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		List<Map<String, Object>> visitsRankingList = chartMapper.queryVisitsRankingList(startTime, endTime, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		List<Map<String, Object>> userCountList = chartMapper.queryUserCountList(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		List<Map<String, Object>> perCapitaUserCountList = chartMapper.queryPerCapitaUserCountList(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));

		NumberFormat numberFormat = NumberFormat.getPercentInstance();
		Map<String, Object> analysisMap = new HashMap<>();
		// 模拟数据，按实际返回
		analysisMap.put("totalSales", 186560);// 总销售额
		analysisMap.put("dailySales", 5236);// 日销售额
		analysisMap.put("weekGain", numberFormat.format(0.18));// 周同比
		analysisMap.put("dailyGain", numberFormat.format(0.17));// 日同比
		analysisMap.put("totalVisits", 8856);// 总访问量
		analysisMap.put("dailyVisits", 3391);// 日访问量
		analysisMap.put("totalPaymentQuantity", 6563);// 总支付笔数
		analysisMap.put("conversionRate", numberFormat.format(0.68));// 转化率
		analysisMap.put("activityEffect", numberFormat.format(0.88));// 运营活动效果
		analysisMap.put("totalUserCount", 245);// 搜索用户数
		analysisMap.put("subTotalUserCount", 15);// 搜索用户数子统计
		analysisMap.put("totalPerCapitaUserCount", 11);// 人均搜索次数
		analysisMap.put("subTotalPerCapitaUserCount", 6);// 人均搜索次数子统计

		resultMap.put("analysisData", analysisMap);
		resultMap.put("visitsData", visitsList);
		resultMap.put("visitsTrendData", visitsTrendList);
		resultMap.put("paymentQuantityData", paymentQuantityList);
		resultMap.put("searchData", searchList);
		resultMap.put("cvrData", cvrList);
		resultMap.put("flowData", flowList);
		resultMap.put("salesData", salesList);
		resultMap.put("salesTypeData", salesTypeList);
		resultMap.put("salesTypeOnlineData", salesTypeOnlineList);
		resultMap.put("salesTypeOfflineData", salesTypeOfflineList);
		resultMap.put("salesRankingData", salesRankingList);
		resultMap.put("visitsRankingData", visitsRankingList);
		resultMap.put("userCountData", userCountList);
		resultMap.put("perCapitaUserCountData", perCapitaUserCountList);
		return resultMap;
	}

	/**
	 * 查询图表的监控页数据
	 */
	@Override
	public LinkedHashMap<String, Object> queryMonitorChartData() {
		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();

		// 模拟数据，按实际返回
		List<Map<String, Object>> compareDonutList = new ArrayList<>();
		Map<String, Object> compareDonutMap1 = new LinkedHashMap<>();
		compareDonutMap1.put("year", 2020);
		compareDonutMap1.put("area", "亚太地区");
		compareDonutMap1.put(PROFIT, 7860 * 0.189);
		compareDonutList.add(compareDonutMap1);
		Map<String, Object> compareDonutMap2 = new LinkedHashMap<>();
		compareDonutMap2.put("year", 2020);
		compareDonutMap2.put("area", "非洲及中东");
		compareDonutMap2.put(PROFIT, 7860 * 0.042);
		compareDonutList.add(compareDonutMap2);
		Map<String, Object> compareDonutMap3 = new LinkedHashMap<>();
		compareDonutMap3.put("year", 2020);
		compareDonutMap3.put("area", "拉丁美洲");
		compareDonutMap3.put(PROFIT, 7860 * 0.025);
		compareDonutList.add(compareDonutMap3);
		Map<String, Object> compareDonutMap4 = new LinkedHashMap<>();
		compareDonutMap4.put("year", 2020);
		compareDonutMap4.put("area", "中欧和东欧");
		compareDonutMap4.put(PROFIT, 7860 * 0.018);
		compareDonutList.add(compareDonutMap4);
		Map<String, Object> compareDonutMap5 = new LinkedHashMap<>();
		compareDonutMap5.put("year", 2020);
		compareDonutMap5.put("area", "西欧");
		compareDonutMap5.put(PROFIT, 7860 * 0.462);
		compareDonutList.add(compareDonutMap5);
		Map<String, Object> compareDonutMap6 = new LinkedHashMap<>();
		compareDonutMap6.put("year", 2020);
		compareDonutMap6.put("area", "北美");
		compareDonutMap6.put(PROFIT, 7860 * 0.265);
		compareDonutList.add(compareDonutMap6);
		Map<String, Object> compareDonutMap7 = new LinkedHashMap<>();
		compareDonutMap7.put("year", 2019);
		compareDonutMap7.put("area", "亚太地区");
		compareDonutMap7.put(PROFIT, 7860 * 0.539);
		compareDonutList.add(compareDonutMap7);
		Map<String, Object> compareDonutMap8 = new LinkedHashMap<>();
		compareDonutMap8.put("year", 2019);
		compareDonutMap8.put("area", "非洲及中东");
		compareDonutMap8.put(PROFIT, 7860 * 0.065);
		compareDonutList.add(compareDonutMap8);
		Map<String, Object> compareDonutMap9 = new LinkedHashMap<>();
		compareDonutMap9.put("year", 2019);
		compareDonutMap9.put("area", "拉丁美洲");
		compareDonutMap9.put(PROFIT, 7860 * 0.065);
		compareDonutList.add(compareDonutMap9);
		Map<String, Object> compareDonutMap10 = new LinkedHashMap<>();
		compareDonutMap10.put("year", 2019);
		compareDonutMap10.put("area", "中欧和东欧");
		compareDonutMap10.put(PROFIT, 7860 * 0.034);
		compareDonutList.add(compareDonutMap10);
		Map<String, Object> compareDonutMap11 = new LinkedHashMap<>();
		compareDonutMap11.put("year", 2019);
		compareDonutMap11.put("area", "西欧");
		compareDonutMap11.put(PROFIT, 7860 * 0.063);
		compareDonutList.add(compareDonutMap11);
		Map<String, Object> compareDonutMap12 = new LinkedHashMap<>();
		compareDonutMap12.put("year", 2019);
		compareDonutMap12.put("area", "北美");
		compareDonutMap12.put(PROFIT, 7860 * 0.234);
		compareDonutList.add(compareDonutMap12);

		List<Map<String, Object>> activeList = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			Map<String, Object> activeMap = new LinkedHashMap<>();
			activeMap.put("x", i + ":00");
			activeMap.put("y", RandomStringUtils.random(4, "123456789"));
			activeList.add(activeMap);
		}

		List<Map<String, Object>> tagList = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Map<String, Object> tagMap = new LinkedHashMap<>();
			tagMap.put("name", "广州市");
			tagMap.put("value", i);
			tagMap.put("type", 1);// 0-2
			tagList.add(tagMap);
		}

		NumberFormat numberFormat = NumberFormat.getPercentInstance();
		Map<String, Object> monitorMap = new HashMap<>();
		monitorMap.put("totalTransactionsToday", 124888233);// 今日交易总额
		monitorMap.put("salesTargetCompletionRate", numberFormat.format(0.95));// 销售目标完成率
		monitorMap.put("totalTransactionsPerSecond", 345);// 每秒交易总额
		monitorMap.put("efficiency", 88);// 效率
		monitorMap.put("fastFood", numberFormat.format(0.48));// 中式快餐占比
		monitorMap.put("westernFood", numberFormat.format(0.32));// 西餐占比
		monitorMap.put("hotPot", numberFormat.format(0.20));// 火锅占比
		monitorMap.put("fundSurplus", 35);// 补贴资金剩余

		resultMap.put("monitorData", monitorMap);
		resultMap.put("compareDonutData", compareDonutList);
		resultMap.put("activeData", activeList);
		resultMap.put("tagData", tagList);
		return resultMap;
	}

}
