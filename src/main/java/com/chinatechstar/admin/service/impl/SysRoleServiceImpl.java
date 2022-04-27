package com.chinatechstar.admin.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.chinatechstar.component.commons.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.admin.entity.SysRole;
import com.chinatechstar.admin.mapper.SysRoleMapper;
import com.chinatechstar.admin.service.SysRoleService;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import com.chinatechstar.component.commons.utils.CollectionUtils;
import com.chinatechstar.component.commons.utils.CurrentUserUtils;
import com.chinatechstar.component.commons.utils.SequenceGenerator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.web.multipart.MultipartFile;

/**
 * 角色信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class SysRoleServiceImpl implements SysRoleService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static SequenceGenerator sequenceGenerator = new SequenceGenerator();

	@Autowired
	private SysRoleMapper sysRoleMapper;

	/**
	 * 查询角色分页
	 */
	@Override
	public Map<String, Object> querySysRole(Integer currentPage, Integer pageSize, String roleName, String roleCode, String roleDescription, String sorter) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("roleName", roleName);
		paramMap.put("roleCode", roleCode);
		paramMap.put("roleDescription", roleDescription);
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
		List<LinkedHashMap<String, Object>> resultList = sysRoleMapper.querySysRole(paramMap);

		String roleData = sysRoleMapper.queryRoleData("sysrole", CurrentUserUtils.getOAuth2AuthenticationInfo().get("name"), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), page.getTotal(), currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, page.getTotal(), currentPage, pageSize);
		}
	}

	/**
	 * 查询角色名称的下拉框数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> queryRoleName(Long userId, String postCode, Short assign) {
		return sysRoleMapper.queryRoleName(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"), userId, postCode, assign);// 当前用户的租户编码
	}

	/**
	 * 查询角色编码的下拉框数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> queryRoleCode() {
		return sysRoleMapper.queryRoleCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

	/**
	 * 查询角色的多选框数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> queryRoleNameCheckbox() {
		return sysRoleMapper.queryRoleNameCheckbox(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

	/**
	 * 查询角色的导出数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> querySysRoleForExcel(Map<String, Object> paramMap) {
		return sysRoleMapper.querySysRole(paramMap);
	}

	/**
	 * 新增角色
	 */
	@Override
	public void insertSysRole(SysRole sysRole) {
		Integer existing = sysRoleMapper.getSysRoleByRoleCode(sysRole.getRoleCode().trim(), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		if (existing != null && existing > 0) {
			throw new IllegalArgumentException("角色编码已存在");
		}
		sysRole.setId(sequenceGenerator.nextId());
		sysRole.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		sysRoleMapper.insertSysRole(sysRole);
		logger.info("角色已新增： {}", sysRole.getRoleCode());
	}

	/**
	 * 将对应的用户授权给角色
	 */
	@Override
	public void insertRoleIdUserId(Long roleId, String[][] userId) {
		Set<String> userIdSet = new HashSet<>();
		for (int x = 0; x < userId.length; x++) {
			for (int y = 0; y < userId[x].length; y++) {
				userIdSet.add(userId[x][y]);
			}
		}
		sysRoleMapper.deleteSysRoleUser(null, roleId, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		Iterator<String> iterator = userIdSet.iterator();
		while (iterator.hasNext()) {
			Long newUserId = null;
			String postCode = null;
			String[] userIdData;
			String data = iterator.next();
			if (StringUtils.contains(data, '|')) {
				userIdData = data.split("\\|");
				newUserId = Long.valueOf(userIdData[0]);
				postCode = userIdData[1];
			} else {
				newUserId = Long.valueOf(data);
			}
			sysRoleMapper.insertSysRoleUser(sequenceGenerator.nextId(), roleId, newUserId, postCode, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		}
	}

	/**
	 * 编辑角色
	 */
	@Override
	public void updateSysRole(SysRole sysRole) {
		sysRole.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		sysRoleMapper.updateSysRole(sysRole);
		logger.info("角色已编辑： {}", sysRole.getRoleCode());
	}

	/**
	 * 导入角色
	 */
	@Override
	public void importSysRole(MultipartFile file) {
		if (file.getOriginalFilename().toLowerCase().indexOf(".xlsx") == -1) {
			throw new IllegalArgumentException("请上传xlsx格式的文件");
		}
		List<Map<Integer, String>> listMap = ExcelUtils.readExcel(file);
		for (Map<Integer, String> data : listMap) {
			SysRole sysRole = new SysRole();
			sysRole.setRoleCode(data.get(0) == null ? "" : data.get(0));
			sysRole.setRoleName(data.get(1) == null ? "" : data.get(1));
			sysRole.setRoleDescription(data.get(2) == null ? "" : data.get(2));
			insertSysRole(sysRole);
		}
	}

	/**
	 * 删除角色
	 */
	@Override
	public void deleteSysRole(Long[] id) {
		sysRoleMapper.deleteSysRole(id, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		for (int i = 0; i < id.length; i++) {
			sysRoleMapper.deleteSysRoleUser(null, id[i], CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		}
	}

	/**
	 * 查询当前用户的过滤数据字段
	 */
	@Override
	public String queryRoleData(String menuCode, String username) {
		return sysRoleMapper.queryRoleData(menuCode, username, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

}
