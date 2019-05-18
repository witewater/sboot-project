package cn.wendong.admin.core.log.action.base;

import java.util.HashMap;

import cn.wendong.admin.core.log.action.model.BusinessMethod;
import cn.wendong.admin.core.log.action.model.BusinessType;

/**
 * 所有行为集合抽象类
 * @author MB
 * @date 2018-12-02
 */
public abstract class CommActionMap {

    protected HashMap<String, Object> dictory = new HashMap<>();

    public CommActionMap(){
        init();
    }

    /**
     * 初始化行为列表
     */
    public abstract void init();

    /**
     * 获取指定的行为
     * @param key 行为key
     */
    public Object get(String key) {
        return this.dictory.get(key);
    }

    /**
     * 添加行为
     * @param key 行为key
     * @param modelType 模型类型对象
     */
    public void put(String key, Object modelType) {
        this.dictory.put(key, modelType);
    }

    /**
     * 添加行为-默认类型(业务)
     * @param key 行为key
     * @param message 日志消息
     */
    public void put(String key, String message) {
        this.dictory.put(key, new BusinessType(message));
    }

    /**
     * 添加行为-默认类型(业务)
     * @param key 行为key
     * @param name 日志名称
     * @param message 日志消息
     */
    public void put(String key, String name, String message) {
        this.dictory.put(key, new BusinessType(name, message));
    }

    /**
     * 添加行为方法名
     * @param key 行为key
     * @param modelMethod 模型方法名对象
     */
    public void putMethod(String key, Object modelMethod) {
        this.dictory.put(key, modelMethod);
    }

    /**
     * 添加行为方法名-默认类型(业务)
     * @param key 行为key
     * @param method 方法名
     */
    public void putMethod(String key, String method) {
        this.dictory.put(key, new BusinessMethod(method));
    }

    /**
     * 添加行为方法名-默认类型(业务)
     * @param key 行为key
     * @param name 日志名称
     * @param method 方法名
     */
    public void putMethod(String key, String name, String method) {
        this.dictory.put(key, new BusinessMethod(name, method));
    }


}
