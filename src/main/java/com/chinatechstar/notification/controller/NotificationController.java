package com.chinatechstar.notification.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;
import com.chinatechstar.notification.entity.Notification;
import com.chinatechstar.notification.service.NotificationService;
import com.chinatechstar.notification.vo.NotificationVO;

/**
 * 消息通知的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;


	/**
	 * 查找首页的咨询信息
	 *
	 * @param notificationVO 咨询前端参数
	 * @return
	 */
	@GetMapping(path = "/queryNotifications")
	public List<Notification> queryNotifications(NotificationVO notificationVO) {
		List<Notification> list =notificationService.queryNotifications();
		return list;
	}




	/**
	 * 查询消息通知分页
	 * 
	 * @param notificationVO 消息通知前端参数
	 * @return
	 */
	@GetMapping(path = "/queryNotification")
	public ListResult<Object> queryNotification(NotificationVO notificationVO) {
		Map<String, Object> data = notificationService.queryNotification(notificationVO.getCurrentPage(), notificationVO.getPageSize(),
				notificationVO.getTitle(), notificationVO.getContent(), notificationVO.getType(), notificationVO.getPriority(), notificationVO.getPublisher(),
				null, notificationVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询我的消息分页
	 * 
	 * @param notificationVO 我的消息前端参数
	 * @return
	 */
	@GetMapping(path = "/queryMyNotification")
	public ListResult<Object> queryMyNotification(NotificationVO notificationVO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> data = notificationService.queryNotification(notificationVO.getCurrentPage(), notificationVO.getPageSize(),
				notificationVO.getTitle(), notificationVO.getContent(), notificationVO.getType(), notificationVO.getPriority(), notificationVO.getPublisher(),
				authentication.getName(), notificationVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询顶部消息通知的数据
	 * 
	 * @return
	 */
	@GetMapping(path = "/queryHeadNotification")
	public ListResult<Object> queryHeadNotification() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<LinkedHashMap<String, Object>> data = notificationService.queryHeadNotification(authentication.getName());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询类型的下拉框数据
	 * 
	 * @return
	 */
	@GetMapping(path = "/queryNotificationType")
	public ListResult<Object> queryNotificationType() {
		List<LinkedHashMap<String, Object>> data = notificationService.queryNotificationType();
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询优先级的下拉框数据
	 * 
	 * @return
	 */
	@GetMapping(path = "/queryNotificationPriority")
	public ListResult<Object> queryNotificationPriority() {
		List<LinkedHashMap<String, Object>> data = notificationService.queryNotificationPriority();
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增消息通知
	 * 
	 * @param notification 消息通知对象
	 * @return
	 */
	@PostMapping(path = "/addNotification")
	public ActionResult addNotification(@Validated(InsertValidator.class) @RequestBody Notification notification) {
		notificationService.insertNotification(notification);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑消息通知
	 * 
	 * @param notification 消息通知对象
	 * @return
	 */
	@PutMapping(path = "/updateNotification")
	public ActionResult updateNotification(@Validated(UpdateValidator.class) @RequestBody Notification notification) {
		notificationService.updateNotification(notification);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除消息通知
	 * 
	 * @param id 消息通知ID
	 * @return
	 */
	@PostMapping(path = "/deleteNotification")
	public ActionResult deleteNotification(@RequestParam(name = "id", required = true) Long[] id) {
		notificationService.deleteNotification(id);
		return ResultBuilder.buildActionSuccess();
	}

}
