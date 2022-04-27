package com.chinatechstar.generator.vo;

import java.io.Serializable;

import com.chinatechstar.component.commons.vo.CommonVO;

/**
 * 模板信息的参数类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class GeneratorTemplateVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = 1136168414878561019L;
	private String type; // 模板类型
	private String item;// 模板项目

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

}
