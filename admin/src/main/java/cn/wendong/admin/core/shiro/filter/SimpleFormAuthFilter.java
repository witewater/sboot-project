package cn.wendong.admin.core.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.wendong.admin.core.shiro.SimpleUsernamePasswordToken;
import cn.wendong.admin.sys.entity.SysUser;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义shiro 登录filter
 * 账密未认证过会触发这个拦截器
 * 表单验证
 * 自定义HTTP 请求头，310 未登录
 * @author MB
 * @date 2018-12-02
 */
@Slf4j
public class SimpleFormAuthFilter extends FormAuthenticationFilter {

	public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";
	public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
	public static final String DEFAULT_MESSAGE_PARAM = "message";
	/** 自定义保存到session中的请求数据 用于页面跳转 */
	public static final String DIY_SAVED_REQUEST_RARAM = "diySavedRequest";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;
	private String mobileLoginParam = DEFAULT_MOBILE_PARAM;
	private String messageParam = DEFAULT_MESSAGE_PARAM;
	private String savedRequestParam = DIY_SAVED_REQUEST_RARAM;

	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = super.getUsername(request);
		String password = super.getPassword(request);
		if (password == null) {
			password = "";
		}
		boolean rememberMe = super.isRememberMe(request);
		String host = getHost(request);
		String captcha = this.getCaptcha(request);
		boolean mobile = this.isMobileLogin(request);
		return new SimpleUsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha, mobile);
	}

	public String getCaptchaParam() {
		return captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, this.getCaptchaParam());
	}

	public String getMobileLoginParam() {
		return mobileLoginParam;
	}

	protected boolean isMobileLogin(ServletRequest request) {
		return WebUtils.isTrue(request, this.getMobileLoginParam());
	}

	public String getMessageParam() {
		return messageParam;
	}

	public String getSavedRequestParam() {
		return savedRequestParam;
	}

	protected String SavedRequest(ServletRequest request) {
		return WebUtils.getCleanParam(request, this.getSavedRequestParam());
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// 获取请求URI
		String requestURI = getPathWithinApplication(request);
		log.info("shiro未登录拦截URI：{}", requestURI);
		HttpServletResponse res = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
//		if (isAjax(request)) {//是否ajax请求
//			
//		}else{
//			return super.onAccessDenied(request, response);
//		}
		String method = req.getMethod();
		// 如果是跨域的options 请求则放过，复杂contentType跨域，先发一个options 请求
		if (!RequestMethod.OPTIONS.name().equalsIgnoreCase(method)) {
			res.setStatus(310);
		}
		return false;
	}

	/**
	 * 登录失败调用事件
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		String className = e.getClass().getName(), message = "";
		if (IncorrectCredentialsException.class.getName().equals(className)
				|| UnknownAccountException.class.getName().equals(className)) {
			message = "用户或密码错误， 请重试。";
		} else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")) {
			message = StringUtils.replace(e.getMessage(), "msg:", "");
		} else {
			message = "系统出现点问题，请稍后再试！";
			e.printStackTrace(); // 输出到控制台
		}
		request.setAttribute(getFailureKeyAttribute(), className);
		request.setAttribute(getMessageParam(), message);
		return true;
	}

	/**
	 * 登录成功
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
		Session session = subject.getSession();
		session.setAttribute(this.getSavedRequestParam(), savedRequest);
		return super.onLoginSuccess(token, subject, request, response);
	}
	
	/**
	 * 记住我过滤
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		Subject subject = getSubject(request, response);

		// 如果 isAuthenticated 为 false 证明不是登录过的，同时 isRememberd 为true
		// 证明是没登陆直接通过记住我功能进来的
		if (!subject.isAuthenticated() && subject.isRemembered()) {

			// 获取session看看是不是空的
			Session session = subject.getSession(true);

			// 随便拿session的一个属性来看session当前是否是空的，我用userSessionId，你们的项目可以自行发挥
			if (session.getAttribute("userSessionId") == null) {
				// 初始化
				SysUser user = (SysUser) subject.getPrincipal();
				session.setAttribute("userSession", user);
				session.setAttribute("userSessionId", user.getId());
			}
		}

		// 这个方法本来只返回 subject.isAuthenticated() 现在我们加上 subject.isRemembered()
		// 让它同时也兼容remember这种情况
		return subject.isAuthenticated() || subject.isRemembered();
	}
	
	@Override
	public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception)
			throws Exception {
		if (exception != null) {
			log.error("shiro filter afterCompletion", exception);
		}
	}
	
//	private boolean isAjax(ServletRequest request) {
//		String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
//		if ("XMLHttpRequest".equalsIgnoreCase(header)) {
//			return true;
//		}
//		return false;
//	}
}
