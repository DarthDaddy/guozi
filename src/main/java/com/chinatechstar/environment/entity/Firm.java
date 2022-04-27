package com.chinatechstar.environment.entity;

import com.chinatechstar.component.commons.entity.TimeEntity;

import java.io.Serializable;

public class Firm extends TimeEntity implements Serializable {

    private Integer id;
    private String  firmName;
    private String  firmLogPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getFirmLogPath() {
        return firmLogPath;
    }

    public void setFirmLogPath(String firmLogPath) {
        this.firmLogPath = firmLogPath;
    }
}
