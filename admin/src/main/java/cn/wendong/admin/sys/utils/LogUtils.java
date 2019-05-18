package cn.wendong.admin.sys.utils;

import javax.persistence.Table;

import cn.wendong.admin.core.utils.SpringContextHolder;
import cn.wendong.admin.sys.entity.SysOptLog;
import cn.wendong.admin.sys.service.SysOptLogService;
import cn.wendong.tools.utils.ReflectHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 日志工具
 * @author MB yangtdo@qq.com
 * @date 2018-12-06
 */
public class LogUtils {

    /**
     * 获取实体对象的日志
     * @param entity 实体对象
     */
    public List<SysOptLog> entityList(Object entity){
        SysOptLogService actionLogService = SpringContextHolder.getBean(SysOptLogService.class);
        Table table = entity.getClass().getAnnotation(Table.class);
        String tableName = table.name();
        try {
            Object object = ReflectHelper.getField(entity, "id");
            Long entityId = Long.valueOf(String.valueOf(object));
            return actionLogService.getDataLogList(tableName, entityId);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
