package cn.wendong.tools.utils;

import java.io.File;
import org.apache.commons.io.FilenameUtils;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
public class FileUtils {

	/**
     * 获得唯一文件
     *
     * @param file
     * @return
     */
    public static File getUniqueFile(final File file) {
        if (!file.exists())
            return file;

        File tmpFile = new File(file.getAbsolutePath());
        File parentDir = tmpFile.getParentFile();
        int count = 1;
        String extension = FilenameUtils.getExtension(tmpFile.getName());
        String baseName = FilenameUtils.getBaseName(tmpFile.getName());
        do {
            tmpFile = new File(parentDir, baseName + "(" + count++ + ")."
                    + extension);
        } while (tmpFile.exists());
        return tmpFile;
    }
    
    /**
     * 获取名称后缀
     *
     * @param name
     * @return
     */
    public static String getExt(String name) {
        if (name == null || name.equals("") || !name.contains("."))
            return "";
        return name.substring(name.lastIndexOf("."));
    }

    /**
     * 创建目录
     *
     * @param path
     */
    public static void createFolder(String path) {
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
    }
}
