package cn.wendong.admin.sys.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wendong.admin.common.base.BaseSpecification;
import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.sys.entity.SysMenu;
import cn.wendong.admin.sys.repository.SysMenuRepository;
import cn.wendong.admin.sys.service.SysMenuService;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuRepository sysMenuRepository;

    /**
     * 根据菜单ID查询菜单数据
     * @param id 菜单ID
     */
    @Override
    public SysMenu getId(Long id) {
        Byte[] status = {StatusEnum.NORMAL.getCode(), StatusEnum.FREEZED.getCode()};
        return sysMenuRepository.findByIdAndStatusIn(id, status);
    }

    /**
     * 根据菜单url查询菜单数据
     * @param url 菜单url
     */
    @Override
    public SysMenu getHref(String href){
        Byte[] StatusBytes = {StatusEnum.NORMAL.getCode(), StatusEnum.FREEZED.getCode()};
        return sysMenuRepository.findByUrlAndStatusIn(href, StatusBytes);
    }

    /**
     * 根据菜单ID列表查询多个菜单数据
     * @param ids 菜单ID列表
     */
    @Override
    public List<SysMenu> getIdList(List<Long> ids) {
        return sysMenuRepository.findByIdInAndStatus(ids, StatusEnum.NORMAL.getCode());
    }

    /**
     * 获取菜单列表数据
     * @param example 查询实例
     * @param sort 排序对象
     */
    @Override
    public List<SysMenu> getList(Example<SysMenu> example, Sort sort) {
        return sysMenuRepository.findAll(example, sort);
    }

    /**
     * 获取菜单列表数据
     * @param sort 排序对象
     */
    @Override
    public List<SysMenu> getList(Sort sort) {
        return sysMenuRepository.findAllByStatus(sort, StatusEnum.NORMAL.getCode());
    }

    /**
     * 获取排序最大值
     * @param pid 父菜单ID
     */
    @Override
    public Integer getSortMax(Long pid){
        return sysMenuRepository.findSortMax(pid);
    }

    /**
     * 根据父级菜单ID获取本级全部菜单
     * @param pid 父菜单ID
     * @param notId 需要排除的菜单ID
     */
    @Override
    public List<SysMenu> getPid(Long pid, Long notId){
        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        Byte[] bytes = {StatusEnum.NORMAL.getCode(), StatusEnum.FREEZED.getCode()};
        return sysMenuRepository.findByPidAndIdNotAndStatusIn(sort, pid, notId, bytes);
    }

    /**
     * 保存菜单
     * @param menu 菜单实体类
     */
    @Override
    @Transactional
    public SysMenu save(SysMenu menu){
        return sysMenuRepository.save(menu);
    }

    /**
     * 保存多个菜单
     * @param menuList 菜单实体类列表
     */
    @Override
    @Transactional
    public List<SysMenu> save(List<SysMenu> menuList){
        return sysMenuRepository.saveAll(menuList);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Integer updateStatus(StatusEnum statusEnum, List<Long> idList){

        // 获取与之关联的所有菜单
        Set<SysMenu> treeMenuList = new HashSet<>();
        idList.forEach(id -> {
            Optional<SysMenu> menu = sysMenuRepository.findById(id);
            List<SysMenu> list = sysMenuRepository.findByPidsLikeAndStatus("%["+id+"]%", menu.get().getStatus());
            treeMenuList.add(menu.get());
            treeMenuList.addAll(list);
        });

        treeMenuList.forEach(menu -> {
            // 删除菜单状态时，同时更新角色的权限
            if(statusEnum == StatusEnum.DELETE){
                menu.getRoles().forEach(role -> {
                    role.getMenus().remove(menu);
                });
            }
            // 更新关联的所有菜单状态
            menu.setStatus(statusEnum.getCode());
        });

        return treeMenuList.size();
    }

	@Override
	public List<SysMenu> findShowMenuAll() {
		return sysMenuRepository.findAllByStatus(new Sort(Sort.Direction.ASC, "sort"), StatusEnum.NORMAL.getCode());
	}

	@Override
	public Page<SysMenu> getPageList(BaseSpecification<SysMenu> sysMenu, Pageable pageable) {
		Page<SysMenu> page = sysMenuRepository.findAll(sysMenu, pageable);
		return page;
	}

}
