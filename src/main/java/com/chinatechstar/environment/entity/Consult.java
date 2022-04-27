package com.chinatechstar.environment.entity;

import com.chinatechstar.component.commons.entity.TimeEntity;

import java.io.Serializable;

/**
 * 咨询表
 */
public class Consult extends TimeEntity implements Serializable {

    private static final long serialVersionUID = -1579143201666702884L;

    private Integer id;
    private String consultName;
    private String consultText;
    private String consultType;

    public String getConsultType() {
        return consultType;
    }

    public void setConsultType(String consultType) {
        this.consultType = consultType;
    }

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
}
