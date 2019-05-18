package cn.wendong.admin.sys.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wendong.admin.common.base.BaseSpecification;
import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.sys.entity.SysRole;
import cn.wendong.admin.sys.repository.SysRoleRepository;
import cn.wendong.admin.sys.service.SysRoleService;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleRepository sysRoleRepository;

    /**
     * 获取用户角色列表
     * @param id 用户ID
     */
    @Override
    @Transactional
    public Set<SysRole> getUserRoleList(Long id) {
        Byte status = StatusEnum.NORMAL.getCode();
        return sysRoleRepository.findByUsers_IdAndStatus(id, status);
    }

    /**
     * 根据角色ID查询角色数据
     * @param id 角色ID
     */
    @Override
    @Transactional
    public SysRole getId(Long id) {
        Byte[] status = {StatusEnum.NORMAL.getCode(), StatusEnum.FREEZED.getCode()};
        return sysRoleRepository.findByIdAndStatusIn(id, status);
    }

    /**
     * 根据角色ID列表查询多个角色数据
     * @param ids 角色ID列表
     */
    @Override
    @Transactional
    public List<SysRole> getIdList(List<Long> ids) {
        return sysRoleRepository.findByIdInAndStatus(ids, StatusEnum.NORMAL.getCode());
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @param pageIndex 页码
     * @param pageSize 获取列表长度
     * @return 返回分页数据
     */
    @Override
    public Page<SysRole> getPageList(Example<SysRole> example, Integer pageIndex, Integer pageSize) {
        // 创建分页对象
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        PageRequest page = PageRequest.of(pageIndex-1, pageSize, sort);
        Page<SysRole> list = sysRoleRepository.findAll(example, page);
        return list;
    }

    /**
     * 获取角色列表数据
     * @param sort 排序对象
     */
    @Override
    public List<SysRole> getList(Sort sort) {
        return sysRoleRepository.findAllByStatus(sort, StatusEnum.NORMAL.getCode());
    }


    /**
     * 保存角色
     * @param role 角色实体类
     */
    @Override
    public SysRole save(SysRole role){
        return sysRoleRepository.save(role);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Integer updateStatus(StatusEnum statusEnum, List<Long> idList){
        // 删除角色时取消与角色和菜单的关联
        if(statusEnum == StatusEnum.DELETE){
            idList.forEach(id -> {
                Optional<SysRole> optional = sysRoleRepository.findById(id);
                optional.ifPresent(role -> {
                    role.setMenus(null);
                    role.getUsers().forEach(user -> user.getRoles().remove(role));
                });
            });
        }
        return sysRoleRepository.updateStatus(statusEnum.getCode(),idList);
    }

	@Override
	public Page<SysRole> getPageList(BaseSpecification<SysRole> sysRole, Pageable pageable) {
		Page<SysRole> page = sysRoleRepository.findAll(sysRole, pageable);
		return page;
	}
    
    
}
