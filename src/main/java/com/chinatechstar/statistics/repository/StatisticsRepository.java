package com.chinatechstar.statistics.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chinatechstar.statistics.entity.Sms;

/**
 * 统计的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Repository
public interface StatisticsRepository extends MongoRepository<Sms, String> {

}
