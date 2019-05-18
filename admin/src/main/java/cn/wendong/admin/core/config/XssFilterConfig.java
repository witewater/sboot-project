package cn.wendong.admin.core.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.wendong.admin.core.config.properties.ProjectProperties;
import cn.wendong.admin.core.xssfilter.XssFilter;

@Configuration
public class XssFilterConfig {
	
	private static final int FILTER_ORDER = 1;

	@Bean
	public FilterRegistrationBean xssFilterRegistrationBean(ProjectProperties properties) {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new XssFilter());
		registration.setOrder(FILTER_ORDER);
		registration.setEnabled(properties.isXssEnabled());
		registration.addUrlPatterns(properties.getXssUrlPatterns().split(","));
		Map<String, String> initParameters = new HashMap<>();
		initParameters.put("excludes", properties.getXssExcludes());
		registration.setInitParameters(initParameters);
		return registration;
	}
	
}
