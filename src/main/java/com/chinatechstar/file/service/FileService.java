package com.chinatechstar.file.service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件信息的业务逻辑接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface FileService {

	/**
	 * 查询文件分页
	 *
	 * @param currentPage      当前页数
	 * @param pageSize         每页记录数
	 * @param id               文件ID
	 * @param originalFilename 文件名称
	 * @param content          文件字符串内容
	 * @param parentId         上级ID
	 * @param previousId       返回上一级ID
	 * @param contentType      数据类型
	 * @param fileType         文件类型
	 * @param sorter           排序
	 * @return
	 */
	Map<String, Object> queryFile(Integer currentPage, Integer pageSize, Long id, String originalFilename, String content, Long parentId, Long previousId,
			String contentType, String fileType, String sorter, String tenantCode);

	/**
	 * 查询文件类型名称
	 * 
	 * @return
	 */
	List<LinkedHashMap<String, Object>> queryFileTypeName();

	/**
	 * 删除文件
	 * 
	 * @param id 文件ID
	 * @throws IOException
	 */
	void deleteFile(Long[] id) throws IOException;

	/**
	 * 上传文件
	 * 
	 * @param file       文件资源
	 * @param id         用户ID
	 * @param parentId   上级ID
	 * @param uploadType 上传类型
	 * @param fileType   文件类型
	 * @throws IOException
	 */
	String uploadFile(MultipartFile file, Long id, Long parentId, String uploadType, String fileType, String tenantCode) throws Exception;

	/**
	 * 新增文件夹
	 * 
	 * @param id               用户ID
	 * @param originalFilename 文件夹名称
	 * @param parentId         上级ID
	 */
	void addFolder(Long id, String originalFilename, Long parentId);

}
