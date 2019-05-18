package cn.wendong.admin.sys.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import cn.wendong.admin.common.base.BaseSpecification;
import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.sys.entity.SysRole;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
public interface SysRoleService {
	
    Set<SysRole> getUserRoleList(Long id);

    SysRole getId(Long id);

    List<SysRole> getIdList(List<Long> ids);

    Page<SysRole> getPageList(Example<SysRole> example, Integer pageIndex, Integer pageSize);

    List<SysRole> getList(Sort sort);

    @Transactional
    SysRole save(SysRole role);

    @Transactional
    Integer updateStatus(StatusEnum statusEnum, List<Long> idList);

    Page<SysRole> getPageList(BaseSpecification<SysRole> sysRole,Pageable pageable);
}