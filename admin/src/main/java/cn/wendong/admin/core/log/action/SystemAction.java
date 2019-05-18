package cn.wendong.admin.core.log.action;

import cn.wendong.admin.core.log.action.base.CommActionMap;
import cn.wendong.admin.core.log.action.base.ResetLog;
import cn.wendong.admin.core.log.action.model.SystemMethod;
import cn.wendong.admin.sys.entity.SysOptLog;
import cn.wendong.admin.sys.entity.SysUser;

/**
 * 系统行为
 * @author MB
 * @date 2018-12-02
 */
public class SystemAction extends CommActionMap {

    public static final String RUNTIME_EXCEPTION = "runtime_exception";

    @Override
    public void init() {
        // 系统异常行为
        putMethod(RUNTIME_EXCEPTION, new SystemMethod("系统异常","runtimeException"));
    }

    // 系统异常行为方法
    public void runtimeException(ResetLog resetLog){
        RuntimeException runtime = (RuntimeException) resetLog.getParam("e");
        StringBuilder message = new StringBuilder();
        message.append(runtime.toString());
        StackTraceElement[] stackTrace = runtime.getStackTrace();
        for (StackTraceElement stack : stackTrace) {
            message.append("\n\t").append(stack.toString());
        }
        SysOptLog actionLog = resetLog.getSysOptLog();
        if(actionLog.getCreateBy() == null){
            SysUser user = new SysUser();
            user.setNickname("系统");
            actionLog.setCreateBy(user);
        }
        actionLog.setMessage(String.valueOf(message));
    }
}
