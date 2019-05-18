package cn.wendong.admin.core.enums;

import lombok.Getter;

/**
 * 菜单类型状态枚举
 * @author MB yangtdo@qq.com
 * @date 2018-12-11
 */
@Getter
public enum MenuTypeEnum {

    TOP_LEVEL((byte)1, "一级菜单"),
    SUB_LEVEL((byte)2, "子级菜单"),
    NOT_MENU((byte)3, "不是菜单");

    private Byte code;

    private String message;

    MenuTypeEnum(Byte code, String message) {
        this.code = code;
        this.message = message;
    }
}

