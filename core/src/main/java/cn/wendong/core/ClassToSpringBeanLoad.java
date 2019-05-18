package cn.wendong.core;

import java.util.List;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

/**
 * 添加到spring容器中加载bean
 * 
 * @author MB yangtdo@qq.com
 * @date 2019-01-03
 */
public class ClassToSpringBeanLoad {

	/**
	 * 在spring初始化容器之前，根据扫描路径将需要由spring管理的类注册进BeanFactory，
	 * 在spring初始化完成后，调用所有插件的boot class
	 * 
	 * @param path
	 * @param applicationContext
	 * @return
	 */
	public static void beanLoad(String path, ApplicationContext applicationContext) {
		//ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
		//DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
		//方法一
		//DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getParentBeanFactory();
		//方法二
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
		//根据obj的类型、创建一个新的bean、添加到Spring容器中，
		//注意BeanDefinition有不同的实现类，注意不同实现类应用的场景
		//BeanDefinition beanDefinition = new GenericBeanDefinition();
		ClassLoader beanClassLoader = beanFactory.getBeanClassLoader();
		List<Class<?>> clazzList = ClassScanner.loadClass(path, beanClassLoader);
		for (Class<?> clazz : clazzList) {
			//这里判断是否存在spring管理注解
			if (!clazz.isInterface()) {
				//让obj完成Spring初始化过程中所有增强器检验，只是不重新创建obj。
				applicationContext.getAutowireCapableBeanFactory().applyBeanPostProcessorsAfterInitialization(clazz, clazz.getClass().getName());
				//将obj以单例的形式入驻到容器中，此时通过obj.getClass().getName()或obj.getClass()都可以拿到放入Spring容器的Bean。
				beanFactory.registerSingleton(clazz.getClass().getName(), clazz);
				//BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
				//beanFactory.registerBeanDefinition(clazz.getName(), beanDefinitionBuilder.getRawBeanDefinition());
			}
		}
	}
	
}
