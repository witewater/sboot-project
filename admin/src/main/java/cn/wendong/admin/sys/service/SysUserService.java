package cn.wendong.admin.sys.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import cn.wendong.admin.common.base.BaseSpecification;
import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.sys.entity.SysUser;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
public interface SysUserService {

    Page<SysUser> getPageList(Example<SysUser> example, Integer pageIndex, Integer pageSize);

    Page<SysUser> getPageList(BaseSpecification<SysUser> sysUser,Pageable pageable);
    
    @Transactional
    SysUser save(SysUser user);

    @Transactional
    List<SysUser> save(List<SysUser> userList);

    SysUser getByName(String username, Byte... statusEnums);

    SysUser getId(Long id);

    List<SysUser> getIdList(List<Long> ids);

    @Transactional
    Integer updateStatus(StatusEnum statusEnum, List<Long> idList);
    
    SysUser findByUsername(String userName);
    
    @Transactional
    void updateUserLoginInfo(SysUser user);

	boolean validatePwd(String oldPassword, Long userId);
}
