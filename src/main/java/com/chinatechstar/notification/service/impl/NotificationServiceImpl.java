package com.chinatechstar.notification.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatechstar.admin.controller.SysDictController;
import com.chinatechstar.admin.controller.SysRoleController;
import com.chinatechstar.admin.controller.SysUserController;
import com.chinatechstar.component.commons.result.PaginationBuilder;
import com.chinatechstar.component.commons.utils.CollectionUtils;
import com.chinatechstar.component.commons.utils.CurrentUserUtils;
import com.chinatechstar.component.commons.utils.SequenceGenerator;
import com.chinatechstar.notification.entity.Notification;
import com.chinatechstar.notification.mapper.NotificationMapper;
import com.chinatechstar.notification.service.NotificationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 消息通知的业务逻辑实现层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static SequenceGenerator sequenceGenerator = new SequenceGenerator();

	@Autowired
	private SysUserController sysUserServiceClient;

	@Autowired
	private SysDictController sysDictServiceClient;

	@Autowired
	private SysRoleController sysRoleServiceClient;

	@Autowired
	private NotificationMapper notificationMapper;

	/**
	 * 查询消息通知分页
	 */
	@Override
	public Map<String, Object> queryNotification(Integer currentPage, Integer pageSize, String title, String content, String type, String priority,
			String publisher, String username, String sorter) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("title", title);
		paramMap.put("content", content);
		paramMap.put("tenantCode", CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		paramMap.put("type", type);
		paramMap.put("priority", priority);
		paramMap.put("publisher", publisher);
		if (username != null) {
			List<Long> sysUserIdClientList = sysUserServiceClient.querySysUserIdByUsername(new String[] { username });
			paramMap.put("sysUserId", sysUserIdClientList.get(0));
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

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		List<LinkedHashMap<String, Object>> notificationList = notificationMapper.queryNotification(paramMap);
		for (int i = 0; i < notificationList.size(); i++) {
			LinkedHashMap<String, Object> notificationMap = notificationList.get(i);
			Iterator<Entry<String, Object>> iterator = notificationMap.entrySet().iterator();
			List<LinkedHashMap<String, Object>> usernameList = new ArrayList<>();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = iterator.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				if ("id".equals(key)) {
					List<Long> sysUserIdList = notificationMapper.querySysUserId(Long.valueOf(value.toString()), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
					usernameList = sysUserServiceClient.queryUsernameBySysUserId(sysUserIdList.stream().toArray(Long[]::new));
				}
			}
			notificationMap.put("username", CollectionUtils.convertList(usernameList));
			resultList.add(notificationMap);
		}

		String roleData = sysRoleServiceClient.queryRoleData("notification", CurrentUserUtils.getOAuth2AuthenticationInfo().get("name"));
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			return PaginationBuilder.buildResult(CollectionUtils.convertFilterList(resultList, roleDataArray), page.getTotal(), currentPage, pageSize);
		} else {
			return PaginationBuilder.buildResult(resultList, page.getTotal(), currentPage, pageSize);
		}
	}

	/**
	 * 查询顶部消息通知的数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> queryHeadNotification(String username) {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		List<Long> sysUserIdList = sysUserServiceClient.querySysUserIdByUsername(new String[] { username });
		List<LinkedHashMap<String, Object>> notificationList = notificationMapper.queryHeadNotification(sysUserIdList.get(0), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		for (int i = 0; i < notificationList.size(); i++) {
			LinkedHashMap<String, Object> notificationMap = notificationList.get(i);
			Iterator<Entry<String, Object>> iterator = notificationMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = iterator.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				if ("description".equals(key)) {
					entry.setValue(Jsoup.parse(value.toString()).text());
				}
			}
			resultList.add(notificationMap);
		}
		return resultList;
	}

	/**
	 * 查询类型的下拉框数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> queryNotificationType() {
		return sysDictServiceClient.queryDictByDictType("notification");
	}

	/**
	 * 查询优先级的下拉框数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> queryNotificationPriority() {
		return sysDictServiceClient.queryDictByDictType("priority");
	}

	/**
	 * 新增消息通知
	 */
	@Override
	public void insertNotification(Notification notification) {
		notification.setId(sequenceGenerator.nextId());
		notification.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		persistNotification(notification);
		notificationMapper.insertNotification(notification);
		logger.info("消息通知已新增： {}", notification.getTitle());
	}

	/**
	 * 编辑消息通知
	 */
	@Override
	public void updateNotification(Notification notification) {
		notification.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));// 当前用户的租户编码
		notificationMapper.updateNotification(notification);
		notificationMapper.deleteNotificationSysUser(notification.getId(), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		persistNotification(notification);
		logger.info("消息通知已编辑： {}", notification.getTitle());
	}

	/**
	 * 持久化消息通知
	 */
	private void persistNotification(Notification notification) {
		List<Long> sysUserIdList = null;
		if (notification.getUsername() != null && notification.getUsername().length > 0) {
			sysUserIdList = sysUserServiceClient.querySysUserIdByUsername(notification.getUsername());
		} else {
			sysUserIdList = sysUserServiceClient.querySysUserIdByUsername(null);// 查询全部用户ID
		}
		for (int i = 0; i < sysUserIdList.size(); i++) {
			notificationMapper.insertNotificationSysUser(sequenceGenerator.nextId(), notification.getId(), sysUserIdList.get(i), CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
		}
	}

	/**
	 * 删除消息通知
	 */
	@Override
	public void deleteNotification(Long[] id) {
        notificationMapper.deleteNotification(id, CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode"));
	}

	@Override
	public List<Notification> queryNotifications() {
		return notificationMapper.queryNotifications();
	}

}
