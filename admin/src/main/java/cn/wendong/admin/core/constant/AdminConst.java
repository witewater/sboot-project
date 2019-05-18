package cn.wendong.admin.core.constant;

/**
 * 超级管理员常量 
 * 
 * @author MB
 * @date 2018-12-02
 */
public class AdminConst {

	public static final String SESSION_USER_MENUS = "Sessionu-User-Menus";

	/**
	 * 超级管理员id
	 */
	public static final Long SUPER_ADMIN_ID = 1L;

	/**
	 * 超级管理员角色id
	 */
	public static final Long SUPER_ADMIN_ROLE_ID = 1L;

	public enum KickoutMode {
		/**
		 * 后者登录的用户踢出前者登录的用户
		 */
		BEFORE,
		/**
		 * 全部踢出
		 */
		ALL
	}

}
