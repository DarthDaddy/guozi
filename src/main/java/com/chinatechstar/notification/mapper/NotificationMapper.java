package com.chinatechstar.notification.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinatechstar.notification.entity.Notification;

/**
 * 消息通知的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface NotificationMapper {

	/**
	 * 查询消息通知分页
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryNotification(Map<String, Object> paramMap);

	/**
	 * 查询顶部消息通知的数据列表
	 * 
	 * @param sysUserId 用户ID
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryHeadNotification(Long sysUserId);

	/**
	 * 根据消息通知ID查询关联的用户ID
	 * 
	 * @param notificationId 消息通知ID
	 * @return
	 */
	List<Long> querySysUserId(Long notificationId);

	/**
	 * 新增消息通知
	 * 
	 * @param notification 消息通知对象
	 * @return
	 */
	int insertNotification(Notification notification);

	/**
	 * 新增消息通知与系统用户关联记录
	 * 
	 * @param id             消息通知与系统用户关联ID
	 * @param notificationId 消息通知ID
	 * @param sysUserId      发布对象的用户ID
	 * @return
	 */
	int insertNotificationSysUser(Long id, Long notificationId, Long sysUserId);

	/**
	 * 编辑消息通知
	 * 
	 * @param notification 消息通知对象
	 * @return
	 */
	int updateNotification(Notification notification);

	/**
	 * 删除消息通知
	 * 
	 * @param id 消息通知ID
	 * @return
	 */
	int deleteNotification(Long[] id);

	/**
	 * 删除消息通知与系统用户关联记录
	 * 
	 * @param notificationId 消息通知ID
	 * @return
	 */
	int deleteNotificationSysUser(Long notificationId);

    List<Notification> queryNotifications();
}
