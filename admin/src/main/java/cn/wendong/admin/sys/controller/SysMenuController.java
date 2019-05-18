package cn.wendong.admin.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
import cn.wendong.admin.core.enums.ResultEnum;
import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.core.exception.ResponseException;
import cn.wendong.admin.core.log.action.SaveAction;
import cn.wendong.admin.core.log.action.StatusAction;
import cn.wendong.admin.core.log.annotation.Logger;
import cn.wendong.admin.core.utils.FormBeanUtil;
import cn.wendong.admin.core.utils.MyExample;
import cn.wendong.admin.core.utils.TreeUtils;
import cn.wendong.admin.sys.entity.SysMenu;
import cn.wendong.admin.sys.service.SysMenuService;
import cn.wendong.admin.sys.service.model.MenuModel;
import cn.wendong.admin.sys.utils.DictUtil;

/**
 * 
 * @author MB yangtdo@qq.com
 * @date 2018-12-07
 */
@Controller
@RequestMapping("/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 跳转到列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("/menu/index")
    public String index(Model model, SysMenu menu) {
        String search = "";
        if (menu.getStatus() != null) {
            search += "status=" + menu.getStatus();
        }
        if (menu.getName() != null) {
            search += "&name=" + menu.getName();
        }
        if (menu.getUrl() != null) {
            search += "&url=" + menu.getUrl();
        }
        model.addAttribute("search", search);
        return "/system/menu/index";
    }

    /**
     * 菜单数据列表
     */
    @GetMapping("/list")
    @RequiresPermissions("/menu/index")
    @ResponseBody
    public RestResult<?> list(SysMenu menu) {
        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("name", match -> match.contains());

        // 获取用户列表
        Example<SysMenu> example = MyExample.of(menu, matcher);
        Sort sort = new Sort(Sort.Direction.ASC, "type", "sort");
        List<SysMenu> list = sysMenuService.getList(example, sort);
        list.forEach(editMenu -> {
            String type = String.valueOf(editMenu.getType());
            editMenu.setRemark(DictUtil.keyValue("MENU_TYPE", type));
        });
        return RestResultGenerator.success(list);
    }

    /**
     * 获取菜单列表
     * @param menu
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getpage.do")
    @RequiresPermissions("/menu/index")
	@ResponseBody
	public RestResult<?> getPage(SysMenu menu) {
    	// 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("name", match -> match.contains());
        // 获取用户列表
        Example<SysMenu> example = MyExample.of(menu, matcher);
        Sort sort = new Sort(Sort.Direction.ASC, "type", "sort");
        List<SysMenu> list = sysMenuService.getList(example, sort);
		return RestResultGenerator.success(new TreeUtils<SysMenu>().treeGridList(list));
    }
    
    /**
     * 获取排序菜单列表
     */
    @GetMapping("/sortList/{pid}/{notId}")
    @RequiresPermissions({"/menu/add", "/menu/edit"})
    @ResponseBody
    public Map<Integer, String> sortList(
            @PathVariable(value = "pid", required = false) Long pid,
            @PathVariable(value = "notId", required = false) Long notId){
        // 本级排序菜单列表
        notId = notId != null ? notId : (long) 0;
        List<SysMenu> levelMenu = sysMenuService.getPid(pid, notId);
        Map<Integer, String> sortMap = new TreeMap<>();
        for (int i = 1; i <= levelMenu.size(); i++) {
            sortMap.put(i, levelMenu.get(i - 1).getName());
        }
        return sortMap;
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping({"/add", "/add/{pid}"})
    @RequiresPermissions("/menu/add")
    public String toAdd(@PathVariable(value = "pid", required = false) Long pid, Model model) {
        // 父级菜单
        if (pid != null) {
            SysMenu pMenu = sysMenuService.getId(pid);
            model.addAttribute("pMenu", pMenu);
        }

        return "/system/menu/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("/menu/edit")
    public String toEdit(@PathVariable("id") Long id, Model model) {
        SysMenu menu = sysMenuService.getId(id);
        SysMenu pMenu = sysMenuService.getId(menu.getPid());
        if (pMenu == null) {
            SysMenu newMenu = new SysMenu();
            newMenu.setId((long) 0);
            newMenu.setName("顶级菜单");
            pMenu = newMenu;
        }

        model.addAttribute("menu", menu);
        model.addAttribute("pMenu", pMenu);
        return "/system/menu/add";
    }

    /**
     * 保存添加/修改的数据
     *
     * @param menuForm 表单验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"/menu/add", "/menu/edit"})
    @ResponseBody
    @Logger(name = "菜单管理", message = "菜单：${title}", action = SaveAction.class)
    public RestResult save(@Validated MenuModel menuForm) {
         if (menuForm.getId() == null) {
            // 排序为空时，添加到最后
            if(menuForm.getSort() == null){
                Integer sortMax = sysMenuService.getSortMax(menuForm.getPid());
                menuForm.setSort(sortMax !=null ? sortMax - 1 : 0);
            }

            // 添加全部上级序号
            if (menuForm.getPid() != 0) {
                SysMenu pMenu = sysMenuService.getId(menuForm.getPid());
                menuForm.setPids(pMenu.getPids() + ",[" + menuForm.getPid() + "]");
            } else {
                menuForm.setPids("[0]");
            }
        }

        // 将验证的数据复制给实体类
        SysMenu menu = new SysMenu();
        if (menuForm.getId() != null) {
            menu = sysMenuService.getId(menuForm.getId());
            menuForm.setPids(menu.getPids());
        }
        FormBeanUtil.copyProperties(menuForm, menu);

        // 排序功能
        Integer sort = menuForm.getSort();
        Long notId = menu.getId() != null ? menu.getId() : 0;
        List<SysMenu> levelMenu = sysMenuService.getPid(menuForm.getPid(), notId);
        levelMenu.add(sort, menu);
        for (int i = 1; i <= levelMenu.size(); i++) {
            levelMenu.get(i - 1).setSort(i);
        }

        // 保存数据
        sysMenuService.save(levelMenu);
        return RestResultGenerator.success("保存成功");
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("/menu/detail")
    public String toDetail(@PathVariable("id") Long id, Model model) {
        SysMenu menu = sysMenuService.getId(id);
        model.addAttribute("menu", menu);
        return "/system/menu/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("/menu/status")
    @ResponseBody
    @Logger(name = "菜单状态", action = StatusAction.class)
    public RestResult status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> idList) {
        try {
            // 获取状态StatusEnum对象
            StatusEnum statusEnum = StatusEnum.valueOf(param.toUpperCase());
            // 更新状态
            Integer count = sysMenuService.updateStatus(statusEnum, idList);
            if (count > 0) {
                return RestResultGenerator.success(statusEnum.getMessage() + "成功");
            } else {
                return RestResultGenerator.error(statusEnum.getMessage() + "失败，请重新操作");
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseException(ResultEnum.STATUS_ERROR);
        }
    }


}
