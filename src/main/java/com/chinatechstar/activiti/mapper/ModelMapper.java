package com.chinatechstar.activiti.mapper;

/**
 * 模型信息的数据持久接口层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public interface ModelMapper {

	/**
	 * 新增模型与机构关联记录
	 * 
	 * @param id       模型与机构关联ID
	 * @param modelId  模型ID
	 * @param sysOrgId 可引用机构ID
	 * @return
	 */
	int insertModelSysOrg(String id, String modelId, String sysOrgId);

	/**
	 * 新增模型与用户关联记录
	 * 
	 * @param id        模型与用户关联ID
	 * @param modelId   模型ID
	 * @param sysUserId 用户ID
	 * @param postCode  岗位编码
	 * @return
	 */
	int insertModelSysUser(String id, String modelId, String sysUserId, String postCode);

	/**
	 * 删除模型与机构关联记录
	 * 
	 * @param modelId 模型ID
	 * @return
	 */
	int deleteModelSysOrg(String modelId);

	/**
	 * 删除模型与用户关联记录
	 * 
	 * @param modelId 模型ID
	 * @return
	 */
	int deleteModelSysUser(String modelId);

}
