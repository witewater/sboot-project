package cn.wendong.admin.core.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wendong.admin.common.page.RestResult;
import cn.wendong.admin.common.page.RestResultGenerator;
import cn.wendong.admin.core.enums.ResultEnum;
import cn.wendong.admin.core.log.action.SystemAction;
import cn.wendong.admin.core.log.annotation.Logger;
import cn.wendong.admin.core.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;

/**
 * 异常处理
 * 
 * @author MB
 * @date 2018-12-02
 */
@ControllerAdvice
@Slf4j
public class SystemExceptionHandler {

	// /**
	// * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
	// */
	// @InitBinder
	// protected void initBinder(WebDataBinder binder) {
	// // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
	// binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
	// @Override
	// public void setAsText(String text) {
	// setValue(text == null ? null :
	// StringEscapeUtils.escapeHtml4(text.trim()));
	// }
	// @Override
	// public String getAsText() {
	// Object value = getValue();
	// return value != null ? value.toString() : "";
	// }
	// });
	// // Date 类型转换
	// binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
	// @Override
	// public void setAsText(String text) {
	// setValue(DateUtils.parseDate(text));
	// }
	// });
	// }

	// 拦截自定义异常
	@ExceptionHandler(ResponseException.class)
	@ResponseBody
	public RestResult<?> systemException(ResponseException e, HttpServletResponse response) {
		return RestResultGenerator.error(e.getCode(), e.getMessage());
	}

	// 拦截表单验证异常
	@ExceptionHandler(BindException.class)
	@ResponseBody
	public RestResult<?> bindException(BindException e, HttpServletResponse response) {
		BindingResult bindingResult = e.getBindingResult();
		return RestResultGenerator.error(bindingResult.getFieldError().getDefaultMessage());
	}

	// 拦截数据验证异常
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	public RestResult<?> validatorException(ConstraintViolationException e, HttpServletResponse response) {
		return RestResultGenerator.error(500, e.getConstraintViolations().iterator().next().getMessage());
	}

	// 拦截访问权限异常
	@ExceptionHandler(AuthorizationException.class)
	@ResponseBody
	public RestResult<?> authorizationException(AuthorizationException e, HttpServletRequest request,
			HttpServletResponse response) {

		Integer code = ResultEnum.NO_PERMISSIONS.getCode();
		String msg = ResultEnum.NO_PERMISSIONS.getMessage();
		// 获取异常信息
		Throwable cause = e.getCause();
		String message = cause.getMessage();
		Class<RestResult> restResultClass = RestResult.class;
		// 判断无权限访问的方法返回对象是否为RestResult
		if (!message.contains(restResultClass.getName())) {
			try {
				// 重定向到无权限页面
				String contextPath = request.getContextPath();
				ShiroFilterFactoryBean shiroFilter = SpringContextHolder.getBean(ShiroFilterFactoryBean.class);
				response.sendRedirect(contextPath + shiroFilter.getUnauthorizedUrl());
			} catch (IOException e1) {
				return RestResultGenerator.error(code, msg);
			}
		}
		// json数据返回
		return RestResultGenerator.error(code, msg);
	}

	// 拦截未知的运行时异常
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	@Logger(key = SystemAction.RUNTIME_EXCEPTION, action = SystemAction.class)
	public RestResult<?> runtimeException(RuntimeException e, HttpServletResponse response) {
		log.error("【系统异常】", e);
		return RestResultGenerator.error(500, "未知错误：ERROR");
	}
}
