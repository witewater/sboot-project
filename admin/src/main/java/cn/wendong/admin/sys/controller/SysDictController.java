package cn.wendong.admin.sys.controller;

import java.util.HashMap;
import java.util.List;
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
import cn.wendong.admin.sys.entity.SysDict;
import cn.wendong.admin.sys.service.SysDictService;
import cn.wendong.admin.sys.service.model.DictModel;
import cn.wendong.admin.sys.utils.DictUtil;

/**
 * 系统字典数据
 * @author MB yangtdo@qq.com
 * @date 2018-12-07
 */
@Controller
@RequestMapping("/dict")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    /**
     * 列表页面
     * @param pageIndex 页码
     * @param pageSize 获取数据长度
     */
    @GetMapping("/index")
    @RequiresPermissions("/dict/index")
    public String index(Model model, SysDict dict,
                        @RequestParam(value="page",defaultValue="1") int pageIndex,
                        @RequestParam(value="size",defaultValue="10") int pageSize){

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("title", match -> match.contains());

        // 获取字典列表
        Example<SysDict> example = MyExample.of(dict, matcher);
        Page<SysDict> list = sysDictService.getPageList(example, pageIndex, pageSize);

        // 封装数据
        model.addAttribute("list",list.getContent());
        model.addAttribute("page",list);
        return "/system/dict/index";
    }

    /**
     * 获取字典列表
     * @param dict
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getpage.do")
    @RequiresPermissions("/dict/index")
	@ResponseBody
	public PageHelper<SysDict> getPage(DictModel dict,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
    	
    	Map<String, Object> param = new HashMap<String, Object>();
		param.put("name", dict.getName());
		param.put("type", dict.getType());
		param.put("status", dict.getStatus());
		
		BaseSpecification<SysDict> sysDict = new BaseSpecification<>(param, SysDict.class);
		Page<SysDict> page = sysDictService.getPageList(sysDict, PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.DESC,"id"));
		
		PageHelper<SysDict> ph = new PageHelper<SysDict>(page);
		ph.setOffset(pageNumber);
		ph.setLimit(pageSize);
		return ph;
    	
    }
    	
    	
    
    
    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("/dict/add")
    public String toAdd(){
        return "/system/dict/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("/dict/edit")
    public String toEdit(@PathVariable("id") Long id, Model model){
        SysDict dict = sysDictService.getId(id);
        model.addAttribute("dict",dict);
        return "/system/dict/add";
    }

    /**
     * 保存添加/修改的数据
     * @param dictForm 表单验证对象
     */
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"/dict/add","/dict/edit"})
    @ResponseBody
    @Logger(name = "字典管理", message = "字典：${title}", action = SaveAction.class)
    public RestResult save(@Validated DictModel dictForm){
        // 清除字典值两边空格
        dictForm.setValue(dictForm.getValue().trim());

        // 将验证的数据复制给实体类
        SysDict dict = new SysDict();
        if(dictForm.getId() != null){
            dict = sysDictService.getId(dictForm.getId());
        }
        FormBeanUtil.copyProperties(dictForm, dict);

        // 保存数据
        sysDictService.save(dict);
        if(dictForm.getId() != null){
            DictUtil.clearCache(dictForm.getName());
        }
        return RestResultGenerator.success("保存成功");
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("/dict/detail")
    public String toDetail(@PathVariable("id") Long id, Model model){
        SysDict dict = sysDictService.getId(id);
        model.addAttribute("dict",dict);
        return "/system/dict/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("/dict/status")
    @ResponseBody
    @Logger(name = "字典状态", action = StatusAction.class)
    public RestResult status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> idList){
        try {
            // 获取状态StatusEnum对象
            StatusEnum statusEnum = StatusEnum.valueOf(param.toUpperCase());
            // 更新状态
            Integer count = sysDictService.updateStatus(statusEnum,idList);
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
