package com.chinatechstar.job.component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.javacrumbs.shedlock.core.SchedulerLock;

/**
 * 分布式定时任务调度实现类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Component
public class ScheduledTasks {

	private static final String FOURTEEN_MIN = "PT14M";// M是指分钟，S是指秒钟

	/**
	 * <p>
	 * 不同的定时任务设置不同的name。正常情况下，任务完成立即释放锁。
	 * </p>
	 * <p>
	 * lockAtMostForString设置执行节点遇到异常退出时仍将锁保留多长时间，值必须大于节点执行时间。
	 * </p>
	 * <p>
	 * lockAtLeastForString设置锁的最短时间，作用是在任务很短且节点之间存在时钟差的情况下，防止从多个节点执行。
	 * </p>
	 */
	@Scheduled(cron = "0 */15 * * * *")
	@SchedulerLock(name = "scheduledTaskOne", lockAtMostForString = FOURTEEN_MIN, lockAtLeastForString = FOURTEEN_MIN) // NOSONAR
	public void scheduledTask() {
		// 其他微服务模仿ShedlockConfig和ScheduledTasks的代码编写分布式定时任务，或者在这里通过FeignClient加入其他微服务需要定时执行的任务
	}

}
