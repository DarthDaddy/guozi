package com.chinatechstar.chart.vo;

import java.io.Serializable;

import com.chinatechstar.component.commons.vo.CommonVO;

/**
 * 图表信息的参数类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class ChartVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = 7709247818109517872L;
	private String salesType;// 销售类别

	public String getSalesType() {
		return salesType;
	}

	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}

}
