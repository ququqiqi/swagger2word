package org.word.model;

import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 返回属性
 *
 * @author kevin
 */
@Data
public class ModelAttr implements Serializable {

	public void setClassName(String className) {
		this.className = className;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setRequire(Boolean require) {
		this.require = require;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setProperties(List<ModelAttr> properties) {
		this.properties = properties;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	@Serial
	private static final long serialVersionUID = -4074067438450613643L;

    /**
     * 类名
     */
    private String className = StringUtils.EMPTY;
    /**
     * 属性名
     */
    @Getter
	private String name = StringUtils.EMPTY;
    /**
     * 类型
     */
    @Getter
	private String type = StringUtils.EMPTY;
    /**
     * 是否必填
     */
    private Boolean require = false;
    /**
     * 属性描述
     */
    @Getter
	private String description;

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Getter
	private String defaultValue = StringUtils.EMPTY;

    /**
     * 嵌套属性列表
     */
    @Getter
	private List<ModelAttr> properties = new ArrayList<>();

    /**
     * 是否加载完成，避免循环引用
     */
    private boolean isCompleted = false;

	public boolean isEnum() {
		return isEnum;
	}

	public void setEnum(boolean anEnum) {
		isEnum = anEnum;
	}

	private boolean isEnum = false;
}
