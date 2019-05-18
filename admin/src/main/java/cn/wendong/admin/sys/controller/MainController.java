package cn.wendong.admin.sys.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.wendong.admin.common.base.BaseController;
import cn.wendong.admin.common.enums.BaseReultEnum;
import cn.wendong.admin.common.page.RestResult;
import cn.wendong.admin.common.page.RestResultGenerator;
import cn.wendong.admin.core.enums.ResultEnum;
import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.core.exception.ResponseException;
import cn.wendong.admin.core.shiro.ShiroAuthManager;
import cn.wendong.admin.core.utils.FormBeanUtil;
import cn.wendong.admin.core.utils.SpringContextHolder;
import cn.wendong.admin.sys.entity.SysFile;
import cn.wendong.admin.sys.entity.SysMenu;
import cn.wendong.admin.sys.entity.SysUser;
import cn.wendong.admin.sys.service.SysUserService;
import cn.wendong.admin.sys.service.model.UserModel;
import cn.wendong.admin.sys.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 登录系统主界面
 * @author MB
 * @date 2018-12-02
 */
@Controller
@Api(value = "MainController|主控制器")
public class MainController extends BaseController{

    @Autowired
    private SysUserService sysUserService;

    /**
     * 后台主体内容
     */
    @GetMapping("/")
    @RequiresPermissions("user")
    public String main(Model model){
        SysUser User = ShiroAuthManager.getSysUser();
        // 登录成功后，验证码计算器清零
        UserUtils.isValidateCodeLogin(User.getUsername(), false, true);
        model.addAttribute("user",User);
//    	// 封装菜单树形数据
//        Map<Long,SysMenu> keyMenu = new HashMap<>();
//        User.getRoles().forEach(role -> {
//            role.getMenus().forEach(menu -> {
//                if(menu.getStatus().equals(StatusEnum.NORMAL.getCode())){
//                    keyMenu.put(menu.getId(), menu);
//                }
//            });
//        });
//        Map<Long,SysMenu> treeMenu = new HashMap<>();
//        keyMenu.forEach((id, menu) -> {
//            if(!menu.getType().equals(MenuTypeEnum.NOT_MENU.getCode())){
//                if(keyMenu.get(menu.getPid()) != null){
//                    keyMenu.get(menu.getPid()).getChildren().put(Long.valueOf(menu.getSort()), menu);
//                }else{
//                    if(menu.getType().equals(MenuTypeEnum.TOP_LEVEL.getCode())){
//                        treeMenu.put(Long.valueOf(menu.getSort()), menu);
//                    }
//                }
//            }
//        });
//        model.addAttribute("treeMenu",treeMenu);
        return "/index";
    }

    @PostMapping("/getUserMenus")
    @RequiresPermissions("user")
    @ResponseBody
    public RestResult<?> getUserMenus() {
    	SysUser User = ShiroAuthManager.getSysUser();
    	List<SysMenu> menus = new LinkedList<SysMenu>();
        User.getRoles().forEach(role -> {
            role.getMenus().forEach(menu -> {
                if(menu.getStatus().equals(StatusEnum.NORMAL.getCode())){
                	menus.add(menu);
                }
            });
        });
        //按照升序
       // menus.stream().collect(Collectors.groupingBy(SysMenu::getSort,TreeMap::new,Collectors.toList()));
        
        //排序 根据sort属性升序
        Collections.sort(menus, new Comparator<SysMenu>() {
			@Override
			public int compare(SysMenu o1, SysMenu o2) {
				return o1.getSort().compareTo(o2.getSort());
			}
		});
        
		return RestResultGenerator.success(menus);
	}
    
    
    @PostMapping("/getUserInfo")
    @RequiresPermissions("user")
    @ResponseBody
	public RestResult<?> getUserInfo() {
		SysUser user = sysUserService.getId(ShiroAuthManager.getUserId());
		Assert.notNull(user, "用户存在");
		//user.setPhotoFullUrl(FileHandlerFactory.getFullUrl(user.getPhoto()));
		UserModel userModel = new UserModel();
		FormBeanUtil.copyProperties(user, userModel); 
		if(user.getSysOffice() != null){
			userModel.setOfficeName(user.getSysOffice().getName());
			userModel.setOfficeId(user.getSysOffice().getId());
		}
		return RestResultGenerator.success(userModel);
	}

//	@PostMapping("/updateInfo")
//	@RequiresPermissions("user")
//	@ResponseBody
//	public RestResult<?> updateInfo(SysUser user) {
//        // 保存数据
//        sysUserService.save(user);
//		return RestResultGenerator.success();
//	}
    
    /**
     * 主页
     */
    @GetMapping("/home")
    @RequiresPermissions("user")
    public String index(Model model){
        return "/system/main/home";
    }

    /**
     * 修改用户头像
     */
    @PostMapping("/user_picture")
    @RequiresPermissions("user")
    @ResponseBody
    public RestResult<?> userPicture(@RequestParam("picture") MultipartFile picture, HttpServletResponse response){
        SysFileController fileController = SpringContextHolder.getBean(SysFileController.class);
        RestResult<?> imageResult = fileController.uploadPicture(picture);
        if(imageResult.getCode().equals(BaseReultEnum.SUCCESS.getCode())){
            SysUser subject = ShiroAuthManager.getSysUser();
            subject.setPicture(((SysFile) imageResult.getData()).getPath());
            sysUserService.save(subject);
            ShiroAuthManager.resetCookieRememberMe();
            return RestResultGenerator.success("保存成功");
        }else {
            return imageResult;
        }
    }
    
//    /**
//     * 跳转到个人信息页面
//     */
//    @GetMapping("/user_info")
//    @RequiresPermissions("user")
//    public String toUserInfo(Model model){
//        SysUser user = ShiroAuthManager.getSysUser();
//        model.addAttribute("user", user);
//        return "/system/main/userInfo";
//    }
    
    /**
     * 保存修改个人信息
     * description:校验的信息会存放到其后的BindingResult中。注意，必须相邻
     */
    @PostMapping("/user_info")
    @RequiresPermissions("user")
    @ResponseBody
    public RestResult<?> userInfo(UserModel userForm){
    	Assert.hasLength(userForm.getNickname(), "昵称为空");
        // 不允许修改用户名
        SysUser user = ShiroAuthManager.getSysUser();
        Assert.notNull(user, "用户存在");
        if(!user.getUsername().equals(userForm.getUsername())){
            throw new ResponseException(ResultEnum.STATUS_ERROR);
        }
        // 将验证的数据复制给实体类
        String[] ignore = {"id", "password", "salt", "picture", "roles", "isRole"};
        FormBeanUtil.copyProperties(userForm, user, ignore);
        // 保存数据
        sysUserService.save(user);
        ShiroAuthManager.resetCookieRememberMe();
        return RestResultGenerator.success("保存成功");
    }

//    /**
//     * 跳转到修改密码页面
//     */
//    @GetMapping("/edit_pwd")
//    @RequiresPermissions("user")
//    public String toEditPwd(){
//        return "/system/main/editPwd";
//    }

    /**
     * 保存修改密码
     */
    @PostMapping("/edit_pwd")
    @RequiresPermissions("user")
    @ResponseBody
    @ApiOperation(value="修改用户密码", notes="根据用户id修改密码")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="query", name = "oldPassword", value = "旧密码", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "password", value = "新密码", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "conFirmPassword", value = "确认密码", required = true, dataType = "String")
    })
    public RestResult<?> editPwd(String oldPassword, String password, String conFirmPassword){
        // 判断原来密码是否有误
        SysUser oldPwdUser = ShiroAuthManager.getSysUser();
        String oldPwd = ShiroAuthManager.encrypt(oldPassword, oldPwdUser.getSalt());
        if (oldPassword.isEmpty() || "".equals(oldPassword.trim()) || !oldPwd.equals(oldPwdUser.getPassword())) {
            throw new ResponseException(ResultEnum.USER_OLD_PWD_ERROR);
        }

        // 判断密码是否为空
        if (password.isEmpty() || "".equals(password.trim())) {
            throw new ResponseException(ResultEnum.USER_PWD_NULL);
        }

        // 判断两次密码是否一致
        if (!password.equals(conFirmPassword)) {
            throw new ResponseException(ResultEnum.USER_INEQUALITY);
        }

        // 修改密码，对密码进行加密
        SysUser newPwdUser = sysUserService.getId(oldPwdUser.getId());
        String salt = ShiroAuthManager.getRandomSalt();
        String encrypt = ShiroAuthManager.encrypt(password, salt);
        newPwdUser.setPassword(encrypt);
        newPwdUser.setSalt(salt);

        // 保存数据
        sysUserService.save(newPwdUser);
        return RestResultGenerator.success("修改成功");
    }
    
    /**
     * 修改个人密码是检查密码是否正确
     * @param oldPassword
     * @return
     */
    @PostMapping("/checkPassword")
    @RequiresPermissions("user")
    @ResponseBody
    public boolean checkPassword(@RequestParam String oldPassword) {
    	return sysUserService.validatePwd(oldPassword, ShiroAuthManager.getUserId());
    }
    
}