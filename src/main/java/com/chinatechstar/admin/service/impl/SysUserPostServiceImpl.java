package com.chinatechstar.admin.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.admin.entity.SysUserPost;
import com.chinatechstar.admin.mapper.SysPostMapper;
import com.chinatechstar.admin.mapper.SysRoleMapper;
import com.chinatechstar.admin.mapper.SysUserPostMapper;
import com.chinatechstar.admin.service.SysUserPostService;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import com.chinatechstar.component.commons.utils.CollectionUtils;
import com.chinatechstar.component.commons.utils.CurrentUserUtils;
import com.chinatechstar.component.commons.utils.SequenceGenerator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 用户与岗位关联信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class SysUserPostServiceImpl implements SysUserPostService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static SequenceGenerator sequenceGenerator = new SequenceGenerator();

	@Autowired
	private SysUserPostMapper sysUserPostMapper;
	@Autowired
	private SysPostMapper sysPostMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;

	/**
	 * 查询用户与岗位关联信息的数据分页
	 */
	@Override
	public Map<String, Object> querySysUserPost(Integer currentPage, Integer pageSize, Long userId, String postCode, String postName, Short postType,
			Short status, String postDescription, String sorter) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("postCode", postCode);
		paramMap.put("postName", postName);
		paramMap.put("postType", postType);
		paramMap.put("status", status);
		paramMap.put("postDescription", postDescription);
		paramMap.put("tenantCode", CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		if (StringUtils.isNotBlank(sorter)) {
			String sort = sorter.substring(0, sorter.lastIndexOf('_'));
			String sequence = "ascend".equals(sorter.substring(sorter.lastIndexOf('_') + 1)) ? "ASC" : "DESC";
			paramMap.put("sort", sort);
			paramMap.put("sequence", sequence);
		} else {
			paramMap.put("sort", "createTime");
			paramMap.put("sequence", "DESC");
		}

		Page<Object> page = PageHelper.startPage(currentPage, pageSize);
		List<LinkedHashMap<String, Object>> resultList = sysUserPostMapper.querySysUserPost(paramMap);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String roleData = sysRoleMapper.queryRoleData("sysuserpost", authentication.getName());
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), page.getTotal(), currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, page.getTotal(), currentPage, pageSize);
		}
	}

	/**
	 * 查询用户与岗位关联信息的下拉框数据
	 */
	@Override
	public List<LinkedHashMap<String, Object>> querySysUserPostTree(String userId) {
		return sysUserPostMapper.querySysUserPostTree(userId);
	}

	/**
	 * 查询用户与岗位关联信息的导出数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> querySysUserPostForExcel(Map<String, Object> paramMap) {
		return sysUserPostMapper.querySysUserPost(paramMap);
	}

	/**
	 * 新增用户与岗位关联信息
	 */
	@Override
	public void insertSysUserPost(SysUserPost sysUserPost) {
		validateSysUserPost(sysUserPost);
		sysUserPost.setId(sequenceGenerator.nextId());
		String postName = getSysPostName(sysUserPost.getPostCode());
		sysUserPost.setPostName(postName);
		sysUserPost.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		sysUserPostMapper.insertSysUserPost(sysUserPost);

		logger.info("用户与岗位关联信息已新增： {}", sysUserPost.getPostCode());
	}

	/**
	 * 编辑用户与岗位关联信息
	 */
	@Override
	public void updateSysUserPost(SysUserPost sysUserPost) {
		validateSysUserPost(sysUserPost);
		String postName = getSysPostName(sysUserPost.getPostCode());
		sysUserPost.setPostName(postName);
		sysUserPostMapper.updateSysUserPost(sysUserPost);

		logger.info("用户与岗位关联信息已编辑： {}", sysUserPost.getPostCode());
	}

	/**
	 * 校验用户与岗位关联信息
	 * 
	 * @param sysUserPost 用户与岗位关联对象
	 */
	private void validateSysUserPost(SysUserPost sysUserPost) {
		if (sysUserPost.getPostType() == 1) {
			Integer existing = sysUserPostMapper.getSysUserPostInfo(sysUserPost.getId(), sysUserPost.getUserId(), sysUserPost.getPostType());
			if (existing != null && existing > 0) {
				throw new IllegalArgumentException("只能新增一个主岗位");
			}
		}
	}

	/**
	 * 根据岗位编码获取岗位上下级名称
	 * 
	 * @param postCode 岗位编码
	 * @return
	 */
	private String getSysPostName(String postCode) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("postCode", postCode);
		Long id = Long.valueOf(String.valueOf(sysPostMapper.querySysPostInfo(paramMap).get(0).get("id")));
		Set<Long> ids = new HashSet<>();
		ids.add(id);
		getRecursiveIds(id, ids);
		Map<String, Object> idParamMap = new HashMap<>();
		idParamMap.put("idArray", ids);
		List<LinkedHashMap<String, Object>> sysPostInfoList = sysPostMapper.querySysPostInfo(idParamMap);
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < sysPostInfoList.size(); i++) {
			LinkedHashMap<String, Object> map = sysPostInfoList.get(i);
			stringBuilder.append(map.get("postName") + "·");
		}
		if (stringBuilder.length() > 0) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}
		return stringBuilder.toString();
	}

	/**
	 * 使用递归的方式查询所有父节点的id
	 * 
	 * @param id  父节点id
	 * @param ids 父节点id集
	 */
	private void getRecursiveIds(Long id, Set<Long> ids) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		List<LinkedHashMap<String, Object>> list = sysPostMapper.querySysPostInfo(paramMap);
		for (int i = 0; i < list.size(); i++) {
			for (Entry<String, Object> entry : list.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("parentId")) {
					ids.add(Long.valueOf(String.valueOf(value)));
					getRecursiveIds(Long.valueOf(String.valueOf(value)), ids);
				}
			}
		}
	}

	/**
	 * 删除用户与岗位关联信息
	 */
	@Override
	public void deleteSysUserPost(Long[] id) {
		sysUserPostMapper.deleteSysUserPost(id);
	}

}