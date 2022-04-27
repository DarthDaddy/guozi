package com.chinatechstar.chart.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 图表信息的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface ChartMapper {

	/**
	 * 查询商品报表数据
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryProduct(Map<String, Object> paramMap);

	/**
	 * 查询访问量的数据列表
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryVisitsList();

	/**
	 * 查询访问量趋势的数据列表
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryVisitsTrendList();

	/**
	 * 查询门店访问量排名的数据列表
	 * 
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryVisitsRankingList(String startTime, String endTime);

	/**
	 * 查询支付笔数的数据列表
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryPaymentQuantityList();

	/**
	 * 查询线上热门搜索的数据列表
	 * 
	 * @return
	 */
	List<Map<String, Object>> querySearchList();

	/**
	 * 查询店铺转化率的数据列表
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryCvrList();

	/**
	 * 查询客流量支付笔数的数据列表
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryFlowList();

	/**
	 * 查询销售额的数据列表
	 * 
	 * @return
	 */
	List<Map<String, Object>> querySalesList();

	/**
	 * 查询门店销售额排名的数据列表
	 * 
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * 
	 * 
	 * @return
	 */
	List<Map<String, Object>> querySalesRankingList(String startTime, String endTime);

	/**
	 * 根据渠道查询销售额类别占比的数据列表
	 * 
	 * @param channel 渠道
	 * @return
	 */
	List<Map<String, Object>> querySalesTypeList(@Param(value = "channel") String channel);

	/**
	 * 查询搜索用户数的数据列表
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryUserCountList();

	/**
	 * 查询人均搜索次数的数据列表
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryPerCapitaUserCountList();

}
