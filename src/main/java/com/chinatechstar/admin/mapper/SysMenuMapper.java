package com.chinatechstar.admin.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import com.chinatechstar.admin.entity.SysMenu;

/**
 * 菜单信息的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface SysMenuMapper {

	/**
	 * 查询菜单分页或导出数据
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysMenu(Map<String, Object> paramMap);

	/**
	 * 查询菜单的树数据列表
	 * 
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysMenuTree(@Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询菜单编码
	 *
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<String> queryMenuCode(@Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询菜单包含的按钮数据列表
	 *
	 * @param menuCode   菜单编码
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<String> queryMenuButton(@Param(value = "menuCode") String menuCode, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据角色ID查询对应的菜单ID
	 *
	 * @param roleId     角色ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<String> queryMenuIdByRoleId(@Param(value = "roleId") Long roleId, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据角色编码查询对应的菜单按钮
	 *
	 * @param roleCode   角色编码
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<String> queryMenuButtonByRoleCode(@Param(value = "roleCode") String roleCode, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询菜单包含的按钮
	 *
	 * @param menuCode   菜单编码
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<String> queryCheckedMenuButton(@Param(value = "menuCode") String menuCode, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据菜单编码查询对应的过滤数据字段
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryDataFieldByMenuCode(Map<String, Object> paramMap);

	/**
	 * 查询是否已存在此菜单编码
	 *
	 * @param menuCode   菜单编码
	 * @param tenantCode 租户编码
	 * @return
	 */
	Integer getSysMenuByMenuCode(@Param(value = "menuCode") String menuCode, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询是否已存在此菜单路由
	 *
	 * @param id         菜单ID
	 * @param menuPath   菜单路由
	 * @param tenantCode 租户编码
	 * @return
	 */
	Integer getSysMenuByIdMenuPath(@Param(value = "id") Long id, @Param(value = "menuPath") String menuPath, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询授权菜单的父节点ID
	 *
	 * @param id         菜单ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	Set<Long> queryParentIdById(@Param(value = "array") Long[] id, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 新增菜单
	 * 
	 * @param sysMenu 菜单对象
	 * @return
	 */
	int insertSysMenu(SysMenu sysMenu);

	/**
	 * 将对应的菜单授权给角色
	 *
	 * @param id         角色与菜单关联ID
	 * @param roleId     角色ID
	 * @param menuId     菜单ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int insertRoleIdMenuId(@Param(value = "id") Long id, @Param(value = "roleId") Long roleId, @Param(value = "menuId") Long menuId, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 将对应的菜单按钮授权给角色
	 *
	 * @param id         角色与菜单按钮关联ID
	 * @param roleCode   角色编码
	 * @param menuButton 菜单按钮
	 * @param tenantCode 租户编码
	 * @return
	 */
	int insertRoleCodeMenuButton(@Param(value = "id") Long id, @Param(value = "roleCode") String roleCode, @Param(value = "menuButton") String menuButton, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 将对应的过滤数据字段授权给角色
	 *
	 * @param id         角色与过滤数据关联ID
	 * @param roleCode   角色编码
	 * @param menuCode   菜单编码
	 * @param dataField  过滤数据
	 * @param tenantCode 租户编码
	 * @return
	 */
	int insertRoleCodeMenuData(@Param(value = "id") Long id, @Param(value = "roleCode") String roleCode, @Param(value = "menuCode") String menuCode, @Param(value = "dataField") String dataField, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 新增菜单按钮
	 *
	 * @param id         菜单与按钮关联ID
	 * @param menuCode   菜单编码
	 * @param menuButton 按钮
	 * @param tenantCode 租户编码
	 * @return
	 */
	int insertMenuButton(@Param(value = "id") Long id, @Param(value = "menuCode") String menuCode, @Param(value = "menuButton") String menuButton, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 编辑菜单
	 * 
	 * @param sysMenu 菜单对象
	 * @return
	 */
	int updateSysMenu(SysMenu sysMenu);

	/**
	 * 删除菜单
	 *
	 * @param id         菜单ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteSysMenu(@Param(value = "array") Long[] id, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据角色ID删除角色与菜单关联信息
	 *
	 * @param roleId     角色ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteRoleMenu(@Param(value = "roleId") Long roleId, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据角色编码删除角色与菜单按钮关联信息
	 *
	 * @param roleCode   角色编码
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteRoleMenuButton(@Param(value = "roleCode") String roleCode, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据角色编码、菜单编码删除角色与过滤数据关联信息
	 *
	 * @param roleCode   角色编码
	 * @param menuCode   菜单编码
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteRoleMenuData(@Param(value = "roleCode") String roleCode, @Param(value = "menuCode") String menuCode, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 根据菜单编码删除菜单与按钮关联信息
	 *
	 * @param menuCode   菜单编码
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteButtonByMenuCode(@Param(value = "menuCode") String menuCode, @Param(value = "tenantCode") String tenantCode);

}
