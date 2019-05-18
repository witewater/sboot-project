package cn.wendong.admin.core.shiro;

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import cn.wendong.admin.core.config.properties.ProjectProperties;
import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.core.shiro.session.SessionDAO;
import cn.wendong.admin.sys.entity.SysRole;
import cn.wendong.admin.sys.entity.SysUser;
import cn.wendong.admin.sys.service.SysRoleService;
import cn.wendong.admin.sys.service.SysUserService;
import cn.wendong.admin.sys.utils.CaptchaHelper;
import cn.wendong.admin.sys.utils.UserUtils;

/**
 * 自定义AuthorizingRealm,认证&授权
 * 
 * @author MB
 * @date 2018-12-02
 */
public class ShiroAuthRealm extends AuthorizingRealm {

	@Autowired
	private SysUserService userService;

	@Autowired
	private SysRoleService roleService;

	@Autowired
	private ProjectProperties projectProperties; 
	
	@Autowired
	private SessionDAO sessionDao;
	
	/**
	 * 授权逻辑 只有需要验证权限时才会调用, 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.在配有缓存的情况下，只加载一次.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if(!projectProperties.isMultiAccountLogin()){//是否允许多账户登录
			Collection<Session> sessions = sessionDao.getActiveSessions();
			if (sessions.size() > 0) {
				// 如果是登录进来的，则踢出已在线用户
				if (ShiroAuthManager.getSubject().isAuthenticated()) {
					for (Session session : sessions) {
						sessionDao.delete(session);
					}
				}else {// 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
					ShiroAuthManager.getSubject().logout();
					throw new AuthenticationException("账号已在其它地方登录， 请重新登录。");
				}
			}
		}
		
		// 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// 获取用户Principal对象
		SysUser user = (SysUser) principals.getPrimaryPrincipal();
		
		if (user != null) {
			// 获取角色和资源（JPA延迟加载超时，通过用户ID获取角色列表）
			Set<SysRole> roles = roleService.getUserRoleList(user.getId());
			if (roles != null) {
				// 将角色和菜单封装到Subject主体对象
				user.setRoles(roles);
				// 赋予角色和资源授权
				roles.forEach(role -> {
					// 单角色用户情况
					info.addRole(role.getName());
					role.getMenus().forEach(menu -> {
						if (menu.getStatus().equals(StatusEnum.NORMAL.getCode()) && !menu.getUrl().equals("#")) {
							info.addStringPermission(menu.getUrl());
						}
						/*if (StringUtils.isNotBlank(menu.getPermission())) {
							// 添加基于Permission的权限信息
							for (String permission : StringUtils.split(menu.getPermission(), ",")) {
								info.addStringPermission(permission);
							}
						}*/
					});
				});
			}
			
			// 添加用户权限
			info.addStringPermission("user");
			
			//更新登录IP和时间
			userService.updateUserLoginInfo(user);
		}
		return info;
	}

	/**
	 * 认证逻辑 ,认证回调函数,登录时调用
	 * 首先根据传入的用户名获取User信息；然后如果user为空，那么抛出没找到帐号异常UnknownAccountException；
	 * 如果user找到但锁定了抛出锁定异常LockedAccountException；最后生成AuthenticationInfo信息，
	 * 交给间接父类AuthenticatingRealm使用CredentialsMatcher进行判断密码是否匹配，
	 * 如果不匹配将抛出密码错误异常IncorrectCredentialsException；
	 * 另外如果密码重试次数太多将抛出超出重试次数异常ExcessiveAttemptsException；
	 * 在组装SimpleAuthenticationInfo信息时，
	 * 需要传入：身份信息（用户名）、凭据（密文密码）、加密盐（username+salt），
	 * CredentialsMatcher使用盐加密传入的明文密码和此处的密文密码进行匹配。
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		SimpleUsernamePasswordToken token = (SimpleUsernamePasswordToken) authenticationToken;
		
		// 校验登录验证码
		if (UserUtils.isValidateCodeLogin(token.getUsername(), false, false)) {
			Session session = ShiroAuthManager.getSession();
			if(!CaptchaHelper.validate(session, token.getCaptcha())){
				throw new CaptchaException("验证码错误");
			}
		}
		
		// 获取数据库中的用户名密码
		SysUser user = userService.getByName(token.getUsername());
		if (user == null) {
			throw new UnknownAccountException("该帐号不存在。");// 没找到帐号
		} else {
			
			/*if(isValidateLoginNum(token.getUsername(), false, false)){
				List<Long> ids = new ArrayList<>();
				ids.add(user.getId());
				userService.updateStatus(StatusEnum.FREEZED, ids);
			}*/
			
			if (StatusEnum.FREEZED.getCode().equals(user.getStatus())) {
				throw new LockedAccountException("账号被锁定。"); // 帐号被锁定
			}
			
			// 对盐进行加密处理
			ByteSource salt = ByteSource.Util.bytes(user.getSalt());
			/**
			 * 传入密码自动判断是否正确 参数1：传入对象给Principal 参数2：正确的用户密码 参数3：加盐处理 参数4：固定写法
			 */
			return new SimpleAuthenticationInfo(user, user.getPassword(), salt, getName());
		}
	}

	/**
	 * 设置认证加密方式  密码凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了）
	 */
	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
		matcher.setHashAlgorithmName(ShiroAuthManager.hashAlgorithmName);
		matcher.setHashIterations(ShiroAuthManager.hashIterations);
		super.setCredentialsMatcher(matcher);
	}

	/**
	 * 清除当前用户权限信息
	 */
	public void clearCachedAuthorizationInfo() {
		PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection, getName());
		super.clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清除当前用户认证信息
	 */
	public void clearCachedAuthenticationInfo() {
		PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection, getName());
		super.clearCachedAuthenticationInfo(principals);
	}

	/**
	 * 清除当前用户的认证和授权缓存信息
	 */
	public void clearAllCache() {
		clearCachedAuthorizationInfo();
		clearCachedAuthenticationInfo();
	}

	
}