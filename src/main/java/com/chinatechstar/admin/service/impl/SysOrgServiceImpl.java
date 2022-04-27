package com.chinatechstar.admin.service.impl;

import java.util.ArrayList;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.admin.entity.SysOrg;
import com.chinatechstar.admin.mapper.SysOrgMapper;
import com.chinatechstar.admin.mapper.SysRoleMapper;
import com.chinatechstar.admin.mapper.SysUserMapper;
import com.chinatechstar.admin.service.SysDictService;
import com.chinatechstar.admin.service.SysOrgService;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import com.chinatechstar.component.commons.utils.CollectionUtils;
import com.chinatechstar.component.commons.utils.CurrentUserUtils;
import com.chinatechstar.component.commons.utils.RecursiveListUtils;
import com.chinatechstar.component.commons.utils.SequenceGenerator;

/**
 * 机构信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class SysOrgServiceImpl implements SysOrgService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static SequenceGenerator sequenceGenerator = new SequenceGenerator();

	@Autowired
	private SysOrgMapper sysOrgMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private SysUserMapper sysUserMapper;

	/**
	 * 查询机构的数据分页
	 */
	@Override
	public Map<String, Object> querySysOrg(Integer currentPage, Integer pageSize, String orgName, String orgType, String orgDescription, Long id) {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("orgName", orgName);
		paramMap.put("orgType", orgType);
		paramMap.put("orgDescription", orgDescription);

		if (id != null) {
			Set<Long> ids = new HashSet<>();
			ids.add(id);
			getRecursiveIds(id, ids);
			paramMap.put("idArray", ids.stream().toArray(Long[]::new));
		}

		paramMap.put("tenantCode", CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码

		List<LinkedHashMap<String, Object>> totalList = sysOrgMapper.querySysOrg(paramMap);
		String roleData = sysRoleMapper.queryRoleData("sysorg", CurrentUserUtils.getOAuth2AuthenticationInfo().get("name"));
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			totalList = CollectionUtils.convertFilterList(totalList, roleDataArray);
		}

		if (StringUtils.isBlank(orgType) && StringUtils.isBlank(orgName) && StringUtils.isBlank(orgDescription) && (id == null)) {
			resultList = RecursiveListUtils.queryRecursiveList(totalList);
		} else {
			resultList.addAll(totalList);
		}
		return PaginationBuilder.buildResult(resultList, (long) resultList.size(), currentPage, pageSize);
	}

	/**
	 * 查询机构的树数据
	 */
	@Override
	public LinkedHashMap<String, Object> querySysOrgTree() {
		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
		List<LinkedHashMap<String, Object>> totalList = sysOrgMapper.querySysOrgTree(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		List<LinkedHashMap<String, Object>> resultList = RecursiveListUtils.queryRecursiveList(totalList);
		resultMap.put("list", resultList);
		return resultMap;
	}

	/**
	 * 查询机构类型的下拉框数据
	 */
	@Override
	public LinkedHashMap<String, Object> queryOrgType() {
		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
		List<LinkedHashMap<String, Object>> resultList = sysDictService.queryDictByDictType("org");
		resultMap.put("list", resultList);
		return resultMap;
	}

	/**
	 * 查询机构用户的树数据
	 */
	@Override
	public List<LinkedHashMap<String, Object>> queryOrgUserTree(Long roleId, Short assign) {
		List<LinkedHashMap<String, Object>> totalList = sysOrgMapper.queryOrgUserTree(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码

		// 获取机构和其包含的用户
		List<Long> orgIdList = sysOrgMapper.queryOrgId();
		Map<Long, String> orgUserMap = new HashMap<>();
		for (int i = 0; i < orgIdList.size(); i++) {
			Long orgId = orgIdList.get(i);
			List<String> userIdList = sysUserMapper.queryUserIdByOrgId(orgId, roleId, assign);
			orgUserMap.put(orgId, StringUtils.join(userIdList.toArray(), ","));
		}

		// 获取用户名和昵称，以用户名作为键
		List<LinkedHashMap<String, Object>> usernameNicknameList = sysUserMapper.queryUsernameNickname();
		Map<String, String> usernameNicknameMap = new HashMap<>();
		for (int i = 0; i < usernameNicknameList.size(); i++) {
			String usernameNicknameKey = null;
			String usernameNicknameValue = null;
			for (Entry<String, Object> entry : usernameNicknameList.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("value")) {
					usernameNicknameKey = value.toString();
				}
				if (key.equals("label")) {
					usernameNicknameValue = value.toString();
				}
			}
			usernameNicknameMap.put(usernameNicknameKey, usernameNicknameValue);
		}

		return RecursiveListUtils.queryOrgUserRecursiveList(totalList, orgUserMap, usernameNicknameMap);
	}

	/**
	 * 查询模型信息的机构用户的树数据
	 */
	@Override
	public List<LinkedHashMap<String, Object>> queryModelOrgUserTree(String modelId, Short assign) {
		List<LinkedHashMap<String, Object>> totalList = sysOrgMapper.queryOrgUserTree(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码

		// 获取机构和其包含的用户
		List<Long> orgIdList = sysOrgMapper.queryOrgId();
		Map<Long, String> orgUserMap = new HashMap<>();
		for (int i = 0; i < orgIdList.size(); i++) {
			Long orgId = orgIdList.get(i);
			List<String> userIdList = sysUserMapper.queryModelUserIdByOrgId(orgId, modelId, assign);
			orgUserMap.put(orgId, StringUtils.join(userIdList.toArray(), ","));
		}

		// 获取用户名和昵称，以用户名作为键
		List<LinkedHashMap<String, Object>> usernameNicknameList = sysUserMapper.queryUsernameNickname();
		Map<String, String> usernameNicknameMap = new HashMap<>();
		for (int i = 0; i < usernameNicknameList.size(); i++) {
			String usernameNicknameKey = null;
			String usernameNicknameValue = null;
			for (Entry<String, Object> entry : usernameNicknameList.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("value")) {
					usernameNicknameKey = value.toString();
				}
				if (key.equals("label")) {
					usernameNicknameValue = value.toString();
				}
			}
			usernameNicknameMap.put(usernameNicknameKey, usernameNicknameValue);
		}

		return RecursiveListUtils.queryOrgUserRecursiveList(totalList, orgUserMap, usernameNicknameMap);
	}

	/**
	 * 查询机构的导出数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> querySysOrgForExcel(Map<String, Object> paramMap) {
		return sysOrgMapper.querySysOrg(paramMap);
	}

	/**
	 * 新增机构
	 */
	@Override
	public void insertSysOrg(SysOrg sysOrg) {
		sysOrg.setId(sequenceGenerator.nextId());
		sysOrg.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		sysOrgMapper.insertSysOrg(sysOrg);
		logger.info("机构已新增： {}", sysOrg.getOrgName());
	}

	/**
	 * 编辑机构
	 */
	@Override
	public void updateSysOrg(SysOrg sysOrg) {
		if (sysOrg.getId().longValue() == sysOrg.getParentId().longValue()) {
			throw new IllegalArgumentException("当前节点不能作为自身的父节点");
		}
		sysOrgMapper.updateSysOrg(sysOrg);
		logger.info("机构已编辑： {}", sysOrg.getOrgName());
	}

	/**
	 * 删除机构
	 */
	@Override
	public void deleteSysOrg(Long[] id) {
		Set<Long> ids = new HashSet<>();
		for (int i = 0; i < id.length; i++) {
			ids.add(id[i]);
			getRecursiveIds(id[i], ids);
		}
		sysOrgMapper.deleteSysOrg(ids.stream().toArray(Long[]::new));
	}

	/**
	 * 使用递归的方式查询所有子节点的id
	 * 
	 * @param id  子节点id
	 * @param ids 子节点id集
	 */
	public void getRecursiveIds(Long id, Set<Long> ids) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("parentId", id);
		List<LinkedHashMap<String, Object>> list = sysOrgMapper.querySysOrg(paramMap);
		for (int i = 0; i < list.size(); i++) {
			for (Entry<String, Object> entry : list.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("id")) {
					ids.add(Long.valueOf(String.valueOf(value)));
					getRecursiveIds(Long.valueOf(String.valueOf(value)), ids);
				}
			}
		}
	}

}