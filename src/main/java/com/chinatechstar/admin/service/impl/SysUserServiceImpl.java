package com.chinatechstar.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.Map.Entry;
import java.util.Set;

import com.chinatechstar.admin.entity.*;
import com.chinatechstar.admin.mapper.*;
import com.chinatechstar.admin.service.SysTenantService;
import com.chinatechstar.cache.redis.constants.ApplicationConstants;
import com.chinatechstar.cache.redis.util.RedisUtils;
import com.chinatechstar.component.commons.utils.ExcelUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.admin.entity.SysUser;
import com.chinatechstar.admin.mapper.SysRoleMapper;
import com.chinatechstar.admin.mapper.SysUserMapper;
import com.chinatechstar.admin.service.SysOrgService;
import com.chinatechstar.admin.service.SysUserService;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import com.chinatechstar.component.commons.utils.CollectionUtils;
import com.chinatechstar.component.commons.utils.CurrentUserUtils;
import com.chinatechstar.component.commons.utils.SequenceGenerator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	private static SequenceGenerator sequenceGenerator = new SequenceGenerator();

	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private SysMenuMapper sysMenuMapper;
	@Autowired
	private SysPostMapper sysPostMapper;
	@Autowired
	private SysUrlMapper sysUrlMapper;
	@Autowired
	private SysOrgMapper sysOrgMapper;
	@Autowired
	private SysDictMapper sysDictMapper;
	@Autowired
	private SysUserPostMapper sysUserPostMapper;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private SysTenantService sysTenantService;
	@Autowired
	private SessionRegistry sessionRegistry;
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 查询在线用户分页
	 */
	@Override
	public Map<String, Object> queryOnlineSysUser(Integer currentPage, Integer pageSize, String username, String nickname, String mobile, Long[] roleId,
			Long orgId, String sorter) {
		List<Object> principals = sessionRegistry.getAllPrincipals();
		if (principals.isEmpty()) {
			org.springframework.security.core.userdetails.UserDetails user = new org.springframework.security.core.userdetails.User("admin",
					passwordEncoder.encode("666888"), AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
			principals.add(user);
		}
		List<String> usernameList = new ArrayList<>();
		for (Object principal : principals) {
			org.springframework.security.core.userdetails.UserDetails user = (UserDetails) principal;
			usernameList.add(user.getUsername());
		}

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("username", StringUtils.join(usernameList.toArray(), "|"));
		paramMap.put("nickname", nickname);
		paramMap.put("mobile", mobile);

		if (orgId != null) {
			Set<Long> orgIds = new HashSet<>();
			orgIds.add(orgId);
			sysOrgService.getRecursiveIds(orgId, orgIds);
			paramMap.put("orgIdArray", orgIds.stream().toArray(Long[]::new));
		}

		if (roleId != null && roleId.length > 0) {
			paramMap.put("roleId", roleId[0]);
		}

		paramMap.put("onlineusername", username);
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
		List<LinkedHashMap<String, Object>> resultList = sysUserMapper.querySysUser(paramMap);

		String roleData = sysRoleMapper.queryRoleData("onlinesysuser", CurrentUserUtils.getOAuth2AuthenticationInfo().get("name"), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), page.getTotal(), currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, page.getTotal(), currentPage, pageSize);
		}
	}

	/**
	 * 查询用户分页
	 */
	@Override
	public Map<String, Object> querySysUser(Integer currentPage, Integer pageSize, String username, String status, Long orgId, Long[] roleId, String nickname,
			String mobile, String tenantCode, String sorter) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("username", username);
		paramMap.put("nickname", nickname);
		paramMap.put("mobile", mobile);

		if (orgId != null) {
			Set<Long> orgIds = new HashSet<>();
			orgIds.add(orgId);
			sysOrgService.getRecursiveIds(orgId, orgIds);
			paramMap.put("orgIdArray", orgIds.stream().toArray(Long[]::new));
		}

		if (roleId != null && roleId.length > 0) {
			paramMap.put("roleId", roleId[0]);
		}

		if (StringUtils.isNotBlank(tenantCode)) {
			paramMap.put("tenantCode", tenantCode);// 租户编码参数
		} else {
			paramMap.put("tenantCode", CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		}
		if (StringUtils.isNotBlank(status)) {
			String[] statusArray = status.split(",");
			paramMap.put("statusList", (Short[]) ConvertUtils.convert(statusArray, Short.class));
		}
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
		List<LinkedHashMap<String, Object>> resultList = sysUserMapper.querySysUser(paramMap);

		String roleData = sysRoleMapper.queryRoleData("sysuser", CurrentUserUtils.getOAuth2AuthenticationInfo().get("name"), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), page.getTotal(), currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, page.getTotal(), currentPage, pageSize);
		}
	}

	/**
	 * 查询用户的导出数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> querySysUserForExcel(Map<String, Object> paramMap) {
		if (paramMap.get("onlinestatus") != null) {
			List<Object> principals = sessionRegistry.getAllPrincipals();
			if (principals.isEmpty()) {
				org.springframework.security.core.userdetails.UserDetails user = new org.springframework.security.core.userdetails.User("admin",
						passwordEncoder.encode("666888"), AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
				principals.add(user);
			}
			if (paramMap.get("username") != null && StringUtils.isNotBlank(paramMap.get("username").toString())) {
				paramMap.put("onlineusername", paramMap.get("username").toString());
			}
			List<String> usernameList = new ArrayList<>();
			for (Object principal : principals) {
				org.springframework.security.core.userdetails.UserDetails user = (UserDetails) principal;
				usernameList.add(user.getUsername());
			}
			paramMap.put("username", StringUtils.join(usernameList.toArray(), "|"));
		}

		if (paramMap.get("status") != null && StringUtils.isNotBlank(paramMap.get("status").toString())) {
			String[] statusArray = paramMap.get("status").toString().split(",");
			paramMap.put("statusList", (Short[]) ConvertUtils.convert(statusArray, Short.class));
		}
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		List<LinkedHashMap<String, Object>> dataList = sysUserMapper.querySysUser(paramMap);
		if (!org.apache.commons.collections.CollectionUtils.isEmpty(dataList)) {
			dataList.forEach(map -> {
				LinkedHashMap<String, Object> entity = new LinkedHashMap<>();
				for (Entry<String, Object> entry : map.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					if ("status".equals(key)) {
						value = Short.valueOf(value.toString()) == 1 ? "正常" : "禁用";
					}
					entity.put(key, value);
				}
				resultList.add(entity);
			});
		}
		return resultList;
	}

	/**
	 * 根据用户ID查询用户名的数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> queryUsername(Long[] id) {
		return sysUserMapper.queryUsername(id, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

	/**
	 * 根据用户名查询用户ID的数据列表
	 */
	@Override
	public List<Long> querySysUserId(String[] username) {
		return sysUserMapper.querySysUserId(username, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

	/**
	 * 注册用户并初始化
	 */
	@Override
	public void insertSysUserInitial(SysUser sysUser) {
		String smsCaptchaKey = ApplicationConstants.SMS_CAPTCHA_PREFIX + sysUser.getMobile();
		String smsCaptchaCache = redisUtils.get(smsCaptchaKey);
		if (!sysUser.getCaptcha().equals(smsCaptchaCache)) {
			throw new IllegalArgumentException("手机验证码错误");
		}
		redisUtils.del(smsCaptchaKey);

		Integer existing = sysUserMapper.getSysUserByIdentification(sysUser.getUsername().trim(), sysUser.getEmail().trim(), sysUser.getMobile().trim());
		if (existing != null && existing > 0) {
			throw new IllegalArgumentException("用户名或邮箱或手机号已存在");
		}
		Long userId = sequenceGenerator.nextId();
		sysUser.setId(userId);
		sysUser.setPassword(encoder.encode(sysUser.getPassword()));
		sysUser.setProvinceRegionCode("440000");
		sysUser.setCityRegionCode("440100");
		String tenantCode = String.valueOf(new Random().nextInt(99999999));
		sysUser.setTenantCode(tenantCode);
		sysUserMapper.insertSysUser(sysUser);

		// 新增租户
		SysTenant sysTenant = new SysTenant();
		sysTenant.setId(sequenceGenerator.nextId());
		sysTenant.setTenantCode(tenantCode);
		sysTenant.setTenantName(sysUser.getTenantName());
		sysTenantService.insertSysTenant(sysTenant);

		// 新增岗位
		SysPost sysPost = new SysPost();
		sysPost.setId(sequenceGenerator.nextId());
		String postCode = "OFFICE-" + tenantCode;
		sysPost.setPostCode(postCode);
		String postName = sysUser.getTenantName() + "管理员岗位";
		sysPost.setPostName(postName);
		sysPost.setParentId(0L);
		sysPost.setTenantCode(tenantCode);
		sysPostMapper.insertSysPost(sysPost);

		// 新增角色
		SysRole sysRole = new SysRole();
		Long roleId = sequenceGenerator.nextId();
		sysRole.setId(roleId);
		sysRole.setRoleCode("ROLE_ADMIN_" + tenantCode);
		sysRole.setRoleName(sysUser.getTenantName() + "管理员角色");
		sysRole.setTenantCode(tenantCode);
		sysRoleMapper.insertSysRole(sysRole);

		// 新增用户与岗位关联
		SysUserPost sysUserPost = new SysUserPost();
		sysUserPost.setId(sequenceGenerator.nextId());
		sysUserPost.setUserId(userId);
		sysUserPost.setPostCode(postCode);
		sysUserPost.setPostName(postName);
		sysUserPost.setPostType((short) 1);
		sysUserPost.setStatus((short) 1);
		sysUserPost.setTenantCode(tenantCode);
		sysUserPostMapper.insertSysUserPost(sysUserPost);

		// 新增用户、岗位与角色关联
		sysRoleMapper.insertSysRoleUser(sequenceGenerator.nextId(), roleId, userId, postCode, tenantCode);

		// 新增机构
		SysOrg sysOrg = new SysOrg();
		sysOrg.setId(sequenceGenerator.nextId());
		sysOrg.setOrgName(sysUser.getTenantName());
		sysOrg.setOrgType("company");
		sysOrg.setParentId(0L);
		sysOrg.setTenantCode(tenantCode);
		sysOrgMapper.insertSysOrg(sysOrg);

		// 新增菜单
		List<LinkedHashMap<String, Object>> menuTotalList = sysMenuMapper.querySysMenu(new HashMap<>());
		if (menuTotalList.size() > 0) {
			for (int i = 0; i < menuTotalList.size(); i++) {
				LinkedHashMap<String, Object> map = menuTotalList.get(i);
				SysMenu sysMenu = new SysMenu();
				Long menuId = sequenceGenerator.nextId();
				sysMenu.setId(menuId);
				sysMenu.setMenuCode(String.valueOf(map.get("menuCode")));
				sysMenu.setMenuName(String.valueOf(map.get("menuName")));
				sysMenu.setMenuIcon(String.valueOf(map.get("menuIcon")));
				sysMenu.setMenuPath(String.valueOf(map.get("menuPath")));
				sysMenu.setMenuComponent(String.valueOf(map.get("menuComponent")));
				sysMenu.setMenuSequence(Long.valueOf(String.valueOf(map.get("menuSequence"))));
				sysMenu.setMenuStatus(Short.valueOf(String.valueOf(map.get("menuStatus"))));
				sysMenu.setParentId(0L);
				sysMenu.setTenantCode(tenantCode);
				sysMenuMapper.insertSysMenu(sysMenu);

				// 新增角色与菜单关联
				sysMenuMapper.insertRoleIdMenuId(sequenceGenerator.nextId(), roleId, menuId, tenantCode);
			}
		}

		// 新增URL
		List<LinkedHashMap<String, Object>> urlTotalList = sysUrlMapper.querySysUrl(new HashMap<>());
		if (urlTotalList.size() > 0) {
			for (int i = 0; i < urlTotalList.size(); i++) {
				LinkedHashMap<String, Object> map = urlTotalList.get(i);
				SysUrl sysUrl = new SysUrl();
				Long urlId = sequenceGenerator.nextId();
				sysUrl.setId(urlId);
				sysUrl.setUrl(String.valueOf(map.get("url")));
				sysUrl.setDescription(String.valueOf(map.get("description")));
				sysUrl.setTenantCode(tenantCode);
				sysUrlMapper.insertSysUrl(sysUrl);

				// 新增URL与角色关联
				sysUrlMapper.insertUrlIdRoleId(Long.valueOf(sequenceGenerator.nextId()), urlId, roleId, tenantCode);
			}
		}

		// 新增字典
		List<LinkedHashMap<String, Object>> dictTotalList = sysDictMapper.querySysDict(new HashMap<>());
		if (dictTotalList.size() > 0) {
			for (int i = 0; i < dictTotalList.size(); i++) {
				LinkedHashMap<String, Object> map = dictTotalList.get(i);
				SysDict sysDict = new SysDict();
				sysDict.setId(sequenceGenerator.nextId());
				sysDict.setDictName(String.valueOf(map.get("dictName")));
				sysDict.setDictValue(String.valueOf(map.get("dictValue")));
				sysDict.setDictType(String.valueOf(map.get("dictType")));
				sysDict.setDictSequence(Long.valueOf(String.valueOf(map.get("dictSequence"))));
				sysDict.setParentId(0L);
				sysDict.setTenantCode(tenantCode);
				sysDictMapper.insertSysDict(sysDict);
			}
		}

		logger.info("用户已新增： {}", sysUser.getUsername());
	}

	/**
	 * 新增用户
	 */
	@Override
	public void insertSysUser(SysUser sysUser) {
		Integer existing = sysUserMapper.getSysUserByIdentification(sysUser.getUsername().trim(), sysUser.getEmail().trim(), sysUser.getMobile().trim());
		if (existing != null && existing > 0) {
			throw new IllegalArgumentException("用户名或邮箱或手机号已存在");
		}
		long userId = sequenceGenerator.nextId();
		sysUser.setId(userId);
		sysUser.setPassword(encoder.encode(sysUser.getPassword()));
		sysUser.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		sysUserMapper.insertSysUser(sysUser);
		if (sysUser.getRoleId() != null && sysUser.getRoleId().length > 0) {
			for (int i = 0; i < sysUser.getRoleId().length; i++) {
				sysRoleMapper.insertSysRoleUser(sequenceGenerator.nextId(), sysUser.getRoleId()[i], userId, "CTS-RD-04", CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
			}
		}
		logger.info("用户已新增： {}", sysUser.getUsername());
	}

	/**
	 * 将对应的角色授予用户
	 */
	@Override
	public void insertRoleIdUserId(Long[] roleId, Long userId, String postCode) {
		Set<Long> roleIdSet = new HashSet<>();
		for (int x = 0; x < roleId.length; x++) {
			roleIdSet.add(roleId[x]);
		}
		sysRoleMapper.deleteSysRoleUserPost(userId, postCode, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		Iterator<Long> iterator = roleIdSet.iterator();
		while (iterator.hasNext()) {
			Long roleIdData = iterator.next();
			sysRoleMapper.insertSysRoleUser(sequenceGenerator.nextId(), roleIdData, userId, postCode, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		}
	}

	/**
	 * 编辑用户
	 */
	@Override
	public void updateSysUser(SysUser sysUser) {
		Integer existing = sysUserMapper.getSysUserByIdEmailMobile(sysUser.getId(), sysUser.getEmail().trim(), sysUser.getMobile().trim(), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		if (existing != null && existing > 0) {
			throw new IllegalArgumentException("邮箱或手机号已存在");
		}
		if (StringUtils.isNotBlank(sysUser.getPassword())) {
			sysUser.setPassword(encoder.encode(sysUser.getPassword()));
		}
		sysUser.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		sysUserMapper.updateSysUser(sysUser);
		if (sysUser.getRoleId() != null && sysUser.getRoleId().length > 0) {
			sysRoleMapper.deleteSysRoleUser(sysUser.getId(), null, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
			for (int i = 0; i < sysUser.getRoleId().length; i++) {
				sysRoleMapper.insertSysRoleUser(sequenceGenerator.nextId(), sysUser.getRoleId()[i], sysUser.getId(), "CTS-RD-04", CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
			}
		}
		logger.info("用户已编辑： {}", sysUser.getUsername());
	}

	/**
	 * 删除用户
	 */
	@Override
	public void deleteSysUser(Long[] id) {
		if (ArrayUtils.contains(id, 1L)) {
			throw new IllegalArgumentException("系统管理员不能删除");
		}
		sysUserMapper.deleteSysUser(id, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

	/**
	 * 导入用户
	 */
	@Override
	public void importSysUser(MultipartFile file) {
		if (file.getOriginalFilename().toLowerCase().indexOf(".xlsx") == -1) {
			throw new IllegalArgumentException("请上传xlsx格式的文件");
		}
		List<Map<Integer, String>> listMap = ExcelUtils.readExcel(file);
		for (Map<Integer, String> data : listMap) {
			SysUser sysUser = new SysUser();
			sysUser.setUsername(data.get(0) == null ? "" : data.get(0));
			sysUser.setPassword(data.get(1) == null ? "" : data.get(1));
			sysUser.setEmail(data.get(2) == null ? "" : data.get(2));
			sysUser.setMobile(data.get(3) == null ? "" : data.get(3));
			sysUser.setPrefix(data.get(4) == null ? "" : data.get(4));
			sysUser.setOrgId(data.get(5) == null ? 0L : Long.valueOf(data.get(5)));
			sysUser.setStatus(data.get(6) == null ? 1 : Short.valueOf(data.get(6)));
			insertSysUser(sysUser);
		}
	}

}
