package cn.wendong.admin.sys.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import cn.wendong.admin.common.base.BaseRepository;
import cn.wendong.admin.sys.entity.SysMenu;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
public interface SysMenuRepository extends BaseRepository<SysMenu, Long> {

    /**
     * 查找状态正常的菜单
     * @param sort 排序对象
     */
    public List<SysMenu> findAllByStatus(Sort sort, Byte status);

    /**
     * 查找多个菜单
     * @param ids id列表
     */
    public List<SysMenu> findByIdInAndStatus(List<Long> ids, Byte status);

    /**
     * 查询菜单URL
     * @param url id列表
     */
    public SysMenu findByUrlAndStatusIn(String url, Byte[] status);

    /**
     * 根据父ID查找子菜单
     * @param pids pid列表
     */
    public List<SysMenu> findByPidsLikeAndStatus(String pids, Byte status);

    /**
     * 获取排序最大值
     * @param pid 父菜单ID
     */
    @Query("select max(sort) from SysMenu m where m.pid = ?1")
    public Integer findSortMax(long pid);

    /**
     * 根据父级菜单ID获取本级全部菜单
     * @param sort 排序对象
     * @param pid 父菜单ID
     * @param notId 需要排除的菜单ID
     */
    public List<SysMenu> findByPidAndIdNotAndStatusIn(Sort sort, long pid, long notId, Byte[] bytes);
}
