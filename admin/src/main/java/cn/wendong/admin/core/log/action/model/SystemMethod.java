package cn.wendong.admin.core.log.action.model;

import cn.wendong.admin.core.enums.SysLogEnum;
import lombok.Getter;

/**
 * 系统方法
 * @author MB
 * @date 2018-12-02
 */
@Getter
public class SystemMethod extends BusinessMethod{
    // 日志类型
    protected Byte type = SysLogEnum.SYSTEM.getCode();

    public SystemMethod(String method) {
        super(method);
    }

    public SystemMethod(String name, String method) {
        super(name, method);
    }
}
