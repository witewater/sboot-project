package cn.wendong.admin.core.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * 国际化配置  直接实现WebMvcConfigurer （官方推荐）
 * 页面通过#{}来读取资源文件
 * @author MB yangtdo@qq.com
 * @date 2019-01-15
 */
@Configuration
public class LocaleInterceptorConfig implements WebMvcConfigurer {

	//使用session针对当前的会话有效，session失效，还原为默认状态
	@Bean(name="localeResolver")
    public LocaleResolver localeResolver() {
		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		//设置默认区域
	    sessionLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
	    return sessionLocaleResolver;
    }
	
//	@Bean(name="messageSource")
//    public ResourceBundleMessageSource resourceBundleMessageSource(){
//        ResourceBundleMessageSource source=new ResourceBundleMessageSource();
//        source.setBasename("i18n/messages");
//        source.setDefaultEncoding("UTF-8");
//        source.setUseCodeAsDefaultMessage(true);
//        return source;
//    }
	
	//切换语言过滤器
	@Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // 参数名
        lci.setParamName("lang");
        return lci;
    }
	
	//将过滤器并注册到spring mvc中
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
