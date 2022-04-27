package com.chinatechstar.environment.vo;

import com.chinatechstar.component.commons.vo.CommonVO;

import java.io.Serializable;

public class NeedVo extends CommonVO implements Serializable {
    private static final long serialVersionUID = -8077647613593171376L;
    private Integer id;
    private String needTitle;
    private String neetText;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNeedTitle() {
        return needTitle;
    }

    public void setNeedTitle(String needTitle) {
        this.needTitle = needTitle;
    }

    public String getNeetText() {
        return neetText;
    }

    public void setNeetText(String neetText) {
        this.neetText = neetText;
    }
}
