package cn.wendong.admin.sys.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import cn.wendong.admin.common.base.BaseSpecification;
import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.sys.entity.SysMenu;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
public interface SysMenuService {

    List<SysMenu> getIdList(List<Long> ids);

    List<SysMenu> getList(Example<SysMenu> example, Sort sort);

    SysMenu getId(Long id);

    SysMenu getHref(String href);

    List<SysMenu> getList(Sort sort);

    Integer getSortMax(Long pid);

    List<SysMenu> getPid(Long pid, Long notId);

    @Transactional
    SysMenu save(SysMenu menu);

    @Transactional
    List<SysMenu> save(List<SysMenu> menuList);

    @Transactional
    Integer updateStatus(StatusEnum statusEnum, List<Long> idList);
    
    List<SysMenu> findShowMenuAll();
    
    Page<SysMenu> getPageList(BaseSpecification<SysMenu> sysMenu,Pageable pageable);
}
