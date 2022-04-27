package com.chinatechstar.environment.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.chinatechstar.component.commons.entity.TimeEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 法规表
 */
public class Law extends TimeEntity implements Serializable {

    private static final long serialVersionUID = 4720682870090069330L;
    private Integer id;
    private String  lawTitle;
    private String lawText;
    private Integer pId;
    private List<Law> lawList;
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

    public List<Law> getLawList() {
        return lawList;
    }

    public void setLawList(List<Law> lawList) {
        this.lawList = lawList;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        Law law = (Law) o;
        return Objects.equals ( id, law.id ) &&
                Objects.equals ( lawTitle, law.lawTitle ) &&
                Objects.equals ( lawText, law.lawText );
    }

    @Override
    public int hashCode() {
        return Objects.hash ( id, lawTitle, lawText );
    }


    @Override
    public String toString() {
        return "Law{" +
                "id=" + id +
                ", lawTitle='" + lawTitle + '\'' +
                ", lawText='" + lawText + '\'' +
                '}';
    }
}
