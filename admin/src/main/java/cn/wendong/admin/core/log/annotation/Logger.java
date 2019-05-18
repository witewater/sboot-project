package cn.wendong.admin.core.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.wendong.admin.core.log.action.base.CommActionMap;

/**
 * 自定义系统操作日志注解
 * @author MB yangtdo@qq.com
 * @date 2018-12-06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Logger {
    // 日志名称
    String name() default "";
    // 日志消息
    String message() default "";
    // 行为key
    String key() default "";
    // 行为类
    Class<? extends CommActionMap> action() default CommActionMap.class;
}
