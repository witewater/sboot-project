package cn.wendong.admin.core.log.annotation;

import java.lang.reflect.Method;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import cn.wendong.admin.core.log.action.base.CommActionMap;
import cn.wendong.admin.core.log.action.base.ResetLog;
import cn.wendong.admin.core.log.action.model.BaseUserActionModel;
import cn.wendong.admin.core.log.action.model.BusinessMethod;
import cn.wendong.admin.core.log.action.model.BusinessType;
import cn.wendong.admin.core.shiro.ShiroAuthManager;
import cn.wendong.admin.core.utils.SpringContextHolder;
import cn.wendong.admin.sys.entity.SysOptLog;
import cn.wendong.admin.sys.service.SysOptLogService;
import cn.wendong.tools.utils.IPUtils;
import cn.wendong.tools.utils.JsonObjectUtils;
import cn.wendong.tools.utils.UserAgentUtils;
import lombok.extern.slf4j.Slf4j;

@Aspect//声明一个切面
@Component//让spring管理
@Slf4j
public class LoggerAspect {
	
	private final static String defaultActionName = "default";

	private long currentTime = 0L;//当前执行时间
	
	/**
     * 配置切入点
     */
	@Pointcut("@annotation(cn.wendong.admin.core.log.annotation.Logger)")
	public void logPointcut() {
		// 该方法无方法体,主要为了让同类中其他方法使用此切入点
	}

	 /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
	@Around("logPointcut()")
	public Object logAround(ProceedingJoinPoint point) throws Throwable {
		// 先执行切入点，获取返回值
		Object proceed = point.proceed();

		/* 读取Logger注解消息 */
		Method targetMethod = ((MethodSignature) (point.getSignature())).getMethod();
		Logger anno = targetMethod.getAnnotation(Logger.class);
		if(anno != null){//如果存在注解
			
		}
		// 获取name值
		String name = anno.name();
		// 获取message值
		String message = anno.message();
		// 获取key值
		String key = anno.key();
		// 获取行为模型
		Class<? extends CommActionMap> action = anno.action();
		CommActionMap instance = action.newInstance();
		Object actionModel = instance.get(!key.isEmpty() ? key : defaultActionName);
		Assert.notNull(actionModel, "无法获取日志的行为方法，请检查：" + point.getSignature());

		// 封装日志实例对象
		SysOptLog actionLog = new SysOptLog();
		actionLog.setClazz(point.getTarget().getClass().getName());
		actionLog.setMethod("*." + targetMethod.getName() + "()");
		//请求的参数,只保存第一个参数
		Object[] args = point.getArgs();
		try{
			String params = JsonObjectUtils.object2Json(args[0]);
			actionLog.setParams(params);
		}catch (Exception e){
			e.printStackTrace();
		}
		actionLog.setType(((BaseUserActionModel) actionModel).getType());
		actionLog.setName(!name.isEmpty() ? name : ((BaseUserActionModel) actionModel).getName());
		actionLog.setMessage(message);
		actionLog.setExecuteTime(System.currentTimeMillis() - currentTime);
		actionLog.setOs(UserAgentUtils.getClientOS());
		actionLog.setBrowser(UserAgentUtils.getBrowserInfo());
		actionLog.setOpip(IPUtils.getIpAddr());
		if(ShiroAuthManager.getSysUser() != null){//没有登录时用户为空
			actionLog.setOpusername(ShiroAuthManager.getSysUser().getUsername());
		}
		actionLog.setOptime(new Date());
		// 判断是否为普通实例对象
		if (actionModel instanceof BusinessType) {
			actionLog.setMessage(((BusinessType) actionModel).getMessage());
		} else {
			// 重置日志-自定义日志数据
			ResetLog resetLog = new ResetLog();
			resetLog.setSysOptLog(actionLog);
			resetLog.setRetValue(proceed);
			resetLog.setJoinPoint(point);
			try {
				Method method = action.getDeclaredMethod(((BusinessMethod) actionModel).getMethod(), ResetLog.class);
				method.invoke(instance, resetLog);
				if (!resetLog.getRecord())
					return proceed;
			} catch (NoSuchMethodException e) {
				log.error("获取行为对象方法错误！请检查方法名称是否正确！", e);
				e.printStackTrace();
			}
		}

		// 保存日志
		SysOptLogService actionLogService = (SysOptLogService) SpringContextHolder.getBean(SysOptLogService.class);
		actionLogService.save(actionLog);

		return proceed;
	}
	
	
	/**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
    	SysOptLog sysOptLog = new SysOptLog();
    	sysOptLog.setExceptionDetail(e.getMessage());
    	sysOptLog.setExecuteTime(System.currentTimeMillis() - currentTime);
    	SysOptLogService sysOptLogService = (SysOptLogService) SpringContextHolder.getBean(SysOptLogService.class);
    	sysOptLogService.save(sysOptLog);
    }
}
