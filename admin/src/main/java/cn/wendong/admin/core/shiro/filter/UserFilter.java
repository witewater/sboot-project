package cn.wendong.admin.core.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMethod;
/**
 *
 * 账密或者remember 的拦截器
 * 
 * 解决在remember的情况下，跨域请求options 请求无法携带cookie 的问题
 * 
 * @author MB yangtdo@qq.com
 * @date 2019-01-06
 * 
 */
public class UserFilter extends org.apache.shiro.web.filter.authc.UserFilter {
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		// 跨域的options 请求直接过
		if (RequestMethod.OPTIONS.name().equalsIgnoreCase(req.getMethod())) {
			return true;
		} else {
			res.setStatus(310);
			// 不走后续shiro 默认逻辑
			return false;
		}
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		return super.isAccessAllowed(request, response, mappedValue);
	}
}
