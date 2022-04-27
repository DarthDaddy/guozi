package com.chinatechstar.generator.entity;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.chinatechstar.component.commons.entity.TimeEntity;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * 代码信息的实体类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class Generator extends TimeEntity implements Serializable {

	private static final long serialVersionUID = 924591675326135912L;
	@NotNull(groups = { UpdateValidator.class })
	private Long id;// ID
	@NotBlank(groups = { InsertValidator.class, UpdateValidator.class })
	@Size(max = 256, min = 1, groups = { InsertValidator.class, UpdateValidator.class })
	private String packageName;// 包名
	@NotBlank(groups = { InsertValidator.class, UpdateValidator.class })
	@Size(max = 64, min = 1, groups = { InsertValidator.class, UpdateValidator.class })
	private String entityName;// 实体名
	@NotBlank(groups = { InsertValidator.class, UpdateValidator.class })
	@Size(max = 64, min = 1, groups = { InsertValidator.class, UpdateValidator.class })
	private String tableName;// 表名
	@Size(max = 64, min = 1, groups = { InsertValidator.class, UpdateValidator.class })
	private String serviceName;// 服务名
	private List<GeneratorField> generatorField;// 实体字段
	private String tenantCode;// 租户编码

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public List<GeneratorField> getGeneratorField() {
		return generatorField;
	}

	public void setGeneratorField(List<GeneratorField> generatorField) {
		this.generatorField = generatorField;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Generator item = (Generator) o;
		return Objects.equal(id, item.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", id).add("packageName", packageName).add("entityName", entityName).add("tableName", tableName)
				.add("serviceName", serviceName).add("generatorField", generatorField).add("tenantCode", tenantCode).add("createTime", super.getCreateTime())
				.toString();
	}

}
