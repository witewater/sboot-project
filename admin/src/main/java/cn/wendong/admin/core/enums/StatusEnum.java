package cn.wendong.admin.core.enums;

/**
 * 数据状态枚举
 * 
 * @author MB
 * @date 2018-12-01
 */
public enum StatusEnum {

	NORMAL((byte) 1, "启用"), 
	FREEZED((byte) 2, "冻结"), 
	DELETE((byte) 3, "删除"),
	AUDIT((byte) 4, "审核");

	private Byte code;

	private String message;

	StatusEnum(Byte code, String message) {
		this.code = code;
		this.message = message;
	}

	public Byte getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
