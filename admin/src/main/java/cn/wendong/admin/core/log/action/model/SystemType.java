package cn.wendong.admin.core.log.action.model;

import cn.wendong.admin.core.enums.SysLogEnum;
import lombok.Getter;

/**
 * 系统类型
 * @author MB
 * @date 2018-12-02
 */
@Getter
public class SystemType extends BusinessType{
    // 日志类型
    protected Byte type = SysLogEnum.SYSTEM.getCode();

    public SystemType(String message) {
        super(message);
    }

    public SystemType(String name, String message) {
        super(name, message);
    }
}
