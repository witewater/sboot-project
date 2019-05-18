package cn.wendong.core.scanner.core.scanner;

import java.lang.annotation.Annotation;
import java.util.List;

import cn.wendong.core.scanner.core.callback.ScannerCallback;

/**
 * 包扫描
 * @author MB yangtdo@qq.com
 * @date 2019-02-09
 */
public interface ClassScanner<T> {
	
	/**
     * 扫描多个包下的Class
     *
     * @param scanBasePackages
     * @return
     */
    List<T> scan(List<String> scanBasePackages);

    /**
     * 扫描多个包下带有注解的Class
     *
     * @param scanBasePackages
     * @param anno
     * @return
     */
    List<T> scanByAnno(List<String> scanBasePackages, Class<? extends Annotation> anno);

    /**
     * 扫描多个包下的Class，并执行回调
     *
     * @param scanBasePackages
     * @param callback
     */
    void scanAndCallback(List<String> scanBasePackages, ScannerCallback<?> callback);

    /**
     * 扫描多个包下特定注解的Class，并执行回调
     *
     * @param scanBasePackages
     * @param anno
     * @param callback
     */
    void scanAndCallbackByAnno(List<String> scanBasePackages, Class<? extends Annotation> anno, ScannerCallback<?> callback);

}
