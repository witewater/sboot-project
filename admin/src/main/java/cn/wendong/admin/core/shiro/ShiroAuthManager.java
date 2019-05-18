package cn.wendong.admin.core.shiro;

import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

import cn.wendong.admin.core.constant.AdminConst;
import cn.wendong.admin.core.utils.SpringContextHolder;
import cn.wendong.admin.sys.entity.SysRole;
import cn.wendong.admin.sys.entity.SysUser;
import cn.wendong.tools.utils.Identities;

/**
 * 认证信息操作工具类
 * @author MB
 * @date 2018-12-02
 */
public class ShiroAuthManager {

    /**
     * 加密算法
     */
    public final static String hashAlgorithmName = "SHA-256";

    /**
     * 循环次数
     */
    public final static int hashIterations = 1024;

    /**
     * 加密处理
     * @param password 密码
     * @param salt 密码盐
     */
    public static String encrypt(String password, String salt){
        ByteSource byteSalt = ByteSource.Util.bytes(salt);
        return new SimpleHash(hashAlgorithmName, password, byteSalt, hashIterations).toString();
    }

    /**
     * 获取随机盐值
     */
    public static String getRandomSalt(){
        return Identities.getRandomString(6);
    }

    /**
     * 获取ShiroUser对象
     */
    public static SysUser getSysUser(){
        return (SysUser) SecurityUtils.getSubject().getPrincipal();
    }
    
    /**
	 * 获取用户id
	 * 
	 * @return
	 */
	public static Long getUserId() {
		return getSysUser() == null ? null : getSysUser().getId();
	}
    
    /**
	 * 获取shiro的session
	 */
	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}
	
	/**
	 * 获取shiro的Subject
	 */
	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	/**
	 * 把值放入到当前登录用户的Session里
	 * 
	 * @param key
	 * @param value
	 */
	public static void setSessionAttribute(Object key, Object value) {
		getSession().setAttribute(key, value);
	}

	/**
	 * 从当前登录用户的Session里取值
	 * 
	 * @param key
	 * @return
	 */
	public static Object getSessionAttribute(Object key) {
		return getSession().getAttribute(key);
	}

	/**
	 * 判断是否登录
	 * 
	 * @return
	 */
	public static boolean isLogin() {
		return null != SecurityUtils.getSubject().getPrincipal() && SecurityUtils.getSubject().isAuthenticated();
	}

	/**
	 * 退出登录
	 */
	public static void logout() {
		SecurityUtils.getSubject().logout();
	}
	
	public static boolean isSuperAdmin() {
		return isSuperAdmin(getUserId());
	}
	
	public static boolean isSuperAdmin(Long userId) {
		return AdminConst.SUPER_ADMIN_ID.equals(userId);
	}
	
    /**
     * 重置Cookie“记住我”序列化信息
     */
    public static void resetCookieRememberMe(){
        Set<SysRole> roles = getSysUser().getRoles();
        getSysUser().setRoles(null);
        RememberMeManager meManager = SpringContextHolder.getBean(RememberMeManager.class);
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setRememberMe(true);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo();
        info.setPrincipals(SecurityUtils.getSubject().getPrincipals());
        meManager.onSuccessfulLogin(SecurityUtils.getSubject(), token, info);
        getSysUser().setRoles(roles);
    }
}
