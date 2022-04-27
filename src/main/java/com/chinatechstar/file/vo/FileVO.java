package com.chinatechstar.file.vo;

import java.io.Serializable;

import com.chinatechstar.component.commons.vo.CommonVO;

/**
 * 文件信息的参数类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class FileVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = -3144811184148006684L;
	private Long id;// 文件ID
	private Long parentId;// 上级ID
	private Long previousId;// 返回上一级ID
	private String originalFilename;// 文件名称
	private String content;// 文件字符串内容
	private String contentType;// 数据类型
	private String fileType;// 文件类型

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getPreviousId() {
		return previousId;
	}

	public void setPreviousId(Long previousId) {
		this.previousId = previousId;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}
