package com.chinatechstar.admin.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinatechstar.component.commons.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.admin.entity.SysUrl;
import com.chinatechstar.admin.mapper.SysRoleMapper;
import com.chinatechstar.admin.mapper.SysUrlMapper;
import com.chinatechstar.admin.service.SysUrlService;
import com.chinatechstar.cache.redis.constants.ApplicationConstants;
import com.chinatechstar.cache.redis.util.RedisUtils;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import com.chinatechstar.component.commons.utils.CollectionUtils;
import com.chinatechstar.component.commons.utils.CurrentUserUtils;
import com.chinatechstar.component.commons.utils.SequenceGenerator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.web.multipart.MultipartFile;

/**
 * 接口信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class SysUrlServiceImpl implements SysUrlService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static SequenceGenerator sequenceGenerator = new SequenceGenerator();

	@Autowired
	private SysUrlMapper sysUrlMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private RedisUtils redisUtils;

	/**
	 * 查询接口分页
	 */
	@Override
	public Map<String, Object> querySysUrl(Integer currentPage, Integer pageSize, String url, String description, String menuCode, Long[] roleId,
			String sorter) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("url", url);
		paramMap.put("description", description);
		paramMap.put("menuCode", menuCode);
		paramMap.put("roleId", roleId);
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
		List<LinkedHashMap<String, Object>> resultList = sysUrlMapper.querySysUrl(paramMap);

		String roleData = sysRoleMapper.queryRoleData("sysurl", CurrentUserUtils.getOAuth2AuthenticationInfo().get("name"), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), page.getTotal(), currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, page.getTotal(), currentPage, pageSize);
		}
	}

	/**
	 * 查询接口的导出数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> querySysUrlForExcel(Map<String, Object> paramMap) {
		return sysUrlMapper.querySysUrl(paramMap);
	}

	/**
	 * 根据接口ID查询对应的角色ID
	 */
	@Override
	public List<String> queryRoleIdByUrlId(Long urlId) {
		return sysUrlMapper.queryRoleIdByUrlId(urlId, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

	/**
	 * 根据角色ID查询对应的接口ID
	 */
	@Override
	public List<String> queryUrlIdByRoleId(Long roleId) {
		return sysUrlMapper.queryUrlIdByRoleId(roleId, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

	/**
	 * 新增接口
	 */
	@Override
	public void insertSysUrl(SysUrl sysUrl) {
		Integer existing = sysUrlMapper.getSysUrlByUrl(sysUrl.getUrl().trim(), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		if (existing != null && existing > 0) {
			throw new IllegalArgumentException("接口已存在");
		}
		sysUrl.setId(sequenceGenerator.nextId());
		sysUrl.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		sysUrlMapper.insertSysUrl(sysUrl);
		logger.info("接口已新增： {}", sysUrl.getUrl());
	}

	/**
	 * 将接口授权给角色
	 */
	@Override
	public void insertUrlIdRoleId(Long urlId, Long[] roleId) {
		sysUrlMapper.deleteUrlRole(urlId, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		for (int i = 0; i < roleId.length; i++) {
			sysUrlMapper.insertUrlIdRoleId(Long.valueOf(sequenceGenerator.nextId()), urlId, roleId[i], CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		}
		refreshRedis();
	}

	/**
	 * 将接口授权给角色
	 */
	@Override
	public void insertRoleIdUrlId(Long[] urlId, Long roleId) {
		sysUrlMapper.deleteRoleUrl(roleId, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		for (int i = 0; i < urlId.length; i++) {
			sysUrlMapper.insertUrlIdRoleId(Long.valueOf(sequenceGenerator.nextId()), urlId[i], roleId, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		}
		refreshRedis();
	}

	/**
	 * 刷新Redis里的缓存数据
	 */
	private void refreshRedis() {
		List<String> roleCodeList = sysRoleMapper.queryRoleCodeList();
		for (int i = 0; i < roleCodeList.size(); i++) {
			String roleCode = roleCodeList.get(i);
			List<String> url = sysUrlMapper.queryRoleUrl(roleCode);
			redisUtils.psetex(ApplicationConstants.URL_ROLECODE_PREFIX + roleCode, url == null ? Collections.emptyList().toString() : url.toString());
		}
	}

	/**
	 * 编辑接口
	 */
	@Override
	public void updateSysUrl(SysUrl sysUrl) {
		sysUrl.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		sysUrlMapper.updateSysUrl(sysUrl);
		logger.info("接口已编辑： {}", sysUrl.getUrl());
	}

	/**
	 * 删除接口
	 */
	@Override
	public void deleteSysUrl(Long[] id) {
		sysUrlMapper.deleteSysUrl(id, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

	/**
	 * 导入接口
	 */
	@Override
	public void importSysUrl(MultipartFile file) {
		if (file.getOriginalFilename().toLowerCase().indexOf(".xlsx") == -1) {
			throw new IllegalArgumentException("请上传xlsx格式的文件");
		}
		List<Map<Integer, String>> listMap = ExcelUtils.readExcel(file);
		for (Map<Integer, String> data : listMap) {
			SysUrl sysUrl = new SysUrl();
			sysUrl.setUrl(data.get(0) == null ? "" : data.get(0));
			sysUrl.setDescription(data.get(1) == null ? "" : data.get(1));
			insertSysUrl(sysUrl);
		}
	}

}
