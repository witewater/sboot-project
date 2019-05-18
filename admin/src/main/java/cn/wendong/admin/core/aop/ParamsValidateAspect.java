package cn.wendong.admin.core.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import cn.wendong.admin.core.exception.ResponseException;

/**
 * 参数校验切面
 * @author MB yangtdo@qq.com
 * @date 2019-01-20
 */
@Component
@Aspect
public class ParamsValidateAspect {

	/**
	 * 表达式意思：所有cn.wendong.admin.common.base.BaseController的子类型的任何方法（任何参数类型）
	 *
	 * execution(<修饰符模式>?<返回类型模式><方法名模式>(<参数模式>)<异常模式>?)  除了返回类型模式、方法名模式和参数模式外，其它项都是可选的。
	 * execution()	表达式的主体
	 *	第一个“*”符号	表示返回值的类型任意
	 *  + 匹配指定类型的子类型；仅能作为后缀放在类型模式后边。
	 *	cn.wendong.admin.common.base   AOP所切的服务的包名，即，需要进行横切的业务类
	 *  cn.wendong.admin.common.base..* 表示在类名模式串中，“.*”表示包下的所有类，而“..*”表示包、子孙包下的所有类
	 *	包名后面的“..”	表示当前包及子包
	 *	第二个“*”	表示类名，*即所有类
	 *	.*(..)	表示任何方法名，括号表示参数，两个点表示任何参数类型
	 * 
	 */
	@Pointcut("execution(* cn.wendong.admin.common.base.BaseController+.*(..))")
	private void anyMethod() {
	}

	@Around("anyMethod() && args(..)")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Object[] args = point.getArgs();
		if (null != args && args.length > 0) {
			for (Object arg : args) {
				if (arg instanceof BindingResult) {
					BindingResult br = (BindingResult) arg;
					if (br.hasErrors()) {
						FieldError fieldError = br.getFieldErrors().get(0);
						throw new ResponseException(500, "参数校验错误",
								new Object[] { fieldError.getField(), fieldError.getDefaultMessage() });
					}
				}
			}
		}
		return point.proceed();
	}
}
