package cn.wendong.admin.core.log.action;

import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.core.log.action.base.CommActionMap;
import cn.wendong.admin.core.log.action.base.ResetLog;

import java.util.List;

/**
 * 记录数据状态的行为
 * @author MB yangtdo@qq.com
 * @date 2018-12-06
 */
public class StatusAction extends CommActionMap {

    @Override
    public void init() {
        // 记录数据状态改变日志
        putMethod("default", "defaultMethod");
    }

    /**
     * 重新包装保存的数据行为方法
     *
     * @param resetLog ResetLog对象数据
     */
    @SuppressWarnings("unchecked")
	public static void defaultMethod(ResetLog resetLog) {
        if(resetLog.isSuccessRecord()){
            String param = (String) resetLog.getParam("param");
            StatusEnum statusEnum = StatusEnum.valueOf(param.toUpperCase());
            List<Long> idList = (List<Long>) resetLog.getParam("idList");
            resetLog.getSysOptLog().setMessage(statusEnum.getMessage() + "ID：" + idList.toString());
        }
    }
}
