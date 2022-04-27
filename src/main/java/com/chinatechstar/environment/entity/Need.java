package com.chinatechstar.environment.entity;

import com.chinatechstar.component.commons.entity.TimeEntity;

import java.io.Serializable;

public class Need extends TimeEntity implements Serializable {
    private static final long serialVersionUID = -6580047972860702744L;
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
