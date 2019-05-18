package cn.wendong.admin.sys.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wendong.admin.common.page.RestResult;
import cn.wendong.admin.common.page.RestResultGenerator;
import cn.wendong.admin.core.config.properties.ProjectProperties;
import cn.wendong.admin.core.enums.ResultEnum;
import cn.wendong.admin.core.enums.UserIsAdminEnum;
import cn.wendong.admin.core.exception.ResponseException;
import cn.wendong.admin.core.locale.i18n.LocaleMessageSourceService;
import cn.wendong.admin.core.log.action.UserAction;
import cn.wendong.admin.core.log.annotation.Logger;
import cn.wendong.admin.core.shiro.CaptchaException;
import cn.wendong.admin.core.shiro.ShiroAuthManager;
import cn.wendong.admin.core.shiro.SimpleUsernamePasswordToken;
import cn.wendong.admin.sys.entity.SysUser;
import cn.wendong.admin.sys.utils.CaptchaHelper;
import cn.wendong.admin.sys.utils.UserUtils;
import cn.wendong.tools.utils.ServletContextUtils;

/**
 * 登录控制层
 * 
 * @author MB
 * @date 2018-12-02
 */
@Controller
public class LoginController implements ErrorController {

	@Autowired
	private ProjectProperties projectProperties;
	
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;
	
	/**
	 * 跳转到登录页面
	 */
	@GetMapping("/login")
	public String toLogin(HttpServletRequest request, HttpServletResponse response, Model model) {
		// 地址中如果包含JSESSIONID，则跳转一次，去掉JSESSIONID信息。
		if (StringUtils.containsIgnoreCase(request.getRequestURI(), ";JSESSIONID=")) {
			String queryString = request.getQueryString();
			queryString = queryString == null ? "" : "?" + queryString;
			ServletContextUtils.redirectUrl(request, response, "/login.do" + queryString);
			return null;
		}
		model.addAttribute("isCaptcha", projectProperties.isUseCaptcha());
		System.out.println("sds:"+localeMessageSourceService.getMessage("login.password"));
		return "/login";
	}

	/**
	 * 实现登录
	 */
	@PostMapping("/login")
	@ResponseBody
	@Logger(key = UserAction.USER_LOGIN, action = UserAction.class)
	public RestResult<?> login(String username, String password, String captcha, String rememberMe) {
		// 判断账号密码是否为空
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			throw new ResponseException(ResultEnum.USER_NAME_PWD_NULL);
		}
		Map<String, Object> map = new HashMap<>();
		try {
			// 1.获取Subject主体对象
			Subject subject = SecurityUtils.getSubject();
			// 2.封装用户数据
			SimpleUsernamePasswordToken token = new SimpleUsernamePasswordToken(username, password, rememberMe,
					captcha);
			// 判断是否自动登录
			if (StringUtils.isNotBlank(rememberMe)) {
				token.setRememberMe(true);
			} else {
				token.setRememberMe(false);
			}
			// 3.执行登录，进入自定义Realm类中
			subject.login(token);

			// 判断是否拥有后台角色
			SysUser user = ShiroAuthManager.getSysUser();
			if (user.getIsRole().equals(UserIsAdminEnum.YES.getCode())) {
				return RestResultGenerator.success("登录成功", "/");
			} else {
				return RestResultGenerator.error("您不是后台管理员！");
			}
			
		} catch (CaptchaException cte) {
			map.put("isCaptcha", UserUtils.isValidateCodeLogin(username, true, false));
			return RestResultGenerator.error("验证码错误",map);
		} catch (UnknownAccountException uae) {
			return RestResultGenerator.error("未知账户");
		} catch (IncorrectCredentialsException ice) {
			map.put("isCaptcha", UserUtils.isValidateCodeLogin(username, true, false));
			return RestResultGenerator.error("账户密码错误",map);
		} catch (LockedAccountException lae) {
			return RestResultGenerator.error("账户已锁定");
		} catch (ExcessiveAttemptsException eae) {
			return RestResultGenerator.error("用户名或密码错误次数大于5次,账户已锁定");
		} catch (DisabledAccountException sae) {
			return RestResultGenerator.error("帐号已经禁止登录");
		} catch (AuthenticationException aue) {
			map.put("isCaptcha", UserUtils.isValidateCodeLogin(username, true, false));
			return RestResultGenerator.error("用户名或密码错误");
		}  catch (Exception e) {
			return RestResultGenerator.error("系统登录异常");
		}
	}

	//获取otp短信接口
	@RequestMapping(value="/getotp",method=RequestMethod.POST)
	@ResponseBody
	public RestResult<?> getOtp(@RequestParam("telPhone")String telPhone){
		//按照一定规则生成otp验证码
		
		//将otp验证码通对应用户的手机号关联
		
		//将otp验证码通过短息通道发送给用户
		
		
		return RestResultGenerator.success();
	}
	
	
	/**
	 * 验证码图片
	 */
	@GetMapping("/captcha")
	public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CaptchaHelper.createImage(request, response);
	}

	/**
	 * 退出登录
	 */
	@GetMapping("/logout")
	public String logout() {
		SecurityUtils.getSubject().logout();
		
		return "redirect:/login";
	}

	/**
	 * 权限不足页面
	 */
	@GetMapping("/noAuth")
	public String noAuth() {
		return "/error/noAuth";
	}

	/**
	 * 自定义错误页面
	 */
	@Override
	public String getErrorPath() {
		return "/error";
	}

	/**
	 * 处理错误页面
	 */
	@RequestMapping("/error")
	public String handleError(Model model, HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		String message = request.getParameter("javax.servlet.error.message");
		String errorMsg = "好像出错了！";
		if (statusCode == 404) {
			errorMsg = "页面未找到！";
		}
		model.addAttribute("statusCode", statusCode);
		model.addAttribute("message", message);
		model.addAttribute("msg", errorMsg);
		return "/error/error";
	}

}
