package cn.wendong.admin.core.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * 应用工具
 * 
 * @author MB
 * @date 2018-12-02
 */
public class AppUtils {

	/**
	 * 获取项目不同模式下的根路径
	 */
	public static String getProjectPath() {
		String filePath = AppUtils.class.getResource("").getPath();
		String projectPath = AppUtils.class.getClassLoader().getResource("").getPath();
		StringBuilder path = new StringBuilder();

		if (!filePath.startsWith("file:/")) {
			// 开发模式下根路径
			char[] filePathArray = filePath.toCharArray();
			char[] projectPathArray = projectPath.toCharArray();
			for (int i = 0; i < filePathArray.length; i++) {
				if (projectPathArray.length > i && filePathArray[i] == projectPathArray[i]) {
					path.append(filePathArray[i]);
				} else {
					break;
				}
			}
		} else if (!projectPath.startsWith("file:/")) {
			// 部署服务器模式下根路径
			projectPath = projectPath.replace("/WEB-INF/classes/", "");
			projectPath = projectPath.replace("/target/classes/", "");
			try {
				path.append(URLDecoder.decode(projectPath, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				return projectPath;
			}
		} else {
			// jar包启动模式下根路径
			String property = System.getProperty("java.class.path");
			int firstIndex = property.lastIndexOf(System.getProperty("path.separator")) + 1;
			int lastIndex = property.lastIndexOf(File.separator) + 1;
			path.append(property, firstIndex, lastIndex);
			System.out.println(path);
		}

		File file = new File(path.toString());
		return file.getAbsolutePath().replaceAll("\\\\", "/");
	}

	/**
	 * 将枚举转成List集合
	 * 
	 * @param enumClass
	 *            枚举类
	 */
	public static Map<Long, String> enumToMap(Class<?> enumClass) {
		Map<Long, String> map = new TreeMap<>();
		try {
			Object[] objects = enumClass.getEnumConstants();
			Method getCode = enumClass.getMethod("getCode");
			Method getMessage = enumClass.getMethod("getMessage");
			for (Object obj : objects) {
				Object iCode = getCode.invoke(obj);
				Object iMessage = getMessage.invoke(obj);
				map.put(Long.valueOf(String.valueOf(iCode)), String.valueOf(iMessage));
			}
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 根据枚举code获取枚举对象
	 * 
	 * @param enumClass
	 *            枚举类
	 * @param code
	 *            code值
	 */
	public static Object enumCode(Class<?> enumClass, Object code) {
		try {
			Object[] objects = enumClass.getEnumConstants();
			Method getCode = enumClass.getMethod("getCode");
			for (Object obj : objects) {
				Object iCode = getCode.invoke(obj);
				if (iCode.equals(code)) {
					return obj;
				}
			}
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return "";
	}

}
