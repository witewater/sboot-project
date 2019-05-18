package cn.wendong.admin.core.file.handler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.util.Assert;

/**
 *  阿里云文件处理
 * @author MB yangtdo@qq.com
 * @date 2019-01-06
 */
public class AliFileFileHandler implements FileHandler {

	//private OSSClient ossClient;
	private String bucketName;
	
	/**
	 * 
	 * @param endpoint
	 * @param accessKeyId
	 * @param accessKeySecret
	 */
	public AliFileFileHandler(String endpoint, String accessKeyId, String accessKeySecret,String bucketName) {
		//ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		this.bucketName = bucketName;
	}

	/**
	 * storePath 在阿里云oss 中即为 BucketName
	 */
	@Override
	public void upload(String relativePath, InputStream in) throws IOException {
		//ossClient.putObject(bucketName, handleRelativePath(relativePath), in);
		if (null != in) {
			in.close();
		}
	}

	@Override
	public InputStream download(String relativePath) throws FileNotFoundException {
		//OSSObject object = ossClient.getObject(bucketName, handleRelativePath(relativePath));
		//return object.getObjectContent();
		return null;
	}

	/**
	 * 阿里云路径不能以/ 开始
	 * @param relativePath
	 * @return
	 */
	private String handleRelativePath(String relativePath) {
		Assert.hasLength(relativePath,"路径为空");
		if (relativePath.startsWith("/")) {
			relativePath = relativePath.substring(1);
		}
		return relativePath;
	}
	@Override
	public void destroy() {
//		if (null != ossClient) {
//			ossClient.shutdown();
//		}
	}
}
