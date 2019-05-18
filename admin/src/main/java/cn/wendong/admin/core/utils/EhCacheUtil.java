package cn.wendong.admin.core.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * EhCache缓存操作工具
 * @author MB
 * @date 2018-12-02
 */
public class EhCacheUtil {
	
	private static final String SYS_CACHE = "sysCache";

	
    /**
     * 获取SYS_CACHE缓存
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        return get(SYS_CACHE, key);
    }

    /**
     * 写入SYS_CACHE缓存
     *
     * @param key
     * @return
     */
    public static void put(String key, Object value) {
        put(SYS_CACHE, key, value);
    }

    /**
     * 从SYS_CACHE缓存中移除
     *
     * @param key
     * @return
     */
    public static void remove(String key) {
        remove(SYS_CACHE, key);
    }
	
    /**
     * 获取EhCacheManager管理对象
     */
    public static CacheManager getCacheManager(){
        return SpringContextHolder.getBean(CacheManager.class);
    }

    
    /**
     * 获取字典缓存对象
     */
    public static Cache getDictCache(){
        return getCacheManager().getCache("dictionary");
    }

    /**
     * 获取缓存
     *
     * @param cacheName
     * @param key
     * @return
     */
    public static Object get(String cacheName, String key) {
        Element element = getCache(cacheName).get(key);
        return element == null ? null : element.getObjectValue();
    }

    /**
     * 写入缓存
     *
     * @param cacheName
     * @param key
     * @param value
     */
    public static void put(String cacheName, String key, Object value) {
        Element element = new Element(key, value);
        getCache(cacheName).put(element);
    }

    /**
     * 从缓存中移除
     *
     * @param cacheName
     * @param key
     */
    public static void remove(String cacheName, String key) {
        getCache(cacheName).remove(key);
    }
    
    /**
     * 获取EhCache缓存对象，没有则创建一个。
     *
     * @param cacheName
     * @return
     */
    private static Cache getCache(String cacheName) {
        Cache cache = getCacheManager().getCache(cacheName);
        if (cache == null) {
        	getCacheManager().addCache(cacheName);
            cache = getCacheManager().getCache(cacheName);
            cache.getCacheConfiguration().setEternal(true);
        }
        return cache;
    }
    
}
