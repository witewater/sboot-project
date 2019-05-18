package cn.wendong.core.scanner.core.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识需要扫描的class
 * @author MB yangtdo@qq.com
 * @date 2019-02-09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scannable {

}
