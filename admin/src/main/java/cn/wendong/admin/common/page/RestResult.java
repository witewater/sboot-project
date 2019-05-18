package cn.wendong.admin.common.page;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 前后端数据交互格式
 * 
 * @author MB yangtdo@qq.com
 * @date 2018-12-06
 */
@Data
public class RestResult<T> {

	private Integer code;// 状态码
	private String msg;// 返回消息
	private T data;
	private boolean success;// 结果，是否成功
	@JSONField(serialize = false)
	private Map<String, Object> attrs;// 其他参数

	private RestResult() {
	}

	public static <T> RestResult<T> newInstance() {
		return new RestResult<T>();
	}
	
	public Map<String, Object> addProperty(String key, Object value) {
		if (attrs == null) {
			attrs = new HashMap<>(1);
		}
		if (null == value) {
			return attrs;
		}
		attrs.put(key, value);
		return attrs;
	}
}
