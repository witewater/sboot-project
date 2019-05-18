package cn.wendong.admin.common.enums;

import lombok.Getter;

/**
 * 基础状态信息 统一定义页面错误代码
 * @author MB yangtdo@qq.com
 * @date 2018-12-06
 */
@Getter
public enum BaseReultEnum {

	SUCCESS(200, "成功"),
    ERROR(500, "错误");

    private Integer code;

    private String message;

    BaseReultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
	
}
