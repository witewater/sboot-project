package cn.wendong.admin.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 设置允许跨域
 * 
 * @author MB yangtdo@qq.com
 * @date 2018-12-23
 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 设置允许跨域的路径
		registry.addMapping("/cors/**")
				// 设置允许跨域请求的域名
				.allowedOrigins("*")
				// 设置允许的header
				.allowedHeaders("*")
				// 是否允许证书 不再默认开启
				.allowCredentials(true)
				// 设置允许的方法
				.allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
				// 跨域允许时间
				.maxAge(3600);
	}

//	/**
//	 * 配置@EnableWebMvc
//     * 配置静态页面请求处理  
//     */
//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setPrefix(null);
//        viewResolver.setSuffix(null);
//        registry.viewResolver(viewResolver);
//        registry.order(1);
//    }
	
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		String os = System.getProperty("os.name");
//		if (os.toLowerCase().startsWith("win")) { // 如果是Windows系统
//			registry.addResourceHandler("/store/**")
//					 // /store/**表示在磁盘store目录下的所有资源会被解析为以下的路径
//					.addResourceLocations("file:G:/store/") // 媒体资源
//					.addResourceLocations("classpath:/META-INF/resources/"); // swagger2页面
//		} else { // linux和mac
//			registry.addResourceHandler("/store/**").addResourceLocations("file:/resources/smallapple/") // 媒体资源
//					.addResourceLocations("classpath:/META-INF/resources/"); // swagger2页面;
//		}
//	}

}