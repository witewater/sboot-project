package cn.wendong.admin.core.file.beans;

import lombok.Data;

@Data
public class FileInfo {

	/**
	 * 文件全路劲
	 */
	private String fullUrl;
	/**
	 * 相对路径
	 */
	private String relativePath;
	/**
	 * 原始文件名
	 */
	private String originalFilename;
}
