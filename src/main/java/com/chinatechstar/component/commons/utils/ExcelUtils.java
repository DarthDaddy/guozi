package com.chinatechstar.component.commons.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.chinatechstar.component.commons.exception.ServiceException;

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
			ExcelWriter excelWriter = EasyExcel.write(servletOutputStream).excelType(ExcelTypeEnum.XLSX).relativeHeadRowIndex(0).build();
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
						list.add(value == null ? null : value.toString());
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

}
