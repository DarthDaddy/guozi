package com.chinatechstar.environment.vo;

import com.chinatechstar.component.commons.vo.CommonVO;

import java.io.Serializable;

public class PictureVo extends CommonVO implements Serializable {
    private static final long serialVersionUID = 1614243274865503385L;
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
