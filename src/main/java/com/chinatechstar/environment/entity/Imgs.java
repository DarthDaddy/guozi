package com.chinatechstar.environment.entity;

import com.chinatechstar.component.commons.entity.TimeEntity;

import java.io.Serializable;

public class Imgs extends TimeEntity implements Serializable {
    private static final long serialVersionUID = 5191837615688082815L;
    private Integer id;
    private String imgContent;
    private String imgCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImgContent() {
        return imgContent;
    }

    public void setImgContent(String imgContent) {
        this.imgContent = imgContent;
    }

    public String getImgCode() {
        return imgCode;
    }

    public void setImgCode(String imgCode) {
        this.imgCode = imgCode;
    }
}
