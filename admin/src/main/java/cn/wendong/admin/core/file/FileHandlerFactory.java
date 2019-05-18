package cn.wendong.admin.core.file;

import org.apache.commons.lang3.StringUtils;

import cn.wendong.admin.core.file.handler.FileHandler;
import cn.wendong.admin.core.file.handler.LocalFileHandler;

/**
 * 文件处理工厂类
 * @author MB yangtdo@qq.com
 * @date 2019-01-06
 */
public class FileHandlerFactory {

	private static FileHandler fileHandler = null;
	
	// 文件配置
	private static FileConfig fileConfig = new FileConfig();
	
	public static FileHandler getHandler() {
		if (null != fileHandler) {
			return fileHandler;
		}
		String fileStorage = fileConfig.getFileStorage();
		if (FileStorageEnums.LOCAL.getValue().equals(fileStorage)) {// 本地
			fileHandler = new LocalFileHandler(fileConfig.getLocalStorePath());
		} else if (FileStorageEnums.ALIYUN.getValue().equals(fileStorage)) {// 阿里云
			//fileHandler = new AliFileFileHandler(fileConfig.getAliyunEndpoint(), fileConfig.getAliyunAccessKeyId(),
			//		fileConfig.getAliyunAccessKeySecret(), fileConfig.getAliyunBucket());
		} else {
			throw new RuntimeException("请配置文件存储模块");
		}
		return fileHandler;
	}
	
	public static String getFullUrl(String relativePath) {
		if (StringUtils.isEmpty(relativePath)) {
			return null;
		}
		String fileStorage = fileConfig.getFileStorage();
		String urlPrefix = "";
		if (FileStorageEnums.LOCAL.getValue().equals(fileStorage)) {// 本地
			urlPrefix = fileConfig.getLocalUrlPrefix();
		} else if (FileStorageEnums.ALIYUN.getValue().equals(fileStorage)) {// 阿里云
			//urlPrefix = fileConfig.getAliyunUrlPrefix();
		} else {
			throw new RuntimeException("请配置文件存储模块");
		}
		return urlPrefix + relativePath;
	}
}
