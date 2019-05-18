package cn.wendong.admin.sys.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wendong.admin.common.base.BaseService;
import cn.wendong.admin.common.base.BaseSpecification;
import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.core.shiro.ShiroAuthManager;
import cn.wendong.admin.sys.entity.SysUser;
import cn.wendong.admin.sys.repository.SysUserRepository;
import cn.wendong.admin.sys.service.SysUserService;
import cn.wendong.tools.utils.IPUtils;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
@Service
public class SysUserServiceImpl extends BaseService<SysUser, Long> implements SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    /**
     * 根据用户名查询用户数据
     * @param username 用户名
     * @return 用户数据
     */
    @Override
    public SysUser getByName(String username, Byte... status) {
        Byte[] newStatus = new Byte[status.length + 1];
        newStatus[0] = StatusEnum.NORMAL.getCode();
        System.arraycopy(status, 0, newStatus, 1, status.length);
        return sysUserRepository.findByUsernameAndStatusIn(username, newStatus);
    }

    /**
     * 根据用户ID查询用户数据
     * @param id 用户ID
     */
    @Override
    public SysUser getId(Long id) {
        Byte[] status = {StatusEnum.NORMAL.getCode(), StatusEnum.FREEZED.getCode()};
        return sysUserRepository.findByIdAndStatusIn(id, status);
    }

    /**
     * 根据用户ID列表查询多个用户数据
     * @param ids 用户ID列表
     */
    @Override
    public List<SysUser> getIdList(List<Long> ids) {
        return sysUserRepository.findByIdInAndStatus(ids, StatusEnum.NORMAL.getCode());
    }


    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @param pageIndex 页码
     * @param pageSize 获取列表长度
     * @return 返回分页数据
     */
    @Override
    public Page<SysUser> getPageList(Example<SysUser> example, Integer pageIndex, Integer pageSize) {
        // 创建分页对象
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        PageRequest page = PageRequest.of(pageIndex-1, pageSize, sort);
        return sysUserRepository.findAll(example, page);
    }

   
    @Override
	public Page<SysUser> getPageList(BaseSpecification<SysUser> sysUser, Pageable pageable) {
    	Page<SysUser> page = sysUserRepository.findAll(sysUser, pageable);
		return page;
	}

	/**
     * 保存用户
     * @param user 用户实体类
     */
    @Override
    @Transactional
    public SysUser save(SysUser user){
        return sysUserRepository.save(user);
    }

    /**
     * 保存用户列表
     * @param userList 用户实体类
     */
    @Override
    @Transactional
    public List<SysUser> save(List<SysUser> userList){
        return sysUserRepository.saveAll(userList);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Integer updateStatus(StatusEnum statusEnum, List<Long> idList){
        // 删除用户时取消与角色的关联
        if(statusEnum == StatusEnum.DELETE){
            idList.forEach(id -> {
                Optional<SysUser> optional = sysUserRepository.findById(id);
                optional.ifPresent(user -> user.setRoles(null));
            });
        }
        return sysUserRepository.updateStatus(statusEnum.getCode(),idList);
    }

	@Override
	public SysUser findByUsername(String userName) {
		return sysUserRepository.findByUsername(userName);
	}

	@Override
	@Transactional
	public void updateUserLoginInfo(SysUser user) {
		// 保存上次登录信息
		user.setOldLoginIp(user.getLoginIp());
		user.setOldLoginDate(user.getLoginTime());
		// 更新本次登录信息
		user.setLoginIp(IPUtils.getIpAddr());
		user.setLoginTime(new Date());
		sysUserRepository.updateUserLoginInfo(user);
	}

	@Override
	public boolean validatePwd(String oldPassword, Long userId) {
		SysUser user = this.getId(userId);
		return user.getPassword().equals(ShiroAuthManager.encrypt(oldPassword, user.getSalt()));
	}
    
}