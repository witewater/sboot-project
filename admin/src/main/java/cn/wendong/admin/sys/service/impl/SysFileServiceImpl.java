package cn.wendong.admin.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wendong.admin.sys.entity.SysFile;
import cn.wendong.admin.sys.repository.SysFileRepository;
import cn.wendong.admin.sys.service.SysFileService;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
@Service
public class SysFileServiceImpl implements SysFileService {

    @Autowired
    private SysFileRepository sysFileRepository;

    /**
     * 获取文件sha1值的记录
     */
    @Override
    public SysFile isFile(String sha1) {
        return sysFileRepository.findBySha1(sha1);
    }

    /**
     * 保存文件上传
     * @param file 文件上传实体类
     */
    @Override
    @Transactional
    public SysFile save(SysFile file){
        return sysFileRepository.save(file);
    }
}

