package cn.wendong.admin.core.config;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * 验证实体配置
 * 
 * failFast：true 快速失败返回模式 false 普通模式
 * hibernate.validator.fail_fast：true 快速失败返回模式  false 普通模式
 * 
 * @author MB yangtdo@qq.com
 * @date 2018-12-09
 */
@Configuration
public class ValidatorConfiguration {

	@Bean
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure()
				.addProperty("hibernate.validator.fail_fast", "true").buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		return validator;
	}
	
	/**
	 * 验证@RequestParam对应的参数
	 * @return  org.springframework.validation.beanvalidation.MethodValidationPostProcessor
	 */
	@Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        /**设置validator模式为快速失败返回*/
        postProcessor.setValidator(validator());
        return postProcessor;
    }

}
