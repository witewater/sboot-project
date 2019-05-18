package cn.wendong.tools.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * json工具类
 * 
 * @author MB yangtdo@qq.com
 * @date 2018-12-15
 */
public class JsonObjectUtils {

	public static <T> String object2Json(T value) {
		return JSONObject.toJSONString(value);
	}
	
	public static Object getObject4JsonString(String jsonString, Class<?> pojoCalss) {
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		Object pojo = JSONObject.toJavaObject(jsonObject, pojoCalss);
		return pojo;
	}

	public static Map<String, Object> getMap4Json(String jsonString) {
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		Iterator<String> keyIter = jsonObject.keySet().iterator();

		Map<String, Object> valueMap = new HashMap<String, Object>();

		while (keyIter.hasNext()) {
			String key = (String) keyIter.next();
			Object value = jsonObject.get(key);
			valueMap.put(key, value);
		}
		return valueMap;
	}

	public static Object[] getObjectArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.parseArray(jsonString);
		return jsonArray.toArray();
	}

	public static String getObjectToJsonObject(Object obj) {
		return JSON.toJSON(obj).toString();
	}

	public static String[] getStringArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.parseArray(jsonString);
		String[] stringArray = new String[jsonArray.size()];

		for (int i = 0; i < jsonArray.size(); ++i) {
			stringArray[i] = jsonArray.get(i).toString();
		}

		return stringArray;
	}

	public static <T> T getJsonToObject(String jsonString, Class<T> cls) {
		return JSONObject.parseObject(jsonString, cls);
	}

	public static <T> List<T> queryJsonToList(String jsonString, Class<T> cls) {
		List<T> list = JSONArray.parseArray(jsonString, cls);
		return list;
	}
}
