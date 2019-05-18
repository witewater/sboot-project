package cn.wendong.core.scanner.starter.utils;

import java.util.Collection;

/**
 * 空判断相关工具方法
 * @author MB yangtdo@qq.com
 * @date 2019-02-09
 */
public class EmptyUtil {

	/**
     * 字符串判空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        } else if (str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 字符串非空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 列表判空
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null) {
            return true;
        } else if (collection.size() == 0) {
            return true;
        }
        return false;
    }
}
