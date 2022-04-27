package com.chinatechstar.admin.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinatechstar.admin.entity.SysParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 参数信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface SysParamService {

	/**
	 * 查询参数分页
	 * 
	 * @param currentPage 当前页数
	 * @param pageSize    每页记录数
	 * @param paramName   参数名称
	 * @param paramKey    参数键名
	 * @param paramValue  参数键值
	 * @param sorter      排序
	 * @return
	 */
	Map<String, Object> querySysParam(Integer currentPage, Integer pageSize, String paramName, String paramKey, String paramValue, String sorter);

	/**
	 * 查询参数的导出数据列表
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysParamForExcel(Map<String, Object> paramMap);

	/**
	 * 新增参数
	 * 
	 * @param sysParam 参数对象
	 */
	void insertSysParam(SysParam sysParam);

	/**
	 * 编辑参数
	 * 
	 * @param sysParam 参数对象
	 */
	void updateSysParam(SysParam sysParam);

	/**
	 * 删除参数
	 * 
	 * @param id 参数ID
	 */
	void deleteSysParam(Long[] id);

	/**
	 * 导入参数
	 *
	 * @param file 文件资源
	 */
    void importSysParam(MultipartFile file);
}
