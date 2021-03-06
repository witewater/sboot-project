package cn.wendong.admin.core.locale.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 语言拦截器
 * 请求地址的参数,默认为：locale
 * @author MB yangtdo@qq.com
 * @date 2019-01-15
 */
public class LocaleChangeInterceptor extends HandlerInterceptorAdapter {
	/**
	 * Default name of the locale specification parameter: "locale".
	 */
	public static final String DEFAULT_PARAM_NAME = "locale";
	private String paramName = DEFAULT_PARAM_NAME;

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamName() {
		return this.paramName;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Locale newLocale = getLocale(request.getParameter(getParamName()));
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);

		if (localeResolver == null) {
			throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
		}

		localeResolver.setLocale(request, response, newLocale);

		return true;
	}

	// 根据language 获取Locale
	public Locale getLocale(String language) {
		Locale locale = LocaleContextHolder.getLocale();
		if (language != null && language.equals("en")) {
			locale = new Locale("en", "US");
		} else {
			locale = new Locale("zh", "CN");
		}
		return locale;
	}
}
