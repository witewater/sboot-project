package cn.wendong.admin.common.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 控制层基础类
 * 
 * @author MB yangtdo@qq.com
 * @date 2018-12-06
 */
public abstract class BaseController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 管理基础路径
	 */
	@Value("${sboot.admin-path}")
	protected String adminPath;

	/**
	 * 定义默认主题
	 */
	public static String THEME = "default";

	/**
	 * 根据主题名称渲染页面
	 *
	 * @param pageName
	 * @return 返回拼接好的模板路径
	 */
	public String render(String pageName) {
		StringBuffer themeStr = new StringBuffer("templates/");
		themeStr.append(THEME);
		themeStr.append("/");
		return themeStr.append(pageName).toString();
	}

	/**
	 * 添加Model消息 废除 ，现都采用json数据返回
	 * 
	 * @param message
	 */
	@Deprecated
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		model.addAttribute("message", sb.toString());
	}

	/**
	 * 添加Flash消息 废除 ，现都采用json数据返回
	 * 
	 * @param message
	 */
	@Deprecated
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}

	/**
	 * 输出String数据字符串
	 *
	 * @param response
	 *            HttpServletResponse 对象
	 * @param dataStr
	 *            数据对象
	 */
	protected void outString(HttpServletResponse response, Object dataStr) {
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8".toString());
			PrintWriter out = response.getWriter();
			out.print(dataStr);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输出json数据字符串
	 *
	 * @param response
	 *            HttpServletResponse 对象
	 * @param jsonDataStr
	 *            json数据对象
	 */
	protected void outJson(HttpServletResponse response, Object jsonDataStr) {
		try {
			response.setContentType("application/json;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(jsonDataStr);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据字段获取请求参数中的字符串值
	 *
	 * @param request
	 *            HttpServletRequest对象
	 * @param fieldName
	 *            字段名称
	 * @return 返回请求到的字符串，获取不到返回null
	 */
	protected String getString(HttpServletRequest request, String fieldName) {
		if (StringUtils.isBlank(request.getParameter(fieldName))) {
			return null;
		}
		return request.getParameter(fieldName);
	}

	/**
	 * 获取请求的数据流
	 *
	 * @param request
	 *            HttpServletRequest对象
	 * @return 返回请求的数据流，返回JSON或XML字符串
	 */
	protected String readStreamParameter(HttpServletRequest request) {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer.toString();
	}

//	
//	/**
//	 * 参数绑定异常
//	 */
//	@ExceptionHandler({ BindException.class, ConstraintViolationException.class, ValidationException.class })
//	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "参数绑定错误")
//	public String bindException(Model model) {
//		Map<String, Object> responseData = new HashMap<>();
//		responseData.put("errCode", "400");
//		responseData.put("errMsg", "参数绑定异常");
//		model.addAttribute("msg", responseData);
//		return "error/400";
//	}
//
//	/**
//	 * 授权登录异常
//	 */
//	@ExceptionHandler({ AuthenticationException.class })
//	@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "不允许访问")
//	public String authenticationException(Model model) {
//		Map<String, Object> responseData = new HashMap<>();
//		responseData.put("errCode", "403");
//		responseData.put("errMsg", "不允许访问");
//		model.addAttribute("msg", responseData);
//		return "error/403";
//	}
//
//	/**
//	 * 未授权,权限不足
//	 * 
//	 * @throws IOException
//	 */
//	@ExceptionHandler(UnauthorizedException.class)
//	@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "权限不足")
//	public String exceptionHandler(Model model) {
//		Map<String, Object> responseData = new HashMap<>();
//		responseData.put("errCode", "401");
//		responseData.put("errMsg", "权限不足，请联系管理员");
//		model.addAttribute("msg", responseData);
//		return "error/noAuth";
//	}
//
//	// 自定义异常处理controller没有捕获的异常
//	@ExceptionHandler(Exception.class)
//	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "服务器内部错误")
//	public String handlerException(Model model, Exception ex) {
//		Map<String, Object> responseData = new HashMap<>();
//		responseData.put("errCode", "500");
//		responseData.put("errMsg", "系统出现异常，请联系管理员");
//		model.addAttribute("msg", responseData);
//		return "error/500";
//	}
}
