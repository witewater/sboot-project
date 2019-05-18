package cn.wendong.admin.core.log.action;

import java.util.List;

import javax.persistence.Table;

import cn.wendong.admin.core.log.action.base.CommActionMap;
import cn.wendong.admin.core.log.action.base.ResetLog;
import cn.wendong.admin.core.log.action.model.BusinessMethod;
import cn.wendong.admin.core.log.action.model.LoginMethod;
import cn.wendong.admin.core.utils.FormBeanUtil;
import cn.wendong.admin.core.utils.SpringContextHolder;
import cn.wendong.admin.sys.entity.SysOptLog;
import cn.wendong.admin.sys.entity.SysUser;
import cn.wendong.admin.sys.service.SysOptLogService;
import cn.wendong.admin.sys.service.SysUserService;

/**
 * 用户行为日志
 * @author MB
 * @date 2018-12-02
 */
public class UserAction extends CommActionMap {

    public static final String USER_LOGIN = "user_login";
    public static final String USER_SAVE = "user_save";
    public static final String EDIT_PWD = "edit_pwd";
    public static final String EDIT_ROLE = "edit_role";

    @Override
    public void init() {
        // 用户登录行为
        putMethod(USER_LOGIN, new LoginMethod("用户登录","userLogin"));
        // 保存用户行为
        putMethod(USER_SAVE, new BusinessMethod("用户管理","userSave"));
        // 修改用户密码行为
        putMethod(EDIT_PWD, new BusinessMethod("用户密码","editPwd"));
        // 角色分配行为
        putMethod(EDIT_ROLE, new BusinessMethod("角色分配","editRole"));
    }

    // 用户登录行为方法
    public void userLogin(ResetLog resetLog){
       // boolean success = resetLog.isSuccess();
        if (resetLog.isSuccessRecord()){
            SysOptLog actionLog = resetLog.getSysOptLog();
            actionLog.setMessage("后台登录成功");
        }
    }

    // 保存用户行为方法
    public void userSave(ResetLog resetLog){
        resetLog.getSysOptLog().setMessage("用户成功：${username}");
        SaveAction.defaultMethod(resetLog);
    }

    // 修改用户密码行为方法
    @SuppressWarnings("unchecked")
    public void editPwd(ResetLog resetLog){
        List<Long> idList = (List<Long>) resetLog.getParam("idList");
        SysUserService userService = SpringContextHolder.getBean(SysUserService.class);
        List<SysUser> userList = userService.getIdList(idList);
        Table table = SysUser.class.getAnnotation(Table.class);
        String message = "修改用户密码成功";
        if(!resetLog.isSuccess()){
            message = "修改用户密码失败";
        }
        SysOptLogService actionLogService =
                (SysOptLogService) SpringContextHolder.getBean(SysOptLogService.class);

        String finalMessage = message;
        userList.forEach(user -> {
            SysOptLog actionLog = new SysOptLog();
            FormBeanUtil.copyProperties(resetLog.getSysOptLog(), actionLog);
            actionLog.setModel(table.name());
            actionLog.setRecordId(user.getId());
            actionLog.setMessage(finalMessage + user.getUsername());

            // 保存日志
            actionLogService.save(actionLog);
        });
        resetLog.setRecord(false);
    }

    // 角色分配行为方法
    public void editRole(ResetLog resetLog){
        Long id = (Long) resetLog.getParam("id");
        SysUserService userService = SpringContextHolder.getBean(SysUserService.class);
        SysUser user = userService.getId(id);
        Table table = SysUser.class.getAnnotation(Table.class);
        resetLog.getSysOptLog().setModel(table.name());
        resetLog.getSysOptLog().setRecordId(user.getId());
        if (resetLog.isSuccess()){
            resetLog.getSysOptLog().setMessage("角色分配成功："+user.getUsername());
        }else {
            resetLog.getSysOptLog().setMessage("角色分配失败："+user.getUsername());
        }
    }
}
