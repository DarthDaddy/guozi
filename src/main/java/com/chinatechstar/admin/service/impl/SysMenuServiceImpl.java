package com.chinatechstar.admin.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.chinatechstar.component.commons.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.admin.entity.SysMenu;
import com.chinatechstar.admin.mapper.SysMenuMapper;
import com.chinatechstar.admin.mapper.SysRoleMapper;
import com.chinatechstar.admin.service.SysDictService;
import com.chinatechstar.admin.service.SysMenuService;
import com.chinatechstar.component.commons.utils.CurrentUserUtils;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import org.springframework.web.multipart.MultipartFile;

/**
 * 菜单信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class SysMenuServiceImpl implements SysMenuService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static SequenceGenerator sequenceGenerator = new SequenceGenerator();

	@Autowired
	private SysMenuMapper sysMenuMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private SysDictService sysDictService;

	/**
	 * 查询菜单的数据分页
	 */
	@Override
	public Map<String, Object> querySysMenu(Integer currentPage, Integer pageSize, String menuName, String menuPath, String menuCode, String menuIcon,
			Short menuStatus, Long id) {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("menuName", menuName);
		paramMap.put("menuPath", menuPath);
		paramMap.put("menuCode", menuCode);
		paramMap.put("menuIcon", menuIcon);
		paramMap.put("menuStatus", menuStatus);

		if (id != null) {
			Set<Long> ids = new HashSet<>();
			ids.add(id);
			getRecursiveIds(id, ids);
			paramMap.put("idArray", ids.stream().toArray(Long[]::new));
		}

		paramMap.put("tenantCode", CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码

		List<LinkedHashMap<String, Object>> totalList = sysMenuMapper.querySysMenu(paramMap);
		String roleData = sysRoleMapper.queryRoleData("sysmenu", CurrentUserUtils.getOAuth2AuthenticationInfo().get("name"), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			totalList = CollectionUtils.convertFilterList(totalList, roleDataArray);
		}

		if (StringUtils.isBlank(menuName) && StringUtils.isBlank(menuPath) && StringUtils.isBlank(menuCode) && StringUtils.isBlank(menuIcon)
				&& (menuStatus == null) && (id == null)) {
			resultList = RecursiveListUtils.queryRecursiveList(totalList);
		} else {
			resultList.addAll(totalList);
		}
		return PaginationBuilder.buildResult(resultList, (long) resultList.size(), currentPage, pageSize);
	}

	/**
	 * 根据菜单编码查询对应的过滤数据字段
	 */
	@Override
	public Map<String, Object> queryDataFieldByMenuCode(String menuCode) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("menuCode", menuCode);
		List<LinkedHashMap<String, Object>> resultList = sysMenuMapper.queryDataFieldByMenuCode(paramMap);
		resultMap.put("list", resultList);
		resultMap.put("count", resultList.size());
		return resultMap;
	}

	/**
	 * 查询菜单的树数据
	 */
	@Override
	public List<LinkedHashMap<String, Object>> querySysMenuTree() {
		List<LinkedHashMap<String, Object>> totalList = sysMenuMapper.querySysMenuTree(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		return RecursiveListUtils.queryRecursiveList(totalList);
	}

	/**
	 * 查询菜单按钮的树数据
	 */
	@Override
	public List<LinkedHashMap<String, Object>> querySysMenuButtonTree() {
		List<LinkedHashMap<String, Object>> totalList = sysMenuMapper.querySysMenuTree(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码

		// 获取菜单和其包含的按钮
		List<String> menuCodeList = sysMenuMapper.queryMenuCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		Map<String, String> menuButtonMap = new HashMap<>();
		for (int i = 0; i < menuCodeList.size(); i++) {
			String menuCode = menuCodeList.get(i);
			List<String> menuButtonList = sysMenuMapper.queryMenuButton(menuCode, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
			menuButtonMap.put(menuCode, StringUtils.join(menuButtonList.toArray(), ","));
		}

		// 获取按钮的字典名称和值，以值作为键
		List<LinkedHashMap<String, Object>> buttonDictList = sysDictService.queryDictByDictTypeCheckbox("button");
		Map<String, String> buttonDictMap = new HashMap<>();
		for (int i = 0; i < buttonDictList.size(); i++) {
			String buttonDictKey = null;
			String buttonDictValue = null;
			for (Entry<String, Object> entry : buttonDictList.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("value")) {
					buttonDictKey = value.toString();
				}
				if (key.equals("label")) {
					buttonDictValue = value.toString();
				}
			}
			buttonDictMap.put(buttonDictKey, buttonDictValue);
		}

		return RecursiveListUtils.queryMenuButtonRecursiveList(totalList, menuButtonMap, buttonDictMap);
	}

	/**
	 * 根据角色ID查询对应的菜单ID
	 */
	@Override
	public List<String> queryMenuIdByRoleId(Long roleId) {
		return sysMenuMapper.queryMenuIdByRoleId(roleId, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

	/**
	 * 根据角色编码查询对应的菜单按钮
	 */
	@Override
	public List<String> queryMenuButtonByRoleCode(String roleCode) {
		return sysMenuMapper.queryMenuButtonByRoleCode(roleCode, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

	/**
	 * 查询菜单按钮的多选框数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> queryMenuButtonCheckbox() {
		return sysDictService.queryDictByDictTypeCheckbox("button");
	}

	/**
	 * 查询菜单包含的按钮
	 */
	@Override
	public List<String> queryCheckedMenuButton(String menuCode) {
		return sysMenuMapper.queryCheckedMenuButton(menuCode, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

	/**
	 * 查询菜单的导出数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> querySysMenuForExcel(Map<String, Object> paramMap) {
		return sysMenuMapper.querySysMenu(paramMap);
	}

	/**
	 * 新增菜单
	 */
	@Override
	public void insertSysMenu(SysMenu sysMenu) {
		Integer existingMenuCode = sysMenuMapper.getSysMenuByMenuCode(sysMenu.getMenuCode(), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		if (existingMenuCode != null && existingMenuCode > 0) {
			throw new IllegalArgumentException("菜单编码已存在");
		}
		Integer existingMenuPath = sysMenuMapper.getSysMenuByIdMenuPath(null, sysMenu.getMenuPath(), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		if (existingMenuPath != null && existingMenuPath > 0) {
			throw new IllegalArgumentException("菜单路由已存在");
		}
		sysMenu.setId(sequenceGenerator.nextId());
		sysMenu.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		sysMenuMapper.insertSysMenu(sysMenu);

		String[] menuButton = sysMenu.getMenuButton();
		if (menuButton != null) {
			for (int i = 0; i < menuButton.length; i++) {
				sysMenuMapper.insertMenuButton(sequenceGenerator.nextId(), sysMenu.getMenuCode(), menuButton[i], CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
			}
		}

		logger.info("菜单已新增： {}", sysMenu.getMenuCode());
	}

	/**
	 * 将对应的菜单授权给角色
	 */
	@Override
	public void insertRoleIdMenuId(Long roleId, Long[] menuId) {
		sysMenuMapper.deleteRoleMenu(roleId, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		if (menuId != null && menuId.length > 0) {
			Set<Long> parentIdSet = sysMenuMapper.queryParentIdById(menuId, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));

			if (parentIdSet != null && parentIdSet.size() > 0) {
				Set<Long> ids = new HashSet<>();
				ids.addAll(parentIdSet);
				getRecursiveMenuIds(parentIdSet, ids);
				parentIdSet.addAll(ids);
			}

			parentIdSet.remove(0L);
			parentIdSet.addAll(Arrays.stream(menuId).collect(Collectors.toSet()));
			Long[] newMenuId = parentIdSet.stream().toArray(Long[]::new);
			for (int i = 0; i < newMenuId.length; i++) {
				sysMenuMapper.insertRoleIdMenuId(Long.valueOf(sequenceGenerator.nextId()), roleId, newMenuId[i], CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
			}
		}
	}

	/**
	 * 使用递归的方式查询所有父节点的id
	 * 
	 * @param parentIdSet 父节点id集
	 * @param ids         子节点id集
	 */
	private void getRecursiveMenuIds(Set<Long> parentIdSet, Set<Long> ids) {
		Set<Long> newParentIdSet = sysMenuMapper.queryParentIdById(parentIdSet.stream().toArray(Long[]::new), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		Iterator<Long> iterator = newParentIdSet.iterator();
		while (iterator.hasNext()) {
			Long data = iterator.next();
			ids.add(data);
			Set<Long> dataSet = new HashSet<>();
			dataSet.add(data);
			getRecursiveMenuIds(dataSet, ids);
		}
	}

	/**
	 * 将对应的菜单按钮授权给角色
	 */
	@Override
	public void insertRoleCodeMenuButton(String roleCode, String[] menuButton) {
		sysMenuMapper.deleteRoleMenuButton(roleCode, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		if (menuButton != null) {
			for (int i = 0; i < menuButton.length; i++) {
				if (!menuButton[i].contains(":")) {// 排除非菜单按钮节点
					continue;
				}
				sysMenuMapper.insertRoleCodeMenuButton(Long.valueOf(sequenceGenerator.nextId()), roleCode, menuButton[i], CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
			}
		}
	}

	/**
	 * 将对应的过滤数据字段授权给角色
	 */
	@Override
	public void insertRoleCodeMenuData(String roleCode, String menuCode, String dataField) {
		sysMenuMapper.deleteRoleMenuData(roleCode, menuCode, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		sysMenuMapper.insertRoleCodeMenuData(Long.valueOf(sequenceGenerator.nextId()), roleCode, menuCode, dataField, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

	/**
	 * 编辑菜单
	 */
	@Override
	public void updateSysMenu(SysMenu sysMenu) {
		if (sysMenu.getId().longValue() == sysMenu.getParentId().longValue()) {
			throw new IllegalArgumentException("当前节点不能作为自身的父节点");
		}
		Integer existing = sysMenuMapper.getSysMenuByIdMenuPath(sysMenu.getId(), sysMenu.getMenuPath().trim(), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		if (existing != null && existing > 0) {
			throw new IllegalArgumentException("菜单路由已存在");
		}
		sysMenu.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		sysMenuMapper.updateSysMenu(sysMenu);

		String[] menuButton = sysMenu.getMenuButton();
		if (menuButton != null) {
			sysMenuMapper.deleteButtonByMenuCode(sysMenu.getMenuCode(), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
			for (int i = 0; i < menuButton.length; i++) {
				sysMenuMapper.insertMenuButton(sequenceGenerator.nextId(), sysMenu.getMenuCode(), menuButton[i], CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
			}
		}

		logger.info("菜单已编辑： {}", sysMenu.getMenuCode());
	}

	/**
	 * 删除菜单
	 */
	@Override
	public void deleteSysMenu(Long[] id) {
		Set<Long> ids = new HashSet<>();
		for (int i = 0; i < id.length; i++) {
			ids.add(id[i]);
			getRecursiveIds(id[i], ids);
		}
		sysMenuMapper.deleteSysMenu(ids.stream().toArray(Long[]::new), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

	/**
	 * 使用递归的方式查询所有子节点的id
	 * 
	 * @param id  子节点id
	 * @param ids 子节点id集
	 */
	private void getRecursiveIds(Long id, Set<Long> ids) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("parentId", id);
		List<LinkedHashMap<String, Object>> list = sysMenuMapper.querySysMenu(paramMap);
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

	/**
	 * 删除过滤数据字段
	 */
	@Override
	public void deleteDataField(String roleCode, String menuCode) {
		sysMenuMapper.deleteRoleMenuData(roleCode, menuCode, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

	/**
	 * 导入菜单
	 */
	@Override
	public void importSysMenu(MultipartFile file) {
		if (file.getOriginalFilename().toLowerCase().indexOf(".xlsx") == -1) {
			throw new IllegalArgumentException("请上传xlsx格式的文件");
		}
		List<Map<Integer, String>> listMap = ExcelUtils.readExcel(file);
		for (Map<Integer, String> data : listMap) {
			SysMenu sysMenu = new SysMenu();
			sysMenu.setMenuCode(data.get(0) == null ? "" : data.get(0));
			sysMenu.setMenuName(data.get(1) == null ? "" : data.get(1));
			sysMenu.setMenuIcon(data.get(2) == null ? "" : data.get(2));
			sysMenu.setMenuPath(data.get(3) == null ? "" : data.get(3));
			sysMenu.setMenuComponent(data.get(4) == null ? "" : data.get(4));
			sysMenu.setMenuSequence(data.get(5) == null ? 0L : Long.valueOf(data.get(5)));
			sysMenu.setMenuStatus(data.get(6) == null ? 0 : Short.valueOf(data.get(6)));
			sysMenu.setParentId(data.get(7) == null ? 0L : Long.valueOf(data.get(7)));
			insertSysMenu(sysMenu);
		}
	}

}