package cn.wendong.admin.core.enums;

import lombok.Getter;

/**
 * 动作日志吗枚举
 * 
 * @author MB
 * @date 2018-12-02
 */
@Getter
public enum SysLogEnum {

	BUSINESS((byte) 1, "业务"),//指操作记录
	LOGIN((byte) 2, "登录"), //指登录记录
	SYSTEM((byte) 3, "系统");//指系统运行日志

	private Byte code;

	private String message;

	SysLogEnum(Byte code, String message) {
		this.code = code;
		this.message = message;
	}

}
