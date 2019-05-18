package cn.wendong.admin.sys.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wendong.admin.common.base.BaseSpecification;
import cn.wendong.admin.common.page.PageHelper;
import cn.wendong.admin.common.page.RestResult;
import cn.wendong.admin.common.page.RestResultGenerator;
import cn.wendong.admin.core.utils.MyExample;
import cn.wendong.admin.sys.entity.SysMenu;
import cn.wendong.admin.sys.entity.SysOptLog;
import cn.wendong.admin.sys.service.SysOptLogService;
import cn.wendong.admin.sys.service.model.LogModel;
import cn.wendong.admin.sys.service.model.MenuModel;

/**
 * 系统日志
 * @author MB yangtdo@qq.com
 * @date 2018-12-07
 */
@Controller
@RequestMapping("/log")
public class SysOptLogController {

    @Autowired
    private SysOptLogService sysOptLogService;

    /**
     * 列表页面
     * @param pageIndex 页码
     * @param pageSize 获取数据长度
     */
    @GetMapping("/index")
    @RequiresPermissions("/log/index")
    public String index(Model model, SysOptLog actionLog,
                        @RequestParam(value="page",defaultValue="1") int pageIndex,
                        @RequestParam(value="size",defaultValue="10") int pageSize){

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取日志列表
        Example<SysOptLog> example = MyExample.of(actionLog, matcher);
        Page<SysOptLog> list = sysOptLogService.getPageList(example, pageIndex, pageSize);

        // 封装数据
        model.addAttribute("list",list.getContent());
        model.addAttribute("page",list);
        return "/system/log/index";
    }

    /**
     * 获取日志列表
     * @param log
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getpage.do")
    @RequiresPermissions("/log/index")
	@ResponseBody
	public PageHelper<SysOptLog> getPage(LogModel log,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
    	
    	Map<String, Object> param = new HashMap<String, Object>();
		param.put("name", log.getName());
		param.put("type", log.getType());
		BaseSpecification<SysOptLog> sysMenu = new BaseSpecification<>(param, SysOptLog.class);
		Page<SysOptLog> page = sysOptLogService.getPageList(sysMenu, PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.DESC,"id"));
		
		PageHelper<SysOptLog> ph = new PageHelper<SysOptLog>(page);
		ph.setOffset(pageNumber);
		ph.setLimit(pageSize);
		return ph;
    }
    
    
    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("/log/detail")
    public String toDetail(@PathVariable("id") Long id, Model model){
        SysOptLog actionLog = sysOptLogService.getId(id);
        model.addAttribute("actionLog",actionLog);
        return "/system/log/detail";
    }

//    /**
//     * 删除指定的日志
//     */
//    @RequestMapping("/status/delete")
//    @RequiresPermissions("/log/delete")
//    @ResponseBody
//    public AjaxJson delete(
//            @RequestParam(value = "ids", required = false) Long id){
//        if (id != null){
//            sysOptLogService.deleteId(id);
//            return AjaxJsonUtil.success("删除日志成功");
//        }else {
//            sysOptLogService.emptyLog();
//            return AjaxJsonUtil.success("清空日志成功");
//        }
//    }
}
