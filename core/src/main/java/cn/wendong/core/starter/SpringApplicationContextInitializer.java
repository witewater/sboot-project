package cn.wendong.core.starter;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * SpringBoot启动类代码
 * 方式一：使用spring.factories方式 /META-INF/spring.factories
 * 方式二：application.properties添加配置方式    context.initializer.classes对应的类并执行对应的initialize方法
 * 方式三：add一个实现了ApplicationContextInitializer的类
 * @author MB yangtdo@qq.com
 * @date 2019-01-03
 */
public class SpringApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		System.out.println("sboot正在启动");
	}

}
