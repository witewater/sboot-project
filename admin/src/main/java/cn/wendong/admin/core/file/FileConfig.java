package cn.wendong.admin.core.file;

import org.springframework.beans.factory.annotation.Value;

public class FileConfig {
	/**
	 * 存储介质 local= 本地，aliyun=阿里云
	 */
	@Value("${file.storage}")
	private String fileStorage;
	/**
	 * 本地公网访问前缀
	 */
	@Value("${file.local.urlPrefix}")
	private String localUrlPrefix;
	/**
	 * 本地存储路径
	 */
	@Value("${file.local.storePath}")
	private String localStorePath;

	/**
	 * 阿里云公网访问前缀
	 */
	@Value("${file.aliyun.urlPrefix}")
	private String aliyunUrlPrefix;
	/**
	 * 阿里云bucket 即存储路径
	 */
	@Value("${file.aliyun.bucket}")
	private String aliyunBucket;

	@Value("${file.aliyun.endpoint}")
	private String aliyunEndpoint;
	@Value("${file.aliyun.accessKeyId}")
	private String aliyunAccessKeyId;
	@Value("${file.aliyun.accessKeySecret}")
	private String aliyunAccessKeySecret;

	/**
	 * 获取地址
	 * 
	 * @param relativePath
	 * @return
	 */
	public static String getFullUrl(String relativePath) {
		String fullUrl = FileHandlerFactory.getFullUrl(relativePath);
		return fullUrl;
	}

	public String getFileStorage() {
		return fileStorage;
	}

	public void setFileStorage(String fileStorage) {
		this.fileStorage = fileStorage;
	}

	public String getLocalUrlPrefix() {
		return localUrlPrefix;
	}

	public void setLocalUrlPrefix(String localUrlPrefix) {
		this.localUrlPrefix = localUrlPrefix;
	}

	public String getLocalStorePath() {
		return localStorePath;
	}

	public void setLocalStorePath(String localStorePath) {
		this.localStorePath = localStorePath;
	}

	public String getAliyunUrlPrefix() {
		return aliyunUrlPrefix;
	}

	public void setAliyunUrlPrefix(String aliyunUrlPrefix) {
		this.aliyunUrlPrefix = aliyunUrlPrefix;
	}

	public String getAliyunBucket() {
		return aliyunBucket;
	}

	public void setAliyunBucket(String aliyunBucket) {
		this.aliyunBucket = aliyunBucket;
	}

	public String getAliyunEndpoint() {
		return aliyunEndpoint;
	}

	public void setAliyunEndpoint(String aliyunEndpoint) {
		this.aliyunEndpoint = aliyunEndpoint;
	}

	public String getAliyunAccessKeyId() {
		return aliyunAccessKeyId;
	}

	public void setAliyunAccessKeyId(String aliyunAccessKeyId) {
		this.aliyunAccessKeyId = aliyunAccessKeyId;
	}

	public String getAliyunAccessKeySecret() {
		return aliyunAccessKeySecret;
	}

	public void setAliyunAccessKeySecret(String aliyunAccessKeySecret) {
		this.aliyunAccessKeySecret = aliyunAccessKeySecret;
	}

}
