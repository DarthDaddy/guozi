package com.chinatechstar.admin.service.impl;

import java.util.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinatechstar.admin.vo.SysTenantVO;
import com.chinatechstar.component.commons.utils.CurrentUserUtils;
import com.chinatechstar.file.vo.FileVO;
import com.chinatechstar.component.commons.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.admin.entity.SysTenant;
import com.chinatechstar.admin.mapper.SysRoleMapper;
import com.chinatechstar.admin.mapper.SysTenantMapper;
import com.chinatechstar.admin.service.SysTenantService;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import com.chinatechstar.component.commons.utils.CollectionUtils;
import com.chinatechstar.component.commons.utils.SequenceGenerator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.web.multipart.MultipartFile;

/**
 * 租户信息的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class SysTenantServiceImpl implements SysTenantService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static SequenceGenerator sequenceGenerator = new SequenceGenerator();

	@Autowired
	private SysTenantMapper sysTenantMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;

	/**
	 * 查询租户分页
	 */
	@Override
	public Map<String, Object> querySysTenant(Integer currentPage, Integer pageSize, String tenantCode, String tenantName, String tenantContact,
			String tenantEmail, String tenantTel, String sorter) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantCode", tenantCode);
		paramMap.put("tenantName", tenantName);
		paramMap.put("tenantContact", tenantContact);
		paramMap.put("tenantEmail", tenantEmail);
		paramMap.put("tenantTel", tenantTel);
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
		List<LinkedHashMap<String, Object>> resultList = sysTenantMapper.querySysTenant(paramMap);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String roleData = sysRoleMapper.queryRoleData("systenant", authentication.getName(), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), page.getTotal(), currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, page.getTotal(), currentPage, pageSize);
		}
	}

	/**
	 * 查询租户的导出数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> querySysTenantForExcel(Map<String, Object> paramMap) {
		return sysTenantMapper.querySysTenant(paramMap);
	}

	/**
	 * 新增租户
	 */
	@Override
	public void insertSysTenant(SysTenant sysTenant) {
		Integer existing = sysTenantMapper.getSysTenantByTenantCode(sysTenant.getId(), sysTenant.getTenantCode().trim());
		if (existing != null && existing > 0) {
			throw new IllegalArgumentException("租户编码已存在");
		}
		sysTenant.setId(sequenceGenerator.nextId());
		sysTenantMapper.insertSysTenant(sysTenant);
		logger.info("租户已新增： {}", sysTenant.getTenantName());
	}

	/**
	 * 编辑租户
	 */
	@Override
	public void updateSysTenant(SysTenant sysTenant) {
		sysTenantMapper.updateSysTenant(sysTenant);
		logger.info("租户已编辑： {}", sysTenant.getTenantName());
	}

	/**
	 * 删除租户
	 */
	@Override
	public void deleteSysTenant(Long[] id) {
		sysTenantMapper.deleteSysTenant(id);
	}

	/**
	 * 导入租户
	 */
	@Override
	public void importSysTenant(MultipartFile file) {
		if (file.getOriginalFilename().toLowerCase().indexOf(".xlsx") == -1) {
			throw new IllegalArgumentException("请上传xlsx格式的文件");
		}
		List<Map<Integer, String>> listMap = ExcelUtils.readExcel(file);
		for (Map<Integer, String> data : listMap) {
			SysTenant sysTenant = new SysTenant();
			sysTenant.setTenantCode(data.get(0) == null ? "" : data.get(0));
			sysTenant.setTenantName(data.get(1) == null ? "" : data.get(1));
			sysTenant.setTenantContact(data.get(2) == null ? "" : data.get(2));
			sysTenant.setTenantEmail(data.get(3) == null ? "" : data.get(3));
			sysTenant.setTenantTel(data.get(4) == null ? "" : data.get(4));
			insertSysTenant(sysTenant);
		}
	}

	@Override
	public List<SysTenantVO> querySysTenants() {
		return sysTenantMapper.querySysTenants();
	}

	@Override
	public List<SysTenant> querySysTenantByCurrent(String tenantCode) {
		return sysTenantMapper.querySysTenantByCurrent(tenantCode);
	}

	@Override
	public List<SysTenant> querySysTenantList() {
		return sysTenantMapper.querySysTenantList();
	}

	@Override
	public List<SysTenantVO> querySysTenantVo() {
		String tenantCode = CurrentUserUtils.getOAuth2AuthenticationInfo ().get ( "tenantCode" );
		SysTenantVO sysTenantVOS = sysTenantMapper.querySysTenantVo ( tenantCode );
		List<FileVO> fileVOS = sysTenantMapper.queryFileListByTenantCode(tenantCode);
		sysTenantVOS.setFileList ( fileVOS );
		List<SysTenantVO> sysTenantVOSList= new ArrayList<> ();
		sysTenantVOSList.add ( sysTenantVOS );
		return sysTenantVOSList;
	}

}
