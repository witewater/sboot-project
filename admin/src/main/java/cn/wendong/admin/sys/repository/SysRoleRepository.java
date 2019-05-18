package cn.wendong.admin.sys.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;

import cn.wendong.admin.common.base.BaseRepository;
import cn.wendong.admin.sys.entity.SysRole;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
public interface SysRoleRepository extends BaseRepository<SysRole,Long> {

    /**
     * 查找状态正常的角色
     * @param sort 排序对象
     */
    public List<SysRole> findAllByStatus(Sort sort, Byte status);

    /**
     * 查找多个角色
     * @param ids id列表
     */
    public List<SysRole> findByIdInAndStatus(List<Long> ids, Byte status);

    /**
     * 查询指定用户的角色列表
     * @param id 用户ID
     * @param status 角色状态
     */
    public Set<SysRole> findByUsers_IdAndStatus(Long id, Byte status);
}
