package cn.wendong.admin.core.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.support.OpenSessionInViewInterceptor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * jpa配置
 * @author MB yangtdo@qq.com
 * @date 2019-03-16
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(basePackages = "cn.wendong.admin.sys.repository")
@EntityScan(basePackages = "cn.wendong.admin.sys.entity")
public class JpaConfiguration {
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public OpenSessionInViewInterceptor openSessionInViewInterceptor() {
		return new OpenSessionInViewInterceptor();
	}
}
