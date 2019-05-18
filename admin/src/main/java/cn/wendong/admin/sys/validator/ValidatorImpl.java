package cn.wendong.admin.sys.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 验证使用Hibernate Validator
 * 使用@Valid：常见用在方法，类中字段上进行校验 ，可嵌套验证
 * 使用@Validated：是spring提供的对@Valid的封装，常见用在方法上进行校验 ，支持分组注解
 * @author MB yangtdo@qq.com
 * @date 2019-01-16
 */
@Component
@Slf4j
public class ValidatorImpl implements InitializingBean {

	private Validator validator;

	@Override
	public void afterPropertiesSet() throws Exception {
		//将validator通过工厂的初始化使其实例化
		log.info("ValidatorImpl已完成初始化");
		this.validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	//实现校验方法并返回校验结果
	public ValidationResult validate(Object bean){
		ValidationResult result = new ValidationResult();
		Set<ConstraintViolation<Object>> constraintViolationsSet = validator.validate(bean);
		if (!constraintViolationsSet.isEmpty()) {
			//有错误
			result.setHasErrors(true);
			constraintViolationsSet.forEach(constraintViolations ->{
				String propertyName = constraintViolations.getPropertyPath().toString();
				String errorMsg = constraintViolations.getMessage();
				result.getErrorMsgMap().put(propertyName,errorMsg);
			});
		}
		return result;
	}

}
