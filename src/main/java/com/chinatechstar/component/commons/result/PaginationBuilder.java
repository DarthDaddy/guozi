package com.chinatechstar.component.commons.result;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 构建分页信息
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class PaginationBuilder {

	private PaginationBuilder() {

	}

	public static synchronized Map<String, Object> buildResult(List<LinkedHashMap<String, Object>> resultList, long total, Integer currentPage,
			Integer pageSize) {
		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put("list", resultList);
		LinkedHashMap<String, Long> paginationMap = new LinkedHashMap<>();
		paginationMap.put("total", total);
		paginationMap.put("pageSize", (long) pageSize);
		paginationMap.put("current", (long) currentPage);
		resultMap.put("pagination", paginationMap);
		return resultMap;
	}

}
