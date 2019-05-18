package cn.wendong.admin.core.file;

/**
 * 文件枚举类参数
 * 
 * @author MB yangtdo@qq.com
 * @date 2019-01-06
 */
public enum FileStorageEnums {

	/**
	 * 本地
	 */
	LOCAL("local"),
	/**
	 * 阿里云
	 */
	ALIYUN("aliyun");

	private String value;

	private FileStorageEnums(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
