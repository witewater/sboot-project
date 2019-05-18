package cn.wendong.admin.core.log.action.model;

import lombok.Getter;

/**
 * 用户操作基础模型
 * @author MB
 * @date 2018-12-02
 */
@Getter
public class BaseUserActionModel {
    // 日志名称
    protected String name;
    // 日志类型
    protected Byte type;
    
}
