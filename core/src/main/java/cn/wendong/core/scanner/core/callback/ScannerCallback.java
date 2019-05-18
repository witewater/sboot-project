package cn.wendong.core.scanner.core.callback;

import java.util.List;

/**
 * Callback函数接口
 * @author MB yangtdo@qq.com
 * @date 2019-02-09
 */
public interface ScannerCallback<T> {

	/**
     * 回调方法
     *
     * @param clzs
     */
    void callback(List<T> clzs);
}
