package com.chinatechstar.environment.entity;

import com.chinatechstar.component.commons.entity.TimeEntity;

import java.io.Serializable;

public class Supplier extends TimeEntity implements Serializable {
    private static final long serialVersionUID = -5423519096740967041L;

    private Integer id;//供应商主键
    private String supplierName;//供应商名字
    private String supplierType;//供应商行业类型
    private String supplierIntro;//供应商简介
    private String supplierLogo;//供销商头像

    public String getSupplierLogo() {
        return supplierLogo;
    }

    public void setSupplierLogo(String supplierLogo) {
        this.supplierLogo = supplierLogo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public String getSupplierIntro() {
        return supplierIntro;
    }

    public void setSupplierIntro(String supplierIntro) {
        this.supplierIntro = supplierIntro;
    }
}
