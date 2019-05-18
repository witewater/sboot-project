package cn.wendong.tools.utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * spring 获得ServletContext 在spring容器中
 * 
 * @author mubai
 *
 *         2017年2月4日 下午3:11:45
 */
public class ServletContextUtils {
	
	/**
	 * 获得ServletContext 由WebApplicationContext获得
	 * 
	 * @return
	 */
	public static ServletContext getServletContext() {
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		ServletContext servletContext = webApplicationContext.getServletContext();
		return servletContext;
	}
	
	/**
	 * 通过spring的WebApplicationContext上下文对象读取bean对象
	 * servletContext获得WebApplicationContext
	 * 
	 * @param beanName
	 *            要读取的bean的名称
	 * @return 返回获取到的对象。获取不到返回null
	 */
	public static Object getBeanByWebApplicationContext(String beanName) {
		return WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean(beanName);
	}

	/**
	 * 获得绝对路径
	 * 
	 * @param path
	 * @return
	 */
	public static String get(String path) {
		String realPath = getServletContext().getRealPath(path);
		if (realPath == null) {
			realPath = getServletContext().getRealPath("/") + path;
		}
		return realPath;
	}

	/**
	 * 获取当前请求对象
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		try {
			return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取当前响应对象
	 * 
	 * @return
	 */
	public static HttpServletResponse getResponse() {
		try {
			return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 支持AJAX的页面跳转
	 */
	public static void redirectUrl(HttpServletRequest request, HttpServletResponse response, String url){
		try {
			if (isAjaxRequest(request)){
				request.getRequestDispatcher(url).forward(request, response); // AJAX不支持Redirect改用Forward
			}else{
				response.sendRedirect(request.getContextPath() + url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 是否是Ajax异步请求
	 * @param request
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		String accept = request.getHeader("accept");
		if (accept != null && accept.indexOf("application/json") != -1){
			return true;
		}
		String xRequestedWith = request.getHeader("X-Requested-With");
		if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1){
			return true;
		}
		return false;
	}
	
}
