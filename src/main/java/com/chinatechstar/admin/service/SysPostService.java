package com.chinatechstar.admin.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinatechstar.admin.entity.SysPost;

/**
 * 岗位信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface SysPostService {

	/**
	 * 查询岗位的数据分页
	 * 
	 * @param currentPage 当前页数
	 * @param pageSize    每页记录数
	 * @param postCode    岗位编码
	 * @param postName    岗位名称
	 * @return
	 */
	Map<String, Object> querySysPost(Integer currentPage, Integer pageSize, String postCode, String postName);

	/**
	 * 查询岗位的树数据
	 * 
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysPostTree();

	/**
	 * 查询岗位的导出数据列表
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysPostForExcel(Map<String, Object> paramMap);

	/**
	 * 新增岗位
	 * 
	 * @param sysPost 岗位对象
	 */
	void insertSysPost(SysPost sysPost);

	/**
	 * 编辑岗位
	 * 
	 * @param sysPost 岗位对象
	 */
	void updateSysPost(SysPost sysPost);

	/**
	 * 删除岗位
	 * 
	 * @param id 岗位ID
	 */
	void deleteSysPost(Long[] id);

}
