package cn.wendong.admin.core.file.handler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件处理类
 * @author MB yangtdo@qq.com
 * @date 2019-01-06
 */
public interface FileHandler {

	/**
	 * 上传
	 * @param storePath
	 * @param relativePath
	 * @param in
	 * @throws IOException
	 */
	public void upload(String relativePath,InputStream in) throws IOException;
	/**
	 * 下载
	 * @param storePath
	 * @param relativePath
	 * @return
	 */
	public InputStream download(String relativePath) throws FileNotFoundException;
	
	/**
	 * 
	 */
	public void destroy();
}
