package cn.wendong.admin.core.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.Filter;

import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import cn.wendong.admin.core.config.properties.ProjectProperties;
import cn.wendong.admin.core.shiro.LimitRetryHashedMatcher;
import cn.wendong.admin.core.shiro.ShiroAuthRealm;
import cn.wendong.admin.core.shiro.filter.KickoutSessionFilter;
import cn.wendong.admin.core.shiro.filter.SimpleFormAuthFilter;
import cn.wendong.admin.core.shiro.filter.UserAuthFilter;
import cn.wendong.admin.core.shiro.session.CacheSessionDAO;
import cn.wendong.admin.core.shiro.session.SessionDAO;

/**
 * shiro配置
 * 
 * @author MB
 * @date 2018-12-02
 */
@Configuration
public class ShiroConfiguration {

	/**
	 * ShiroFilterFactoryBean 工厂，设置对应的过滤条件和跳转条件 Filter Chain定义说明：
	 * 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
	 * 3、部分过滤器可指定参数，如perms，roles
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		
		/**
		 * 添加自定义拦截器，重写user认证方式，处理session超时问题
		 */
		HashMap<String, Filter> myFilters = new HashMap<>();
		myFilters.put("userAuth", new UserAuthFilter());
		myFilters.put("kickout", new KickoutSessionFilter());
		//myFilters.put("authc", new SimpleFormAuthFilter());
		shiroFilterFactoryBean.setFilters(myFilters);

		/**
		 * 过滤规则（注意优先级） —anon 无需认证(登录)可访问 —authc 必须认证才可访问 —perms[标识] 拥有资源权限才可访问
		 * —role 拥有角色权限才可访问 —user 认证和自动登录可访问
		 */
		Map<String, String> filterMap = new LinkedHashMap<String, String>();
		filterMap.put("/login", "anon");
		filterMap.put("/logout", "logout");
		filterMap.put("/captcha", "anon");
		filterMap.put("/noAuth", "anon");
		filterMap.put("/css/**", "anon");
		filterMap.put("/js/**", "anon");
		filterMap.put("/images/**", "anon");
		filterMap.put("/lib/**", "anon");
		filterMap.put("/plugins/**", "anon");
		filterMap.put("/favicon.ico", "anon");
		filterMap.put("/**", "userAuth");
		// filterMap.put("/**", "authc");

		// 设置过滤规则
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
		// 设置登录页面
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 首页
		shiroFilterFactoryBean.setSuccessUrl("/index");
		// 未授权错误页面
		shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");

		return shiroFilterFactoryBean;
	}

	/**
	 * 自定义的Realm
	 */
	@Bean
	public ShiroAuthRealm getAuthRealm(EhCacheManager ehCacheManager) {
		ShiroAuthRealm authRealm = new ShiroAuthRealm();
		authRealm.setCacheManager(ehCacheManager);
		LimitRetryHashedMatcher matcher = new LimitRetryHashedMatcher(ehCacheManager);
		matcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
		matcher.setHashIterations(2);// 散列的次数，比如散列两次，相当于md5(md5(""));
		authRealm.setCredentialsMatcher(matcher);
		return authRealm;
	}

	/**
	 * 权限管理，配置主要是Realm的管理认证 凭证匹配器
	 * 
	 * @param authRealm
	 * @param cacheManager
	 * @param sessionManager
	 * @param rememberMeManager
	 * @return
	 */
	@Bean
	public DefaultWebSecurityManager getSecurityManager(ShiroAuthRealm authRealm,EhCacheManager cacheManager,
			DefaultWebSessionManager sessionManager, CookieRememberMeManager rememberMeManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 将authenticator引入securityManager
		securityManager.setAuthenticator(authenticator());//注意这块代码放到了前面
		securityManager.setRealm(authRealm);
		securityManager.setSessionManager(sessionManager);
		securityManager.setRememberMeManager(rememberMeManager);
		securityManager.setCacheManager(cacheManager);
		return securityManager;
	}

	/**
	 * 认证器 配置多个realm的时候如何认证
	 * AtLeastOneSuccessfulStrategy 只要有一个或多个的Realm验证成功，那么认证将被视为成功。
	 * FirstSuccessfulStrategy 第一个认证成功，则认为认证成功，且后续realm将被忽略。
	 * AllSuccessfulStrategy 所有realm成功，认证才视为成功。
	 * @return
	 */
	@Bean
	public ModularRealmAuthenticator authenticator() {
		ModularRealmAuthenticator mra = new ModularRealmAuthenticator();
		/*
		 * AuthenticationStrategy接口有三种实现 这里以AtLeastOneSuccessfulStrategy作为身份认证策略
		 */
		AuthenticationStrategy as = new AtLeastOneSuccessfulStrategy();
		mra.setAuthenticationStrategy(as);
		return mra;
	}

	
	/**
	 * 缓存管理器-使用Ehcache实现缓存
	 * 
	 * @return
	 */
	@Bean
	public EhCacheManager ehCacheManager(net.sf.ehcache.CacheManager cacheManager) {
		EhCacheManager ecm = new EhCacheManager();
		//设置ehcache缓存的配置文件
		ecm.setCacheManagerConfigFile("classpath:ehcache.xml");
		ecm.setCacheManager(cacheManager);
		return ecm;
	}

	/**
	 * 注入SessionId生成器
	 * 
	 * @return
	 */
	@Bean
	public JavaUuidSessionIdGenerator sessionIdGenerator() {
		JavaUuidSessionIdGenerator jusi = new JavaUuidSessionIdGenerator();
		return jusi;
	}

	/**
	 * 会话DAO 
	 * @return
	 */
	@Bean
	public SessionDAO getSessionDAO() {
		//EnterpriseCacheSessionDAO ecsd = new EnterpriseCacheSessionDAO();
		CacheSessionDAO ecsd = new CacheSessionDAO();
		//设置Session缓存名字，默认就是shiro-activeSessionCache
		ecsd.setActiveSessionsCacheName("shiro-activeSessionCache");
		//用于生成会话ID，默认就是JavaUuidSessionIdGenerator，使用java.util.UUID生成
		ecsd.setSessionIdGenerator(sessionIdGenerator());
		return ecsd;
	}

	/**
	 * session管理器
	 * 
	 * @param sessionDAO
	 * @param cacheManager
	 * @param properties
	 * @return
	 */
	@Bean
	public DefaultWebSessionManager getDefaultWebSessionManager(SessionDAO sessionDAO, EhCacheManager cacheManager,
			ProjectProperties properties) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setCacheManager(cacheManager);
		sessionManager.setSessionDAO(sessionDAO);
		sessionManager.setGlobalSessionTimeout(properties.getGlobalSessionTimeout() * 1000);
		sessionManager.setSessionValidationInterval(properties.getSessionValidationInterval() * 1000);
		// 是否定时检查session失效没有
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.validateSessions();
		// 去掉登录页面地址栏jsessionid
		sessionManager.setSessionIdUrlRewritingEnabled(false);
		return sessionManager;
	}

	/**
	 * rememberMe管理器
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager(SimpleCookie rememberMeCookie) {
		CookieRememberMeManager manager = new CookieRememberMeManager();
		//cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)  
		manager.setCipherKey(Base64.decode("sgIQrqUVxa1OZRRIK3hLZw=="));
		manager.setCookie(rememberMeCookie);
		return manager;
	}

	/**
	 * 创建一个简单的Cookie对象
	 */
	@Bean
	public SimpleCookie rememberMeCookie(ProjectProperties properties) {
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		simpleCookie.setHttpOnly(true);
		// cookie记住登录信息时间，默认7天
		simpleCookie.setMaxAge(properties.getRememberMeTimeout() * 24 * 60 * 60);
		return simpleCookie;
	}

	/**
	 * 启用shrio授权注解拦截方式，AOP式方法级权限检查
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
			DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * 注入shiro bean的生命周期处理器。 可以自动调用配置在 Spring IOC容器中shiro bean的生命周期方法
	 * 
	 * @return
	 */
	@Bean(name="lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		LifecycleBeanPostProcessor lbpp = new LifecycleBeanPostProcessor();
		return lbpp;
	}

	/**
	 * 使IOC容器中的bean可以使用 shiro的注解. 
	 * @DependsOn注解可保证在注入此bean之前,会先注入指定的bean
	 * 
	 * @return
	 */
	@Bean
	@DependsOn({ "lifecycleBeanPostProcessor" })
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daapc = new DefaultAdvisorAutoProxyCreator();
		daapc.setProxyTargetClass(true);
		return daapc;
	}

	public static void main(String[] args) {
		try {
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			SecretKey deskey = keygen.generateKey();
			System.out.println(Base64.encodeToString(deskey.getEncoded()));
		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
