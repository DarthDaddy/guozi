package com.chinatechstar.environment.entity;

import com.chinatechstar.component.commons.entity.TimeEntity;

import java.io.Serializable;

public class Inspect  extends TimeEntity implements Serializable {

    private static final long serialVersionUID = -4897378541251659693L;
    private Integer id;
    private String inspectTitle;
    private String inspectText;
    private Long firmId;
    private String fileUrl;
    private String inspectImg;
    private Integer inspectCode;
    private String inspetFile;
    private String inspetContent;

    public String getInspetContent() {
        return inspetContent;
    }

    public void setInspetContent(String inspetContent) {
        this.inspetContent = inspetContent;
    }

    public String getInspectImg() {
        return inspectImg;
    }

    public void setInspectImg(String inspectImg) {
        this.inspectImg = inspectImg;
    }

    public Integer getInspectCode() {
        return inspectCode;
    }

    public void setInspectCode(Integer inspectCode) {
        this.inspectCode = inspectCode;
    }

    public String getInspetFile() {
        return inspetFile;
    }

    public void setInspetFile(String inspetFile) {
        this.inspetFile = inspetFile;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getFirmId() {
        return firmId;
    }

    public void setFirmId(Long firmId) {
        this.firmId = firmId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInspectTitle() {
        return inspectTitle;
    }

    public void setInspectTitle(String inspectTitle) {
        this.inspectTitle = inspectTitle;
    }

    public String getInspectText() {
        return inspectText;
    }

    public void setInspectText(String inspectText) {
        this.inspectText = inspectText;
    }

}
