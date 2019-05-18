package cn.wendong.admin.sys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.wendong.admin.sys.entity.SysFile;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
public interface SysFileRepository extends JpaRepository<SysFile, Long> {

    /**
     * 查找指定文件sha1记录
     * @param sha1 文件sha1值
     */
    public SysFile findBySha1(String sha1);
}

