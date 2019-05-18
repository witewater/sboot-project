package cn.wendong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;	

/**
 * 注意该文件的包名启动项必须放在包根目录
 * @description 应用启动入口
 * @author MB
 * @date 2018/11/30
 */
@SpringBootApplication//默认启动类
@EnableJpaAuditing //开启jpa审核
@EnableCaching //开启缓存
@EnableScheduling // 开启定时任务
public class EntryAppliaction extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(EntryAppliaction.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EntryAppliaction.class);
    }
    
//    /**
//     * 设置匹配*.do后缀请求
//     * @param dispatcherServlet
//     * @return
//     */
//    @Bean
//    public ServletRegistrationBean<DispatcherServlet> servletRegistrationBean(DispatcherServlet dispatcherServlet) {
//        ServletRegistrationBean<DispatcherServlet> servletServletRegistrationBean = new ServletRegistrationBean<>(dispatcherServlet);
//        servletServletRegistrationBean.addUrlMappings("*.do");
//        return servletServletRegistrationBean;
//    }
    
}