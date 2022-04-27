package com.chinatechstar.admin.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinatechstar.admin.entity.SysMenu;

/**
 * 菜单信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface SysMenuService {

	/**
	 * 查询菜单的数据分页
	 * 
	 * @param currentPage 当前页数
	 * @param pageSize    每页记录数
	 * @param menuName    菜单名称
	 * @param menuPath    菜单路由
	 * @param menuCode    菜单编码
	 * @param menuIcon    菜单图标
	 * @param menuStatus  菜单状态 0：隐藏 1：显示
	 * @param id          菜单ID
	 * @return
	 */
	Map<String, Object> querySysMenu(Integer currentPage, Integer pageSize, String menuName, String menuPath, String menuCode, String menuIcon,
			Short menuStatus, Long id);

	/**
	 * 查询菜单的树数据
	 * 
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysMenuTree();

	/**
	 * 查询菜单按钮的树数据
	 */
	List<LinkedHashMap<String, Object>> querySysMenuButtonTree();

	/**
	 * 根据角色ID查询对应的菜单ID
	 * 
	 * @param roleId 角色ID
	 * @return
	 */
	List<String> queryMenuIdByRoleId(Long roleId);

	/**
	 * 根据角色编码查询对应的菜单按钮
	 */
	List<String> queryMenuButtonByRoleCode(String roleCode);

	/**
	 * 根据菜单编码查询对应的过滤数据字段
	 * 
	 * @param menuCode 菜单编码
	 * @return
	 */
	Map<String, Object> queryDataFieldByMenuCode(String menuCode);

	/**
	 * 查询菜单按钮的多选框数据列表
	 * 
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryMenuButtonCheckbox();

	/**
	 * 查询菜单包含的按钮
	 * 
	 * @param menuCode 菜单编码
	 * @return
	 */
	List<String> queryCheckedMenuButton(String menuCode);

	/**
	 * 查询菜单的导出数据列表
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysMenuForExcel(Map<String, Object> paramMap);

	/**
	 * 新增菜单
	 * 
	 * @param sysMenu 菜单对象
	 */
	void insertSysMenu(SysMenu sysMenu);

	/**
	 * 将对应的菜单授权给角色
	 * 
	 * @param roleId 角色ID
	 * @param menuId 菜单ID
	 */
	void insertRoleIdMenuId(Long roleId, Long[] menuId);

	/**
	 * 将对应的菜单按钮授权给角色
	 * 
	 * @param roleCode   角色编码
	 * @param menuButton 菜单按钮
	 */
	void insertRoleCodeMenuButton(String roleCode, String[] menuButton);

	/**
	 * 将对应的过滤数据字段授权给角色
	 * 
	 * @param roleCode  角色编码
	 * @param menuCode  菜单编码
	 * @param dataField 过滤数据字段
	 */
	void insertRoleCodeMenuData(String roleCode, String menuCode, String dataField);

	/**
	 * 编辑菜单
	 * 
	 * @param sysMenu 菜单对象
	 */
	void updateSysMenu(SysMenu sysMenu);

	/**
	 * 删除菜单
	 * 
	 * @param id 菜单ID
	 */
	void deleteSysMenu(Long[] id);

	/**
	 * 删除过滤数据字段
	 * 
	 * @param roleCode 角色编码
	 * @param menuCode 菜单编码
	 */
	void deleteDataField(String roleCode, String menuCode);

}
