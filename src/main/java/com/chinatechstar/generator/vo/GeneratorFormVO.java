package com.chinatechstar.generator.vo;

import java.io.Serializable;

import com.chinatechstar.component.commons.vo.CommonVO;

/**
 * 表单信息的参数类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class GeneratorFormVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = 3305789535165450291L;
	private String formDescription;// 表单描述
	private String formContent;// 表单内容

	public String getFormDescription() {
		return formDescription;
	}

	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}

	public String getFormContent() {
		return formContent;
	}

	public void setFormContent(String formContent) {
		this.formContent = formContent;
	}

}
