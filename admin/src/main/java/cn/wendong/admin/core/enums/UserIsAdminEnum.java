package cn.wendong.admin.core.enums;

/**
 * 判断用户是否为管理员登录
 * @author MB
 * @date 2018-12-01
 */
public enum UserIsAdminEnum {

    YES((byte)1, "是后台管理员"),
    NO((byte)2, "不是后台管理员");

    private Byte code;
    private String message;

    UserIsAdminEnum(Byte code, String message) {
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

