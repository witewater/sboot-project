package cn.wendong.admin.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wendong.admin.common.base.BaseSpecification;
import cn.wendong.admin.common.page.PageHelper;
import cn.wendong.admin.common.page.RestResult;
import cn.wendong.admin.common.page.RestResultGenerator;
import cn.wendong.admin.core.constant.AdminConst;
import cn.wendong.admin.core.enums.ResultEnum;
import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.core.exception.ResponseException;
import cn.wendong.admin.core.log.action.RoleAction;
import cn.wendong.admin.core.log.action.StatusAction;
import cn.wendong.admin.core.log.annotation.Logger;
import cn.wendong.admin.core.shiro.ShiroAuthManager;
import cn.wendong.admin.core.utils.FormBeanUtil;
import cn.wendong.admin.sys.entity.SysMenu;
import cn.wendong.admin.sys.entity.SysRole;
import cn.wendong.admin.sys.service.SysMenuService;
import cn.wendong.admin.sys.service.SysRoleService;
import cn.wendong.admin.sys.service.model.RoleModel;

/**
 * 角色控制器
 * @author MB
 * @date 2018-12-02
 */
@Controller
@RequestMapping("/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 列表页面
     * @param pageIndex 页码
     * @param pageSize 获取数据长度
     */
    @GetMapping("/index")
    @RequiresPermissions("/role/index")
    public String index(){
        return "/system/role/index";
    }

    
    /**
     * 获取角色列表
     * @param role
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getpage.do")
    @RequiresPermissions("/role/index")
	@ResponseBody
	public PageHelper<SysRole> getPage(RoleModel role,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
    	
//      // 创建匹配器，进行动态查询匹配
//      ExampleMatcher matcher = ExampleMatcher.matching().
//              withMatcher("title", match -> match.contains());
//
//      // 获取角色列表
//      Example<SysRole> example = MyExample.of(role, matcher);
//      Page<SysRole> list = sysRoleService.getPageList(example, pageNumber, pageSize);
    	
    	Map<String, Object> param = new HashMap<String, Object>();
		param.put("name", role.getName());
		param.put("title", role.getTitle());
		param.put("status", role.getStatus());
		
		BaseSpecification<SysRole> sysRole = new BaseSpecification<>(param, SysRole.class);
		Page<SysRole> page = sysRoleService.getPageList(sysRole, PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.DESC,"id"));
		
		PageHelper<SysRole> ph = new PageHelper<SysRole>(page);
		ph.setOffset(pageNumber);
		ph.setLimit(pageSize);
		return ph;
    }
    
    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("/role/add")
    public String toAdd(){
        return "/system/role/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("/role/edit")
    public String toEdit(@PathVariable("id") Long id, Model model){
        SysRole role = sysRoleService.getId(id);
        model.addAttribute("role",role);
        return "/system/role/add";
    }

    /**
     * 保存添加/修改的数据
     * @param roleForm 表单验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"/role/add","/role/edit"})
    @ResponseBody
    @Logger(key = RoleAction.ROLE_SAVE, action = RoleAction.class)
    public RestResult save(@Validated RoleModel roleForm){
        // 不允许操作管理员角色数据
        if (roleForm.getId() !=null && roleForm.getId().equals(AdminConst.SUPER_ADMIN_ROLE_ID) &&
                !ShiroAuthManager.getSysUser().getId().equals(AdminConst.SUPER_ADMIN_ID)){
            throw new ResponseException(ResultEnum.NO_ADMINROLE_AUTH);
        }

        // 将验证的数据复制给实体类
        SysRole role = new SysRole();
        if(roleForm.getId() != null){
            role = sysRoleService.getId(roleForm.getId());
        }
        String[] ignore = {"users", "menus"};
        FormBeanUtil.copyProperties(roleForm, role, ignore);

        // 保存数据
        sysRoleService.save(role);
        return RestResultGenerator.success("保存成功");
    }

    /**
     * 跳转到授权页面
     */
    @GetMapping("/auth")
    @RequiresPermissions("/role/auth")
    public String toAuth(@RequestParam(value = "ids") Long id, Model model){
        model.addAttribute("id",id);
        return "/system/role/auth";
    }

    /**
     * 获取权限资源列表
     */
    @GetMapping("/authList")
    @RequiresPermissions("/role/auth")
    @ResponseBody
    public RestResult authList(@RequestParam(value = "ids") Long id){
        // 获取指定角色权限资源
        SysRole role = sysRoleService.getId(id);
        Set<SysMenu> authMenus = role.getMenus();
        // 获取全部菜单列表
        Sort sort = new Sort(Sort.Direction.ASC, "type", "sort");
        List<SysMenu> list = sysMenuService.getList(sort);
        // 融合两项数据
        list.forEach(menu -> {
            if(authMenus.contains(menu)){
                menu.setRemark("auth:true");
            }else {
                menu.setRemark("");
            }
        });
        return RestResultGenerator.success(list);
    }

    /**
     * 保存授权信息
     */
    @PostMapping("/auth")
    @RequiresPermissions("/role/auth")
    @ResponseBody
    @Logger(key = RoleAction.ROLE_AUTH, action = RoleAction.class)
    public RestResult auth(
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "authId", required = false) List<Long> authIds){
        // 不允许操作管理员角色数据
        if (id.equals(AdminConst.SUPER_ADMIN_ROLE_ID) &&
                !ShiroAuthManager.getSysUser().getId().equals(AdminConst.SUPER_ADMIN_ID)){
            throw new ResponseException(ResultEnum.NO_ADMINROLE_AUTH);
        }

        // 将查询的数据关联起来
        SysRole role = sysRoleService.getId(id);
        List<SysMenu> menuList = null;
        if(authIds != null){
            menuList = sysMenuService.getIdList(authIds);
            role.setMenus(new HashSet<>(menuList));
        }else {
            role.setMenus(null);
        }

        // 保存数据
        sysRoleService.save(role);
        return RestResultGenerator.success("保存成功");
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("/role/detail")
    public String toDetail(@PathVariable("id") Long id, Model model){
        SysRole role = sysRoleService.getId(id);
        model.addAttribute("role",role);
        return "/system/role/detail";
    }

    /**
     * 跳转到拥有该角色的用户列表页面
     */
    @GetMapping("/userList/{id}")
    @RequiresPermissions("/role/detail")
    public String toUserList(@PathVariable("id") Long id, Model model){
        SysRole role = sysRoleService.getId(id);
        model.addAttribute("list",role.getUsers());
        return "/system/role/user_list";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("/role/status")
    @ResponseBody
    @Logger(name = "角色状态", action = StatusAction.class)
    public RestResult status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> idList){
        // 不能修改超级管理员角色状态
        if(idList.contains(AdminConst.SUPER_ADMIN_ROLE_ID)){
            throw new ResponseException(ResultEnum.NO_ADMINROLE_STATUS);
        }

        try {
            // 获取状态StatusEnum对象
            StatusEnum statusEnum = StatusEnum.valueOf(param.toUpperCase());
            // 更新状态
            Integer count = sysRoleService.updateStatus(statusEnum,idList);
            if (count > 0){
                return RestResultGenerator.success(statusEnum.getMessage()+"成功");
            }else{
                return RestResultGenerator.error(statusEnum.getMessage()+"失败，请重新操作");
            }
        } catch (IllegalArgumentException e){
            throw new ResponseException(ResultEnum.STATUS_ERROR);
        }
    }




}
