package com.chinatechstar.notification.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinatechstar.notification.entity.Notification;

/**
 * 消息通知的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface NotificationService {

	/**
	 * 查询消息通知分页
	 * 
	 * @param currentPage 当前页数
	 * @param pageSize    每页记录数
	 * @param title       标题
	 * @param content     内容
	 * @param type        类型
	 * @param priority    优先级
	 * @param publisher   发布者
	 * @param username    用户名
	 * @param sorter      排序
	 * @return
	 */
	Map<String, Object> queryNotification(Integer currentPage, Integer pageSize, String title, String content, String type, String priority, String publisher,
			String username, String sorter);

	/**
	 * 查询顶部消息通知的数据列表
	 * 
	 * @param username 用户名
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryHeadNotification(String username);

	/**
	 * 查询类型的下拉框数据列表
	 * 
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryNotificationType();

	/**
	 * 查询优先级的下拉框数据列表
	 * 
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryNotificationPriority();

	/**
	 * 新增消息通知
	 * 
	 * @param notification 消息通知对象
	 */
	void insertNotification(Notification notification);

	/**
	 * 编辑消息通知
	 * 
	 * @param notification 消息通知对象
	 */
	void updateNotification(Notification notification);

	/**
	 * 删除消息通知
	 * 
	 * @param id 消息通知ID
	 */
	void deleteNotification(Long[] id);

	List<Notification> queryNotifications();
}
