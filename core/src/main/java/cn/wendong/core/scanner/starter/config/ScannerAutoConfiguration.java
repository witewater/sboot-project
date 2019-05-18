package cn.wendong.core.scanner.starter.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.wendong.core.scanner.core.scanner.ClassScanner;
import cn.wendong.core.scanner.starter.prop.ScannerProp;
import cn.wendong.core.scanner.starter.runner.AutoScanner;

/**
 * 包自动扫描
 * @author MB yangtdo@qq.com
 * @date 2019-02-09
 */
@Configuration
@EnableConfigurationProperties({ScannerProp.class})
public class ScannerAutoConfiguration {

//	 /**
//     * 初始化自动类扫描器
//     *
//     * @param scannerProp
//     * @param classScanner
//     * @return
//     */
//    @Bean
//    public AutoScanner autoScanner(ScannerProp scannerProp, ClassScanner<?> classScanner) {
//        return new AutoScanner(scannerProp, classScanner);
//    }

//    /**
//     * 初始化类扫描器
//     *
//     * @return
//     */
//    @Bean
//    public ClassScanner<?> classScanner() {
//        return null;
//    }
}
