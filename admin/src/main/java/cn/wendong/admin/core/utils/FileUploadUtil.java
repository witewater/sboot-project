package cn.wendong.admin.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import cn.wendong.admin.core.config.properties.ProjectProperties;
import cn.wendong.admin.core.enums.ResultEnum;
import cn.wendong.admin.core.exception.ResponseException;
import cn.wendong.admin.sys.entity.SysFile;
import cn.wendong.tools.utils.FileUtils;

/**
 * 文件上传处理工具
 * 
 * @author MB
 * @date 2018-12-02
 */
public class FileUploadUtil {

	/**
	 * 创建一个File实体对象
	 */
	public static SysFile getFile(MultipartFile multipartFile) {
		if (multipartFile.getSize() == 0) {
			throw new ResponseException(ResultEnum.NO_FILE_NULL);
		}
		SysFile file = new SysFile();
		file.setMime(multipartFile.getContentType());
		file.setSize(multipartFile.getSize());
		file.setName(FileUploadUtil.genFileName(multipartFile.getOriginalFilename()));
		file.setPath(FileUploadUtil.genDateMkdir("yyyyMMdd") + file.getName());
		return file;
	}

	/**
	 * 判断文件是否为支持的格式
	 * 
	 * @param multipartFile
	 *            MultipartFile对象
	 * @param types
	 *            支持的文件类型数组
	 */
	public static boolean isContentType(MultipartFile multipartFile, String[] types) {
		List<String> typeList = Arrays.asList(types);
		return typeList.contains(multipartFile.getContentType());
	}

	/**
	 * 获取文件上传保存路径
	 */
	public static String getUploadPath() {
		ProjectProperties properties = SpringContextHolder.getBean(ProjectProperties.class);
		return properties.getFileUploadPath();
	}

	/**
	 * 获取文件上传目录的静态资源路径
	 */
	public static String getPathPattern() {
		ProjectProperties properties = SpringContextHolder.getBean(ProjectProperties.class);
		return properties.getStaticPathPattern();
	}

	/**
	 * 生成随机且唯一的文件名
	 */
	public static String genFileName(String originalFilename) {
		String fileSuffix = FileUtils.getExt(originalFilename);
		return UUID.randomUUID().toString().replace("-", "") + fileSuffix;
	}

	/**
	 * 生成指定格式的目录名称(日期格式)
	 */
	public static String genDateMkdir(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return "/" + sdf.format(new Date()) + "/";
	}

	/**
	 * 保存文件及获取文件MD5值和SHA1值
	 * 
	 * @param multipartFile
	 *            MultipartFile对象
	 * @param file
	 *            File实体对象
	 */
	public static void transferTo(MultipartFile multipartFile, SysFile file)
			throws IOException, NoSuchAlgorithmException {
		// 创建保存文件对象
		String filePath = getUploadPath() + file.getPath();
		java.io.File dest = new java.io.File(filePath);
		if (!dest.exists()) {
			dest.getParentFile().mkdirs();
			dest.createNewFile();
		}

		byte[] buffer = new byte[4096];
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		try (OutputStream fos = Files.newOutputStream(dest.toPath());
				InputStream fis = multipartFile.getInputStream()) {
			int len = 0;
			while ((len = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				md5.update(buffer, 0, len);
				sha1.update(buffer, 0, len);
			}
			fos.flush();
		}
		BigInteger MD5Bi = new BigInteger(1, md5.digest());
		BigInteger SHA1Bi = new BigInteger(1, sha1.digest());
		file.setMd5(MD5Bi.toString(16));
		file.setSha1(SHA1Bi.toString(16));
		// 重新设置File对象的路径
		file.setPath(getPathPattern().replace("/**", "") + file.getPath());
	}

	/**
	 * 获取文件的SHA1值
	 */
	public static String getFileSHA1(MultipartFile multipartFile) {
		if (multipartFile.getSize() == 0) {
			throw new ResponseException(ResultEnum.NO_FILE_NULL);
		}
		byte[] buffer = new byte[4096];
		try (InputStream fis = multipartFile.getInputStream()) {
			MessageDigest sha1 = MessageDigest.getInstance("SHA1");
			int len = 0;
			while ((len = fis.read(buffer)) != -1) {
				sha1.update(buffer, 0, len);
			}
			BigInteger SHA1Bi = new BigInteger(1, sha1.digest());
			return SHA1Bi.toString(16);
		} catch (IOException | NoSuchAlgorithmException e) {
			return null;
		}
	}

}
