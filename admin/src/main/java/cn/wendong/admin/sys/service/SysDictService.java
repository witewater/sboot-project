package cn.wendong.admin.sys.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import cn.wendong.admin.common.base.BaseSpecification;
import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.sys.entity.SysDict;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
public interface SysDictService {

    SysDict getName(String name);

    Page<SysDict> getPageList(Example<SysDict> example, Integer pageIndex, Integer pageSize);

    Page<SysDict> getPageList(BaseSpecification<SysDict> sysDict, Pageable pageable);
    
    SysDict getId(Long id);

    SysDict save(SysDict dict);

    @Transactional
    Integer updateStatus(StatusEnum statusEnum, List<Long> idList);
    
    List<SysDict> findByType(String type);
    
    //获取所有字典类型
    List<String> findTypes();
}
