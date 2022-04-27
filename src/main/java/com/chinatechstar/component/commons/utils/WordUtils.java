package com.chinatechstar.component.commons.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.xwpf.usermodel.*;

import com.chinatechstar.component.commons.exception.ServiceException;

/**
 * Word工具类
 *
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class WordUtils {

    private WordUtils() {

    }

    /**
     * 导出Word
     *
     * @param headList Word表头列表
     * @param dataList Word内容列表
     * @param wordName Word名称
     * @param response 响应对象
     * @throws IOException
     */
    public static void exportWord(List<String> headList, List<LinkedHashMap<String, Object>> dataList, String wordName, HttpServletResponse response)
            throws IOException {
        ServletOutputStream servletOutputStream = null;
        XWPFDocument xwpfDocument = null;
        try {
            servletOutputStream = response.getOutputStream();
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = new String((wordName + new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(), StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".docx");

            xwpfDocument = new XWPFDocument();
            // 添加标题
            XWPFParagraph titleParagraph = xwpfDocument.createParagraph();
            // 设置标题居中
            titleParagraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun xwpfRun = titleParagraph.createRun();
            xwpfRun.setText(wordName);
            xwpfRun.setFontSize(24);

            if (!CollectionUtils.isEmpty(headList)) {
                XWPFTable headXWPFTable = xwpfDocument.createTable();
                XWPFTableRow headXWPFTableRow = headXWPFTable.getRow(0);
                for (int i = 0; i < headList.size(); i++) {
                    if (i == 0) {
                        headXWPFTableRow.getCell(0).setText(headList.get(i));
                    } else {
                        headXWPFTableRow.createCell().setText(headList.get(i));
                    }
                }
            }

            if (!CollectionUtils.isEmpty(dataList)) {
                XWPFTable dataXWPFTable = xwpfDocument.createTable();
                for (int i = 0; i < dataList.size(); i++) {
                    LinkedHashMap<String, Object> map = dataList.get(i);
                    if (i == 0) {
                        XWPFTableRow dataXWPFTableRow = dataXWPFTable.getRow(0);
                        boolean flag = true;
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            Object value = entry.getValue();
                            if (flag) {
                                if (value instanceof Date) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    dataXWPFTableRow.getCell(0).setText(sdf.format(value));
                                } else {
                                    dataXWPFTableRow.getCell(0).setText(value == null ? null : value.toString());
                                }
                                flag = false;
                            } else {
                                if (value instanceof Date) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    dataXWPFTableRow.createCell().setText(sdf.format(value));
                                } else {
                                    dataXWPFTableRow.createCell().setText(value == null ? null : value.toString());
                                }
                            }
                        }
                    } else {
                        XWPFTableRow dataXWPFTableRow = dataXWPFTable.createRow();
                        int j = 0;
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            Object value = entry.getValue();
                            if (value instanceof Date) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                dataXWPFTableRow.getCell(j).setText(sdf.format(value));
                            } else {
                                dataXWPFTableRow.getCell(j).setText(value == null ? null : value.toString());
                            }
                            j++;
                        }
                    }
                }
            }

            xwpfDocument.write(servletOutputStream);
            servletOutputStream.flush();
        } catch (IOException e) {
            throw new ServiceException(e.toString());
        } finally {
            if (xwpfDocument != null) {
                xwpfDocument.close();
            }
            if (servletOutputStream != null) {
                servletOutputStream.close();
            }
        }
    }

}
