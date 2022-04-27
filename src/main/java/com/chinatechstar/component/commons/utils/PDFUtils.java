package com.chinatechstar.component.commons.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.chinatechstar.component.commons.exception.ServiceException;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

/**
 * PDF工具类
 *
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class PDFUtils {

    // Page configuration
    private static final PDRectangle PAGE_SIZE = PDRectangle.A4;
    private static final float MARGIN = 20;
    private static final boolean IS_LANDSCAPE = true;
    // Font configuration
    private static final float FONT_SIZE = 14;
    // PDFTable configuration
    private static final float ROW_HEIGHT = 60;
    private static final float CELL_MARGIN = 2;

    private PDFUtils() {

    }

    /**
     * 导出PDF
     *
     * @param headList PDF表头列表
     * @param dataList PDF内容列表
     * @param pdfName  PDF名称
     * @param response 响应对象
     * @throws IOException
     */
    public static void exportPDF(List<String> headList, List<LinkedHashMap<String, Object>> dataList, String pdfName, HttpServletResponse response)
            throws IOException {
        ServletOutputStream servletOutputStream = null;
        PDDocument document = null;
        // PDPage page = null;
        try {
            servletOutputStream = response.getOutputStream();
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = new String((pdfName + new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(), StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".pdf");

            document = new PDDocument();

            URL url = PDFUtils.class.getClassLoader().getResource("static");
            String path = url.getPath() + File.separator + "simsun.ttf";
            PDFont font = PDType0Font.load(document, new File(path));

            if (!CollectionUtils.isEmpty(dataList)) {
                List<List<String>> data = new ArrayList<>();
                dataList.forEach(map -> {
                    List<String> list = new ArrayList<>();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        Object value = entry.getValue();
                        if (value != null && value instanceof Date) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            list.add(sdf.format(value));
                        } else {
                            list.add(value == null ? null : value.toString());
                        }
                    }
                    data.add(list);
                });
                new PDFTableGenerator().generatePDF(servletOutputStream, createContent(font, headList, data.stream().map(l -> l.stream().toArray(String[]::new)).toArray(String[][]::new)));
            }

            document.save(servletOutputStream);
            servletOutputStream.flush();
        } catch (IOException e) {
            throw new ServiceException(e.toString());
        } finally {
            if (document != null) {
                document.close();
            }
            if (servletOutputStream != null) {
                servletOutputStream.close();
            }
        }
    }

    private static PDFTable createContent(PDFont font, List<String> headList, String[][] content) {
        // Total size of columns must not be greater than table width.
        List<PDFColumn> columns = new ArrayList<PDFColumn>();
        if (!CollectionUtils.isEmpty(headList)) {
            headList.forEach(head -> {
                columns.add(new PDFColumn(head, 800 / headList.size()));
            });
        }

        float tableHeight = IS_LANDSCAPE ? PAGE_SIZE.getWidth() - (2 * MARGIN) : PAGE_SIZE.getHeight() - (2 * MARGIN);

        PDFTable table = new PDFTableBuilder()
                .setCellMargin(CELL_MARGIN)
                .setColumns(columns)
                .setContent(content)
                .setHeight(tableHeight)
                .setNumberOfRows(content.length)
                .setRowHeight(ROW_HEIGHT)
                .setMargin(MARGIN)
                .setPageSize(PAGE_SIZE)
                .setLandscape(IS_LANDSCAPE)
                .setTextFont(font)
                .setFontSize(FONT_SIZE)
                .build();
        return table;
    }

}
