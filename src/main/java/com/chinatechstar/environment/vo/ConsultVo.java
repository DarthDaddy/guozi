package com.chinatechstar.environment.vo;

import com.chinatechstar.component.commons.vo.CommonVO;

import java.io.Serializable;

public class ConsultVo extends CommonVO implements Serializable {
    private static final long serialVersionUID = -4160556095063731100L;
    private Integer id;
    private String consultName;
    private String consultText;
    private String consultType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConsultName() {
        return consultName;
    }

    public void setConsultName(String consultName) {
        this.consultName = consultName;
    }

    public String getConsultText() {
        return consultText;
    }

    public void setConsultText(String consultText) {
        this.consultText = consultText;
    }

    public String getConsultType() {
        return consultType;
    }

    public void setConsultType(String consultType) {
        this.consultType = consultType;
    }
}
