package cn.wendong.admin.sys.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import java.util.HashMap;
import java.util.Map;

import cn.wendong.admin.core.enums.DictTypeEnum;
import cn.wendong.admin.core.utils.AppUtils;
import cn.wendong.admin.core.utils.EhCacheUtil;
import cn.wendong.admin.core.utils.SpringContextHolder;
import cn.wendong.admin.sys.entity.SysDict;
import cn.wendong.admin.sys.service.SysDictService;

/**
 * 字典提取工具
 * @author MB yangtdo@qq.com
 * @date 2018-12-06
 */
public class DictUtil {

    private static Cache dictCache = EhCacheUtil.getDictCache();

    /**
     * 获取字典值
     * @param label 字典标识
     */
    public static String value(String label){
        String value = "";
        Element dictEle = dictCache.get(label);
        if(dictEle != null){
            value = String.valueOf(dictEle.getObjectValue());
        }else {
            SysDictService dictService = SpringContextHolder.getBean(SysDictService.class);
            SysDict dict = dictService.getName(label);
            if(dict != null){
                value = dict.getCode();
                dictCache.put(new Element(dict.getName(), dict.getCode()));
            }
        }
        return value;
    }

    /**
     * 获取键值对集合
     * @param label 字典标识
     */
    @SuppressWarnings("unchecked")
	public static Map<Long, String> keyValueList(String label){
        Map<Long, String> value = null;
        Element dictEle = dictCache.get(label);
        if(dictEle != null){
            value = (Map<Long, String>) dictEle.getObjectValue();
        }else {
            SysDictService dictService = SpringContextHolder.getBean(SysDictService.class);
            SysDict dict = dictService.getName(label);
            if(dict != null && dict.getType().equals(DictTypeEnum.KEY_VALUE.getCode())){
                String dictValue = dict.getCode();
                String[] outerSplit = dictValue.split("\\r\\n");
                value = new HashMap<>();
                for (String osp : outerSplit) {
                    String[] split = osp.split(":");
                    value.put(Long.valueOf(split[0]), split[1]);
                }
                dictCache.put(new Element(dict.getName(), value));
            }
        }
        return value;
    }

    /**
     * 获取键值对数据
     * @param label 字典标识
     * @param key 键值对标识
     */
    public static String keyValue(String label, String key){
        Map<Long, String> list = DictUtil.keyValueList(label);
        if(list != null){
            return list.get(Long.valueOf(key));
        }else{
            return "";
        }
    }

    /**
     * 获取枚举集合
     * @param label 字典标识
     */
    @SuppressWarnings("unchecked")
	public static Map<Long, String> enumValueList(String label){
        Map<Long, String> value = null;
        Element dictEle = dictCache.get(label);
        if(dictEle != null){
            value = (Map<Long, String>) dictEle.getObjectValue();
        }else try {
            SysDictService dictService = SpringContextHolder.getBean(SysDictService.class);
            SysDict dict = dictService.getName(label);
            if(dict != null && dict.getType().equals(DictTypeEnum.ENUM_VALUE.getCode())){
                Class<?> enumClass = Class.forName(dict.getCode());
                value = AppUtils.enumToMap(enumClass);
                dictCache.put(new Element(dict.getName(), value));
            }
        } catch (ClassNotFoundException ignored) {
        }
        return value;
    }

    /**
     * 获取枚举数据
     * @param label 字典标识
     * @param code 枚举code
     */
    public static String enumValue(String label, String code){
        Map<Long, String> list = DictUtil.enumValueList(label);
        if(list != null){
            return list.get(Long.valueOf(code));
        }else{
            return "";
        }
    }

    /**
     * 封装数据状态字典
     * @param status 状态
     */
    public static String dataStatus(Byte status){
        String label = "DATA_STATUS";
        return DictUtil.keyValue(label, String.valueOf(status));
    }

    /**
     * 清除缓存中指定的数据
     * @param label 字典标识
     */
    public static void clearCache(String label){
        Element dictEle = dictCache.get(label);
        if (dictEle != null){
            dictCache.remove(label);
        }
    }
}
