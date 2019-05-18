package cn.wendong.admin.core.shiro.filter;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wendong.admin.core.shiro.ShiroAuthManager;
import cn.wendong.admin.sys.entity.SysUser;

/**
 * 页面权限
 * 
 * @author MB yangtdo@qq.com
 * @date 2018-12-09
 */
public class PageAccessFilter extends AuthorizationFilter {

	private static Logger logger = LoggerFactory.getLogger(PageAccessFilter.class);

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		// 获取请求URI
		String requestURI = getPathWithinApplication(request);
		logger.info("shiro页面拦截URI:{}", requestURI);
		//获取session中的菜单信息
		@SuppressWarnings("unchecked")
		List<String> menuUrls = (List<String>) ShiroAuthManager.getSessionAttribute("SessionUser-menus");
		if (null == menuUrls) {
			SysUser user = ShiroAuthManager.getSysUser();
			if (null == user) {
				return false;
			}
			logger.info("从数据库查询用户id={}所有MenuUrls，并存入session-{}", user.getId(), ShiroAuthManager.getSession().getId());
			// menuUrls = userService.queryMenuUrls(user.getId());
			ShiroAuthManager.setSessionAttribute("SessionUser-menus", menuUrls);
		}
		if (menuUrls.contains(requestURI)) {
			return true;
		}
		return false;
	}

}
