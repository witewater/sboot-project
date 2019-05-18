package cn.wendong.admin.core.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现用户退出时清除所有缓存
 * @author mubai
 *
 * 2017年6月19日 上午11:24:25
 */
public class SysLogoutFilter extends LogoutFilter {

	private Logger logger = LoggerFactory.getLogger(SysLogoutFilter.class);
	
	@Override
    protected boolean preHandle(ServletRequest req, ServletResponse resp) throws Exception {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response=(HttpServletResponse) resp;
		
		if(logger.isDebugEnabled()){
			logger.debug("退出，所有缓存清除完毕！");
		}
		
		String domain = request.getServerName();
		if (domain.indexOf(".") > -1) {
			domain= domain.substring(domain.indexOf(".") + 1);
		}
		
		Subject subject = getSubject(request, response);
		String redirectUrl = getRedirectUrl(request, response, subject);
        try {
            subject.logout();
        } catch (SessionException ise) {
           ise.printStackTrace();
        }
        issueRedirect(request, response, redirectUrl);
        //返回false表示不执行后续的过滤器，直接返回跳转到登录页面
        return false;
    }
}
