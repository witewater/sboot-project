package cn.wendong.admin.core.log.action.model;

import cn.wendong.admin.core.enums.SysLogEnum;
import lombok.Getter;

/**
 * 登录方法
 * @author MB
 * @date 2018-12-02
 */
@Getter
public class LoginMethod extends BusinessMethod{
    // 日志类型
    protected Byte type = SysLogEnum.LOGIN.getCode();

    public LoginMethod(String method) {
        super(method);
    }

    public LoginMethod(String name, String method) {
        super(name, method);
    }
}
