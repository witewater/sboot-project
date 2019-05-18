package cn.wendong.admin.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import cn.wendong.admin.common.base.BaseRepository;
import cn.wendong.admin.sys.entity.SysUser;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
public interface SysUserRepository extends BaseRepository<SysUser, Long>{
    /**
     * 根据用户名查询用户数据
     *
     * @param username 用户名
     * @return 用户数据
     */
     SysUser findByUsernameAndStatusIn(String username, Byte[] status);

    /**
     * 查找ID列表且状态正常
     *
     * @param ids     ID列表
     * @param status 状态
     */
     List<SysUser> findByIdInAndStatus(List<Long> ids, Byte status);
    
    /**
     * 通过登录名获取用户
     * @param useName
     * @return
     */
    SysUser findByUsername(String useName);
    
    /**
     * 更新登录时间和登录IP
     * @param user
     * @return
     */
    @Modifying
    @Transactional
    @Query(value="UPDATE SysUser u SET u.loginTime= :#{#user.loginTime}, u.loginIp = :#{#user.loginIp} WHERE u.id= :#{#user.id}")
    int updateUserLoginInfo(@Param("user") SysUser user);

}