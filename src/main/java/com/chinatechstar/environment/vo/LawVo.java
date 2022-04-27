package com.chinatechstar.environment.vo;

import com.chinatechstar.component.commons.vo.CommonVO;

import java.io.Serializable;


/**
 * 法规表
 */
public class LawVo extends CommonVO implements Serializable {
    private static final long serialVersionUID = 4797220841250729028L;
    private Integer id;
    private String  lawTitle;
    private String lawText;
    private  Integer pId;
    private Integer typeCode;
    private String fileUrl;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLawTitle() {
        return lawTitle;
    }

    public void setLawTitle(String lawTitle) {
        this.lawTitle = lawTitle;
    }

    public String getLawText() {
        return lawText;
    }

    public void setLawText(String lawText) {
        this.lawText = lawText;
    }

}
