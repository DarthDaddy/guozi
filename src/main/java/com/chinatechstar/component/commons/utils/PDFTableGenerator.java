package com.chinatechstar.component.commons.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.chinatechstar.component.commons.exception.ServiceException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import javax.servlet.ServletOutputStream;

/**
 * PDF表格生成器
 *
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class PDFTableGenerator {

    // Generates document from PDFTable object
    public void generatePDF(ServletOutputStream servletOutputStream, PDFTable table) throws IOException {
        PDDocument doc = null;
        try {
            doc = new PDDocument();
            drawTable(doc, table);
            doc.save(servletOutputStream);
        } finally {
            if (doc != null) {
                doc.close();
            }
        }
    }

    // Configures basic setup for the table and draws it page by page
    public void drawTable(PDDocument doc, PDFTable table) throws IOException {
        // Calculate pagination
        Integer rowsPerPage = new Double(Math.floor(table.getHeight() / table.getRowHeight())).intValue() - 1; // subtract
        Integer numberOfPages = new Double(Math.ceil(table.getNumberOfRows().floatValue() / rowsPerPage)).intValue();

        // Generate each page, get the content and draw it
        for (int pageCount = 0; pageCount < numberOfPages; pageCount++) {
            PDPage page = generatePage(doc, table);
            PDPageContentStream contentStream = generateContentStream(doc, page, table);
            String[][] currentPageContent = getContentForCurrentPage(table, rowsPerPage, pageCount);
            drawCurrentPage(table, currentPageContent, contentStream);
        }
    }

    // Draws current page table grid and border lines and content
    private void drawCurrentPage(PDFTable table, String[][] currentPageContent, PDPageContentStream contentStream)
            throws IOException {
        float tableTopY = table.isLandscape() ? table.getPageSize().getWidth() - table.getMargin() : table.getPageSize().getHeight() - table.getMargin();

        // Draws grid and borders
        drawTableGrid(table, currentPageContent, contentStream, tableTopY);

        // Position cursor to start drawing content
        float nextTextX = table.getMargin() + table.getCellMargin();
        // Calculate center alignment for text in cell considering font height
        float nextTextY = tableTopY - (table.getRowHeight() / 2)
                - ((table.getTextFont().getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * table.getFontSize()) / 4);

        // Write column headers
        writeContentLine(table.getColumnsNamesAsArray(), contentStream, nextTextX, nextTextY, table);
        nextTextY -= table.getRowHeight();
        nextTextX = table.getMargin() + table.getCellMargin();

        // Write content
        for (int i = 0; i < currentPageContent.length; i++) {
            writeContentLine(currentPageContent[i], contentStream, nextTextX, nextTextY, table);
            nextTextY -= table.getRowHeight();
            nextTextX = table.getMargin() + table.getCellMargin();
        }

        contentStream.close();
    }

    // Writes the content for one line
    private void writeContentLine(String[] lineContent, PDPageContentStream contentStream, float nextTextX, float nextTextY,
                                  PDFTable table) throws IOException {
        for (int i = 0; i < table.getNumberOfColumns(); i++) {
            String text = lineContent[i];
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(nextTextX, nextTextY);
            // contentStream.drawString(text != null ? text : "");

            List<String> textList = splitStr(11, text != null ? text : "");
            if (textList != null) {
                for (int m = 0; m < textList.size(); m++) {
                    contentStream.showText(textList.get(m));
                    contentStream.newLine();
                    contentStream.newLineAtOffset(0, -12);
                }
            }

            contentStream.endText();
            nextTextX += table.getColumns().get(i).getWidth();
        }
    }

    /**
     * 拆分方法
     *
     * @param num 每隔几个字符拆分一个中文字符串
     * @param str 原字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public static List<String> splitStr(int num, String str) {
        List<String> list = new ArrayList<>();
        String temp = str;
        int len = str.length();
        while (len > 0) {
            int idx = 0;
            try {
                idx = getEndIndex(temp, num);
            } catch (UnsupportedEncodingException e) {
                throw new ServiceException(e.toString());
            }
            list.add(temp.substring(0, idx + 1));
            temp = temp.substring(idx + 1);
            len = temp.length();
        }
        return list;
    }

    /**
     * 两个数字或者字母等同于一个汉字的长度
     *
     * @param str 字符串
     * @param num 每隔几个字符拆分一个字符串
     * @return 最终索引
     * @throws UnsupportedEncodingException
     */
    public static int getEndIndex(String str, double num) throws UnsupportedEncodingException {
        int idx = 0;
        double val = 0.00;
        // 判断是否是英文/中文
        for (int i = 0; i < str.length(); i++) {
            if (String.valueOf(str.charAt(i)).getBytes("UTF-8").length >= 3) {
                // 中文字符或符号
                val += 1.00;
            } else {
                // 英文字符或符号
                val += 0.50;
            }
            if (val >= num) {
                idx = i;
                if (val - num == 0.5) {
                    idx = i - 1;
                }
                break;
            }
        }
        if (idx == 0) {
            idx = str.length() - 1;
        }
        return idx;
    }

    private void drawTableGrid(PDFTable table, String[][] currentPageContent, PDPageContentStream contentStream, float tableTopY)
            throws IOException {
        // Draw row lines
        float nextY = tableTopY;
        for (int i = 0; i <= currentPageContent.length + 1; i++) {
            contentStream.drawLine(table.getMargin(), nextY, table.getMargin() + table.getWidth(), nextY);
            nextY -= table.getRowHeight();
        }

        // Draw column lines
        final float tableYLength = table.getRowHeight() + (table.getRowHeight() * currentPageContent.length);
        final float tableBottomY = tableTopY - tableYLength;
        float nextX = table.getMargin();
        for (int i = 0; i < table.getNumberOfColumns(); i++) {
            contentStream.drawLine(nextX, tableTopY, nextX, tableBottomY);
            nextX += table.getColumns().get(i).getWidth();
        }
        contentStream.drawLine(nextX, tableTopY, nextX, tableBottomY);
    }

    private String[][] getContentForCurrentPage(PDFTable table, Integer rowsPerPage, int pageCount) {
        int startRange = pageCount * rowsPerPage;
        int endRange = (pageCount * rowsPerPage) + rowsPerPage;
        if (endRange > table.getNumberOfRows()) {
            endRange = table.getNumberOfRows();
        }
        return Arrays.copyOfRange(table.getContent(), startRange, endRange);
    }

    private PDPage generatePage(PDDocument doc, PDFTable table) {
        PDPage page = new PDPage();
        page.setMediaBox(table.getPageSize());
        page.setRotation(table.isLandscape() ? 90 : 0);
        doc.addPage(page);
        return page;
    }

    private PDPageContentStream generateContentStream(PDDocument doc, PDPage page, PDFTable table) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(doc, page, false, false);
        // User transformation matrix to change the reference when drawing.
        // This is necessary for the landscape position to draw correctly
        if (table.isLandscape()) {
            contentStream.concatenate2CTM(0, 1, -1, 0, table.getPageSize().getWidth(), 0);
        }
        contentStream.setFont(table.getTextFont(), table.getFontSize());
        return contentStream;
    }

}
