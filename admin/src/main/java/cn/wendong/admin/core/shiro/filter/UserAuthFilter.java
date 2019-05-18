package cn.wendong.admin.core.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * 处理session超时问题
 * 
 * 登录用户与记住我的用户均可通过
 * @author MB
 * @date 2018-12-02
 */
public class UserAuthFilter extends AccessControlFilter {

	private static Logger logger = LoggerFactory.getLogger(UserAuthFilter.class);
	
	@Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		logger.debug("UserAuthFilter");
		 //如果访问登录
		if (isLoginRequest(request, response)) {
            return true;
        } else {
            Subject subject = getSubject(request, response);
            return subject.getPrincipal() != null;
        }
    }

	//如果isAccessAllowed返回false 则执行这个方法
	@Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        if (httpRequest.getHeader("X-Requested-With") != null
                && httpRequest.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
            httpResponse.sendError(HttpStatus.UNAUTHORIZED.value());
        } else {
        	//保存请求路径调转到登录页面
        	//saveRequestAndRedirectToLogin(request, response);
            redirectToLogin(request, response);
        }
       return false;
    }
}
