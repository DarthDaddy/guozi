package com.chinatechstar.component.commons.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.InternalContextAdapterImpl;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import com.chinatechstar.component.commons.exception.ServiceException;

/**
 * 代码生成器工具类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class GeneratorUtils {

	/**
	 * 生成代码
	 * 
	 * @throws ParseException
	 */
	public static void generateResource(LinkedHashMap<String, Object> codeMap, List<LinkedHashMap<String, Object>> fieldList,
			List<LinkedHashMap<String, Object>> templateList, ZipOutputStream zipOutputStream) throws ParseException {
		// 设置Velocity资源加载器
		Properties properties = new Properties();
		properties.setProperty("resource.loader", "file");
		properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		Velocity.init(properties);

		String packageName = (String) codeMap.get("packageName");
		String entityName = (String) codeMap.get("entityName");
		String tableName = (String) codeMap.get("tableName");

		// 封装模板数据
		Map<String, Object> map = new HashMap<>();
		map.put("packageName", packageName);
		map.put("entityName", entityName);
		map.put("lowercaseEntityName", entityName.toLowerCase());// 字母全部小写
		map.put("lowercaseFirstOneEntityName", toLowerCaseFirstOne(entityName));// 首字母转小写
		map.put("tableName", tableName);
		map.put("fieldList", fieldList);
		VelocityContext velocityContext = new VelocityContext(map);

		for (LinkedHashMap<String, Object> template : templateList) {
			// 渲染模板
			StringWriter stringWriter = new StringWriter();
			RuntimeInstance runtimeInstance = new RuntimeInstance();
			runtimeInstance.init();
			SimpleNode simpleNode = runtimeInstance.parse(String.valueOf(template.get("content")), String.valueOf(template.get("item")));

			Template renderTemplate = new Template();
			simpleNode.init(new InternalContextAdapterImpl(new VelocityContext()), runtimeInstance);
			renderTemplate.setData(simpleNode);
			renderTemplate.merge(velocityContext, stringWriter);

			try {
				// 添加到Zip
				zipOutputStream.putNextEntry(
						new ZipEntry(getFileName(String.valueOf(template.get("item")), entityName, packageName, tableName, toLowerCaseFirstOne(entityName))));
				IOUtils.write(stringWriter.toString(), zipOutputStream, "UTF-8");
				IOUtils.closeQuietly(stringWriter);
				zipOutputStream.closeEntry();
			} catch (IOException e) {
				throw new ServiceException("渲染模板失败，表名：" + entityName, e);
			}
		}
	}

	/**
	 * 获取文件名
	 */
	public static String getFileName(String template, String entityName, String packageName, String tableName, String lowercaseFirstOneEntityName) {
		String packagePath = "main" + File.separator + "java" + File.separator;
		if (StringUtils.isNotBlank(packageName)) {
			packagePath += packageName.replace(".", File.separator) + File.separator;
		}
		if (template.equals("entityjava")) {
			return packagePath + "entity" + File.separator + entityName + ".java";
		}
		if (template.equals("vojava")) {
			return packagePath + "vo" + File.separator + entityName + "VO.java";
		}
		if (template.equals("mapperjava")) {
			return packagePath + "mapper" + File.separator + entityName + "Mapper.java";
		}
		if (template.equals("servicejava")) {
			return packagePath + "service" + File.separator + entityName + "Service.java";
		}
		if (template.equals("serviceimpljava")) {
			return packagePath + "service" + File.separator + "impl" + File.separator + entityName + "ServiceImpl.java";
		}
		if (template.equals("controllerjava")) {
			return packagePath + "controller" + File.separator + entityName + "Controller.java";
		}
		if (template.equals("mapperxml")) {
			return "main" + File.separator + "resources" + File.separator + packageName.replace(".", File.separator) + File.separator + "mapper"
					+ File.separator + entityName + "Mapper.xml";
		}
		if (template.equals("webreactjs")) {
			return "react" + File.separator + entityName + ".js";
		}
		if (template.equals("webreactmodeljs")) {
			return "react" + File.separator + "models" + File.separator + lowercaseFirstOneEntityName + ".js";
		}
		if (template.equals("webvue")) {
			return "vue" + File.separator + entityName + ".vue";
		}
		if (template.equals("webvuemain")) {
			return "vue" + File.separator + entityName + "Main.vue";
		}
		if (template.equals("webvuesub")) {
			return "vue" + File.separator + entityName + "Sub.vue";
		}
		if (template.equals("webvueapi")) {
			return "vue" + File.separator + "api" + File.separator + entityName + ".js";
		}
		if (template.equals("mobilevue")) {
			return "mobile" + File.separator + entityName + "Mobile.vue";
		}
		return null;
	}

	/**
	 * 首字母转小写
	 * 
	 * @return
	 */
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0))) {
			return s;
		} else {
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
		}
	}

	/**
	 * 首字母转大写
	 * 
	 * @return
	 */
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0))) {
			return s;
		} else {
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
		}
	}

	/**
	 * 字段转换成对象属性，例如：user_name to userName
	 * 
	 * @param field 字段名
	 * @return
	 */
	public static String fieldToProperty(String field) {
		if (null == field) {
			return "";
		}
		char[] chars = field.toCharArray();
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '_') {
				int j = i + 1;
				if (j < chars.length) {
					stringBuffer.append(StringUtils.upperCase(CharUtils.toString(chars[j])));
					i++;
				}
			} else {
				stringBuffer.append(c);
			}
		}
		return stringBuffer.toString();
	}

}
