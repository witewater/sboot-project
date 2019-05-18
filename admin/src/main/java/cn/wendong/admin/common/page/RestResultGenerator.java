package cn.wendong.admin.common.page;

import java.util.Map;

import cn.wendong.admin.common.enums.IErrorCode;
import cn.wendong.admin.common.enums.BaseReultEnum;
import cn.wendong.admin.core.utils.URLWrapper;

/**
 * 工厂模式代理生成RestResult
 * 
 * @author MB yangtdo@qq.com
 * @date 2018-12-06
 */
public class RestResultGenerator {

	public static <T> RestResult<T> genResult(boolean success, T data, String message) {
		RestResult<T> result = RestResult.newInstance();
		if (success) {
			result.setCode(BaseReultEnum.SUCCESS.getCode());
		} else {
			result.setCode(BaseReultEnum.ERROR.getCode());
		}
		result.setSuccess(success);
		result.setData(data);
		result.setMsg(message);
		return result;
	}

	/**
	 * 返回成功！
	 * 
	 * @return
	 */
	public static <T> RestResult<T> success() {
		return success(null);
	}

	/**
	 * 返回成功url
	 * 
	 * @param msg
	 * @param url
	 * @return
	 */
	public static <T> RestResult<String> success(String msg, String url) {
		return genResult(true, new URLWrapper(url).getPathUrl(), msg);
	}

	public static <T> RestResult<T> success(String msg) {
		return genResult(true, null, msg);
	}

	public static <T> RestResult<T> success(T data) {
		return genResult(true, data, null);
	}

	public static <T> RestResult<T> error(String message) {
		return genResult(false, null, message);
	}

	public static <T> RestResult<T> error(Integer code,String message){
		RestResult<T> result = RestResult.newInstance();
		result.setCode(code);
		result.setMsg(message);
		result.setData(null);
		return result;
	}
	
	/**
	 * 添加额外属性
	 * @param message
	 * @param attrs
	 * @return
	 */
	public static <T> RestResult<T> error(String message, Map<String, Object> attrs) {
		RestResult<T> rr = error(message);
		rr.setAttrs(attrs);
		return rr;
	}

	public static <T> RestResult<T> error(IErrorCode error) {
		return error(error.getMessageFormat());
	}
}
