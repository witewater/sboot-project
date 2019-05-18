package cn.wendong.admin.common.enums;

public enum ErrorCodeEnum implements IErrorCode{

	UNKNOWN_ERROR(500, -1, "未知错误！"),
	DATA_INTEGRITY_ERROR(400, 200002, "数据不合法或重复的字段值！"),
	BADPARAM(400, 200001, "错误的请求参数错误！"),
	UNAUTHORIZED(401, 201002, "权限不足！"),
	UNAUTHENTICATED(403, 201001, "登录认证失效，请重新登录！"), 
	DATA_NOTEXIST(404, 200003, "数据不存在！"),
	DATA_EXISTED(409, 200004, "重复的数据！"),
	ENTITY_HAS_RELATED_DATA(409, 200005, "已经有关联数据，无法删除！"), 
	SERVER_ERROR(500,-2,"服务错误！");
	
	private int code;
	private int httpStatus;
	private String messageFormat;

	private ErrorCodeEnum(int httpStatus, int code, String messageFormat) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.messageFormat = messageFormat;
	}

	public int getCode() {
		return this.code;
	}

	public String getMessageFormat() {
		return this.messageFormat;
	}

	public int getHttpStatus() {
		return this.httpStatus;
	}

}
