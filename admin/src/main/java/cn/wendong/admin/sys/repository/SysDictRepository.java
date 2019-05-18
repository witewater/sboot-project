package cn.wendong.admin.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import cn.wendong.admin.common.base.BaseRepository;
import cn.wendong.admin.sys.entity.SysDict;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
public interface SysDictRepository extends BaseRepository<SysDict, Long> {

    /**
     * 根据字典标识查询
     * @param name 字典标识
     * @param status 状态
     */
    public SysDict findByNameAndStatus(String name, Byte status);
    
    public List<SysDict> findByType(String type);
    
    /**
     * select distinct type from sys_dict;
     * @return
     */
    @Query(value = "select distinct type from sys_dict",nativeQuery = true)
    public List<String> findDistinctByType();
}
