package com.chinatechstar.environment.entity;

import com.chinatechstar.component.commons.entity.TimeEntity;

import java.io.Serializable;

public class Picture extends TimeEntity implements Serializable {
    private static final long serialVersionUID = -7626772469276259444L;
    private Integer id;
    private String pictureByte;
    private String tenantCode;
    private Integer pictureType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPictureByte() {
        return pictureByte;
    }

    public void setPictureByte(String pictureByte) {
        this.pictureByte = pictureByte;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public Integer getPictureType() {
        return pictureType;
    }

    public void setPictureType(Integer pictureType) {
        this.pictureType = pictureType;
    }
}
