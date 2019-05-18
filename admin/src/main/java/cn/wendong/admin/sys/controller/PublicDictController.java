package cn.wendong.admin.sys.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.wendong.admin.common.base.BaseController;
import cn.wendong.admin.common.page.RestResult;
import cn.wendong.admin.common.page.RestResultGenerator;
import cn.wendong.admin.sys.entity.SysDict;
import cn.wendong.admin.sys.service.SysDictService;

/**
 * 公共的字典查询
 * @author MB yangtdo@qq.com
 * @date 2019-02-02
 */
@RestController
@RequestMapping("/public/dict")
public class PublicDictController extends BaseController{

	@Autowired
	private SysDictService sysDictService;

	@RequestMapping("/getDictsByType.do")
	public RestResult<?> getDictsByType(@RequestParam String type) {
		List<SysDict> list = sysDictService.findByType(type);
		List<Map<String,Object>> resList = Lists.newArrayList();
		for (SysDict sysDict:list) {
			Map<String,Object> dictMap = Maps.newLinkedHashMap();
			dictMap.put("code", sysDict.getCode());
			dictMap.put("name", sysDict.getName());
			dictMap.put("status", sysDict.getStatus());
			resList.add(dictMap);
		}
		return RestResultGenerator.success(resList);
	}
}
