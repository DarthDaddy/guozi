package com.chinatechstar.component.commons.utils;

/**
 * PDF的表格列
 *
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class PDFColumn {

    private String name;
    private float width;

    public PDFColumn(String name, float width) {
        this.name = name;
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }
}
