package cn.wendong.admin.sys.service;

import cn.wendong.admin.sys.entity.SysFile;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
public interface SysFileService {
	
    SysFile isFile(String sha1);

    SysFile save(SysFile file);
}

