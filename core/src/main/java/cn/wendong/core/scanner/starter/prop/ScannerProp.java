package cn.wendong.core.scanner.starter.prop;

import java.util.LinkedList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 类扫描器配置文件
 * @author MB yangtdo@qq.com
 * @date 2019-02-09
 */
@ConfigurationProperties(prefix = "org.scanner.auto")
@Data
public class ScannerProp {

    /**
     * 是否自动启动
     */
    private boolean enable = false;
    /**
     * 需扫描的包列表
     */
    private List<String> packages = new LinkedList<>();
    /**
     * 回调函数
     */
    private String callback;
    /**
     * 标识注解
     */
    private String annotation;

}