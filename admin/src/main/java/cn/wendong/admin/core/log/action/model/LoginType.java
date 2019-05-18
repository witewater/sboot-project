package cn.wendong.admin.core.log.action.model;

import cn.wendong.admin.core.enums.SysLogEnum;
import lombok.Getter;

/**
 * 登录类型
 * @author MB
 * @date 2018-12-02
 */
@Getter
public class LoginType extends BusinessType{
    // 日志类型
    protected Byte type = SysLogEnum.LOGIN.getCode();

    public LoginType(String message) {
        super(message);
    }

    public LoginType(String name, String message) {
        super(name, message);
    }
}
