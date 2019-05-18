package cn.wendong.admin.core.log.action;

import javax.persistence.Table;

import cn.wendong.admin.core.log.action.base.CommActionMap;
import cn.wendong.admin.core.log.action.base.ResetLog;
import cn.wendong.admin.core.log.action.model.BusinessMethod;
import cn.wendong.admin.core.utils.SpringContextHolder;
import cn.wendong.admin.sys.entity.SysRole;
import cn.wendong.admin.sys.service.SysRoleService;

/**
 * 角色日志行为
 * @author MB yangtdo@qq.com
 * @date 2018-12-06
 */
public class RoleAction extends CommActionMap {

    public static final String ROLE_SAVE = "role_save";
    public static final String ROLE_AUTH = "role_auth";

    @Override
    public void init() {
        // 保存日志行为
        putMethod(ROLE_SAVE, new BusinessMethod("日志管理","roleSave"));
        // 角色授权行为
        putMethod(ROLE_AUTH, new BusinessMethod("角色授权","roleAuth"));
    }

    // 保存用户行为方法
    public void roleSave(ResetLog resetLog){
        resetLog.getSysOptLog().setMessage("日志成功：${title}");
        SaveAction.defaultMethod(resetLog);
    }

    // 角色授权行为方法
    public void roleAuth(ResetLog resetLog){
        Long id = (Long) resetLog.getParam("id");
        SysRoleService roleService = SpringContextHolder.getBean(SysRoleService.class);
        SysRole role = roleService.getId(id);
        Table table = SysRole.class.getAnnotation(Table.class);
        resetLog.getSysOptLog().setModel(table.name());
        resetLog.getSysOptLog().setRecordId(role.getId());
        if (resetLog.isSuccess()){
            resetLog.getSysOptLog().setMessage("角色授权成功："+role.getTitle());
        }else {
            resetLog.getSysOptLog().setMessage("角色授权失败："+role.getTitle());
        }
    }
}
