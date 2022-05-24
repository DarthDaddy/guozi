package com.chinatechstar.component.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.apache.commons.collections.CollectionUtils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.chinatechstar.component.commons.exception.ServiceException;
import org.springframework.web.multipart.MultipartFile;

/**
 * Excel工具类
 *
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class ExcelUtils {

    private ExcelUtils() {

    }

    /**
     * 导出Excel
     *
     * @param headList  Excel表头列表
     * @param dataList  Excel内容列表
     * @param sheetName Excel工作表名称
     * @param response  响应对象
     * @throws IOException
     */
    public static void exportExcel(List<String> headList, List<LinkedHashMap<String, Object>> dataList, String sheetName, HttpServletResponse response)
            throws IOException {
        ServletOutputStream servletOutputStream = null;
        try {
            servletOutputStream = response.getOutputStream();
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = new String((sheetName + new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(), StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            ExcelWriter excelWriter = EasyExcel.write(servletOutputStream).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).excelType(ExcelTypeEnum.XLSX).relativeHeadRowIndex(0).build();
            WriteSheet writeSheet = new WriteSheet();
            writeSheet.setSheetName(sheetName);

            WriteTable table = new WriteTable();
            List<List<String>> head = new ArrayList<>();
            if (!CollectionUtils.isEmpty(headList)) {
                headList.forEach(headName -> head.add(Arrays.asList(headName)));
            }
            table.setHead(head);

            List<List<String>> data = new ArrayList<>();
            if (!CollectionUtils.isEmpty(dataList)) {
                dataList.forEach(map -> {
                    List<String> list = new ArrayList<>();
                    for (Entry<String, Object> entry : map.entrySet()) {
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
            }

            excelWriter.write(data, writeSheet, table);
            excelWriter.finish();
            servletOutputStream.flush();
        } catch (IOException e) {
            throw new ServiceException(e.toString());
        } finally {
            if (servletOutputStream != null) {
                servletOutputStream.close();
            }
        }
    }

    /**
     * 获取Excel列表数据
     *
     * @param file 文件资源
     * @return
     */
    public static List<Map<Integer, String>> readExcel(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            List<Map<Integer, String>> listMap = EasyExcel.read(inputStream).sheet().doReadSync();
            return listMap;
        } catch (Exception e) {
            throw new ServiceException(e.toString());
        }
    }

}
