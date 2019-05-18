package cn.wendong.admin.sys.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import cn.wendong.admin.common.base.BaseController;
import cn.wendong.admin.common.base.BaseSpecification;
import cn.wendong.admin.common.page.PageHelper;
import cn.wendong.admin.common.page.RestResult;
import cn.wendong.admin.common.page.RestResultGenerator;
import cn.wendong.admin.core.config.properties.ProjectProperties;
import cn.wendong.admin.core.constant.AdminConst;
import cn.wendong.admin.core.enums.ResultEnum;
import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.core.enums.UserIsAdminEnum;
import cn.wendong.admin.core.exception.ResponseException;
import cn.wendong.admin.core.log.action.StatusAction;
import cn.wendong.admin.core.log.action.UserAction;
import cn.wendong.admin.core.log.annotation.Logger;
import cn.wendong.admin.core.shiro.ShiroAuthManager;
import cn.wendong.admin.core.utils.FormBeanUtil;
import cn.wendong.admin.core.utils.SpringContextHolder;
import cn.wendong.admin.sys.entity.SysRole;
import cn.wendong.admin.sys.entity.SysUser;
import cn.wendong.admin.sys.service.SysRoleService;
import cn.wendong.admin.sys.service.SysUserService;
import cn.wendong.admin.sys.service.model.UserModel;

/**
 * 用户管理
 * 
 * @author MB
 * @date 2018-12-02
 */
@Controller
@RequestMapping("/user")
public class SysUserController extends BaseController {

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysRoleService sysRoleService;

	/**
	 * 列表页面
	 *
	 */
	@GetMapping("/index")
	@RequiresPermissions("/user/index")
	public String index() {
		return "/system/user/index";
	}

	/**
	 * 获取表格数据
	 * @param user
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@GetMapping(value = "/getpage.do")
	@RequiresPermissions("/user/index")
	@ResponseBody
	public PageHelper<SysUser> getPage(UserModel user,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "sortOrder",required = false) String sortOrder,
			@RequestParam(value = "sortName",required = false) String sortName) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("username", user.getUsername());
		param.put("nickname", user.getNickname());
		param.put("status", user.getStatus());
		//获取用户列表
		BaseSpecification<SysUser> sysUser = new BaseSpecification<SysUser>(param, SysUser.class);
		Page<SysUser> page = sysUserService.getPageList(sysUser, PageRequest.of(pageNumber - 1, pageSize,Sort.Direction.DESC,"id"));
		
		// // 创建匹配器，进行动态查询匹配
		// ExampleMatcher matcher = ExampleMatcher.matching().
		// withMatcher("nickname", match -> match.contains()).
		// withIgnorePaths("password", "salt", "roles", "isRole");
		//
		// // 获取用户列表
		// Example<SysUser> example = MyExample.of(user, matcher);
		// Page<SysUser> list = sysUserService.getPageList(example, pageNumber,
		// pageSize);
		
		PageHelper<SysUser> ph = new PageHelper<SysUser>(page);
		ph.setOffset(pageNumber);
		ph.setLimit(pageSize);
		return ph;
	}

	/**
	 * 通过id获取用户信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/get.do")
	@RequiresPermissions("/user/index")
	@ResponseBody
	public RestResult<UserModel> getUserById(@RequestParam Long id){
		SysUser user = sysUserService.getId(id);
		Assert.notNull(user,"用户不存在");
		List<String> roleIds = Lists.newArrayList();
		for (SysRole sysRole:user.getRoles()) {
			roleIds.add(String.valueOf(sysRole.getId()));
		}
		UserModel userModel = new UserModel();
		FormBeanUtil.copyProperties(user, userModel);
		userModel.setRoleIds(roleIds);
		return RestResultGenerator.success(userModel);
	}
	
	
	/**
	 * 检查登录名是否存在
	 * @param id
	 * @param loginName
	 * @return
	 */
	@RequestMapping(value = "/checkLoginName.do",method = RequestMethod.POST)
	@ResponseBody
	public boolean checkLoginName(@RequestParam(required=false) Long id,@RequestParam("userName") String userName){
		if(StringUtils.isEmpty(userName)) {
			return Boolean.TRUE;
		} else {
			SysUser sysUser = sysUserService.findByUsername(userName);
			return sysUser == null || sysUser.getId() == id;
		}
	}
	
	/**
	 * 跳转到添加页面
	 */
	@GetMapping("/add")
	@RequiresPermissions("/user/add")
	public String toAdd() {
		return "/system/user/add";
	}

	/**
	 * 跳转到编辑页面
	 */
	@GetMapping("/edit/ids/{id}")
	@RequiresPermissions("/user/edit")
	public String toEdit(@PathVariable("id") Long id, Model model) {
		SysUser user = sysUserService.getId(id);
		model.addAttribute("user", user);
		return "/system/user/add";
	}

	/**
	 * 保存添加/修改的数据
	 *
	 * @param userForm
	 *            表单验证对象
	 */
	@PostMapping("/save")
	@RequiresPermissions({ "/user/add", "/user/edit" })
	@ResponseBody
	@Logger(key = UserAction.USER_SAVE, action = UserAction.class)
	public RestResult<?> save(@Validated UserModel userForm,BindingResult bindingResult) {
		// 验证数据是否合格
		if (userForm.getId() == null) {
			// 判断账号是否重复
			SysUser soleUser = sysUserService.getByName(userForm.getUsername(), StatusEnum.FREEZED.getCode());
			if (soleUser != null) {
				throw new ResponseException(ResultEnum.USER_EXIST);
			}

			// 判断密码是否为空
			if (userForm.getPassword().isEmpty() || "".equals(userForm.getPassword().trim())) {
				throw new ResponseException(ResultEnum.USER_PWD_NULL);
			}

			// 判断两次密码是否一致
			if (!userForm.getPassword().equals(userForm.getConFirmPassword())) {
				throw new ResponseException(ResultEnum.USER_INEQUALITY);
			}

			// 对密码进行加密
			String salt = ShiroAuthManager.getRandomSalt();
			String encrypt = ShiroAuthManager.encrypt(userForm.getPassword(), salt);
			userForm.setPassword(encrypt);
			//userForm.setSalt(salt);
		}

		// 将验证的数据复制给实体类
		SysUser user = new SysUser();
		if (userForm.getId() != null) {
			// 不允许操作超级管理员数据
			if (userForm.getId().equals(AdminConst.SUPER_ADMIN_ID)
					&& !ShiroAuthManager.getSysUser().getId().equals(AdminConst.SUPER_ADMIN_ID)) {
				throw new ResponseException(ResultEnum.NO_ADMIN_AUTH);
			}
			// 判断账号是否重复
			user = sysUserService.getByName(userForm.getUsername(), StatusEnum.FREEZED.getCode());
			if (user != null && !user.getId().equals(userForm.getId())) {
				throw new ResponseException(ResultEnum.USER_EXIST);
			}
			String[] ignore = { "password", "salt", "picture", "roles", "isRole" };
			FormBeanUtil.copyProperties(userForm, user, ignore);
		} else {
			FormBeanUtil.copyProperties(userForm, user);
		}

		// 保存数据
		sysUserService.save(user);
		return RestResultGenerator.success("保存成功");
	}

	/**
	 * 跳转到详细页面
	 */
	@GetMapping("/detail/{id}")
	@RequiresPermissions("/user/detail")
	public String toDetail(@PathVariable("id") Long id, Model model) {
		SysUser user = sysUserService.getId(id);
		model.addAttribute("user", user);
		return "/system/user/detail";
	}

	/**
	 * 跳转到修改密码页面
	 */
	@GetMapping("/pwd")
	@RequiresPermissions("/user/pwd")
	public String toEditPassword(Model model, @RequestParam(value = "ids", required = false) List<Long> idList) {
		model.addAttribute("idList", idList);
		return "/system/user/pwd";
	}

	/**
	 * 修改密码
	 */
	@PostMapping("/pwd")
	@RequiresPermissions("/user/pwd")
	@ResponseBody
	@Logger(key = UserAction.EDIT_PWD, action = UserAction.class)
	public RestResult editPassword(String password, String confirm,
			@RequestParam(value = "ids", required = false) List<Long> idList) {

		// 判断密码是否为空
		if (password.isEmpty() || "".equals(password.trim())) {
			throw new ResponseException(ResultEnum.USER_PWD_NULL);
		}

		// 判断两次密码是否一致
		if (!password.equals(confirm)) {
			throw new ResponseException(ResultEnum.USER_INEQUALITY);
		}

		// 不允许操作超级管理员数据
		if (idList.contains(AdminConst.SUPER_ADMIN_ID)
				&& !ShiroAuthManager.getSysUser().getId().equals(AdminConst.SUPER_ADMIN_ID)) {
			throw new ResponseException(ResultEnum.NO_ADMIN_AUTH);
		}

		// 修改密码，对密码进行加密
		List<SysUser> userList = sysUserService.getIdList(idList);
		userList.forEach(user -> {
			String salt = ShiroAuthManager.getRandomSalt();
			String encrypt = ShiroAuthManager.encrypt(password, salt);
			user.setPassword(encrypt);
			user.setSalt(salt);
		});

		// 保存数据
		sysUserService.save(userList);
		return RestResultGenerator.success("修改成功");
	}

	/**
	 * 跳转到角色分配页面
	 */
	@GetMapping("/role")
	@RequiresPermissions("/user/role")
	public String toRole(@RequestParam(value = "ids") Long id, Model model) {
		// 获取指定用户角色列表
		SysUser user = sysUserService.getId(id);
		Set<SysRole> authRoles = user.getRoles();
		// 获取全部菜单列表
		Sort sort = new Sort(Sort.Direction.ASC, "createDate");
		List<SysRole> list = sysRoleService.getList(sort);
		// 融合两项数据
		list.forEach(role -> {
			if (authRoles.contains(role)) {
				role.setRemark("auth:true");
			} else {
				role.setRemark("");
			}
		});

		model.addAttribute("id", id);
		model.addAttribute("list", list);
		return "/system/user/role";
	}

	/**
	 * 保存角色分配信息
	 */
	@PostMapping("/role")
	@RequiresPermissions("/user/role")
	@ResponseBody
	@Logger(key = UserAction.EDIT_ROLE, action = UserAction.class)
	public RestResult auth(@RequestParam(value = "id", required = true) Long id,
			@RequestParam(value = "roleId", required = false) List<Long> roleIds) {

		// 不允许操作超级管理员数据
		if (id.equals(AdminConst.SUPER_ADMIN_ID)
				&& !ShiroAuthManager.getSysUser().getId().equals(AdminConst.SUPER_ADMIN_ID)) {
			throw new ResponseException(ResultEnum.NO_ADMIN_AUTH);
		}

		// 将查询的数据关联起来
		SysUser user = sysUserService.getId(id);
		List<SysRole> roleList = null;
		if (roleIds != null) {
			roleList = sysRoleService.getIdList(roleIds);
			user.setRoles(new HashSet<>(roleList));
			user.setIsRole(UserIsAdminEnum.YES.getCode());
		} else {
			user.setRoles(null);
			user.setIsRole(UserIsAdminEnum.NO.getCode());
		}

		// 保存数据
		sysUserService.save(user);
		return RestResultGenerator.success("保存成功");
	}

	/**
	 * 获取用户头像
	 */
	@GetMapping("/picture")
	public void picture(String p, HttpServletResponse response) throws IOException {
		String defaultPath = "/images/user-picture.jpg";
		if (!(StringUtils.isEmpty(p) || p.equals(defaultPath))) {
			ProjectProperties properties = SpringContextHolder.getBean(ProjectProperties.class);
			String fuPath = properties.getFileUploadPath();
			String spPath = properties.getStaticPathPattern().replace("*", "");
			String s = fuPath + p.replace(spPath, "");
			File file = new File(fuPath + p.replace(spPath, ""));
			if (file.exists()) {
				FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
				return;
			}
		}
		Resource resource = new ClassPathResource("static" + defaultPath);
		FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
	}

	/**
	 * ok 启用
	 * freezed 冻结
	 * delete 删除
	 * 设置一条或者多条数据的状态
	 */
	@RequestMapping("/status/{param}")
	@RequiresPermissions("/user/status")
	@ResponseBody
	@Logger(name = "用户状态", action = StatusAction.class)
	public RestResult delete(@PathVariable("param") String param,
			@RequestParam(value = "ids", required = false) List<Long> idList) {
		// 不能修改超级管理员状态
		if (idList.contains(AdminConst.SUPER_ADMIN_ID)) {
			throw new ResponseException(ResultEnum.NO_ADMIN_STATUS);
		}

		try {
			// 获取状态StatusEnum对象
			StatusEnum statusEnum = StatusEnum.valueOf(param.toUpperCase());
			// 更新状态
			Integer count = sysUserService.updateStatus(statusEnum, idList);
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
