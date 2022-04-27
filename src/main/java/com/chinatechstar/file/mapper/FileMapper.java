package com.chinatechstar.file.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.chinatechstar.file.entity.File;

/**
 * 文件信息的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface FileMapper {

	/**
	 * 查询文件分页
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryFile(Map<String, Object> paramMap);

	/**
	 * 新增文件记录
	 * 
	 * @param file 文件对象
	 * @return
	 */
	int insertFile(File file);

	/**
	 * 新增文件与系统用户关联记录
	 *
	 * @param id         文件与系统用户关联ID
	 * @param fileId     文件ID
	 * @param sysUserId  系统用户ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int insertFileSysUser(@Param(value = "id") Long id, @Param(value = "fileId") Long fileId, @Param(value = "sysUserId") Long sysUserId, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 删除文件
	 *
	 * @param id         文件ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	int deleteFile(@Param(value = "array") Long[] id, @Param(value = "tenantCode") String tenantCode);

	/**
	 * 查询下载URL列表
	 *
	 * @param id         文件ID
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<String> queryUrlList(@Param(value = "array") Long[] id, @Param(value = "tenantCode") String tenantCode);

}
