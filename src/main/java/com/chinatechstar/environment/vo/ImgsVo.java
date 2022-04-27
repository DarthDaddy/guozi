package com.chinatechstar.environment.vo;

import com.chinatechstar.component.commons.vo.CommonVO;

import java.io.Serializable;

public class ImgsVo extends CommonVO implements Serializable {
    private static final long serialVersionUID = 6730854774260922045L;
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
