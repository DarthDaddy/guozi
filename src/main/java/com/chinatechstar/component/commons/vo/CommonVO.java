package com.chinatechstar.component.commons.vo;

import java.io.Serializable;

/**
 * 参数类共用字段
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class CommonVO implements Serializable {

	private static final long serialVersionUID = 6332697217948480782L;
	private Integer currentPage = 1;// 当前页数
	private Integer pageSize = 10;// 每页记录数
	private String sorter;// 排序

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSorter() {
		return sorter;
	}

	public void setSorter(String sorter) {
		this.sorter = sorter;
	}

}
