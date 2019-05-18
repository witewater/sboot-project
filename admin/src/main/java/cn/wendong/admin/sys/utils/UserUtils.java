package cn.wendong.admin.sys.utils;

import java.util.Map;

import com.google.common.collect.Maps;

import cn.wendong.admin.core.constant.AdminConst;
import cn.wendong.admin.core.shiro.ShiroAuthManager;
import cn.wendong.admin.core.utils.EhCacheUtil;

/**
 * 用户工具
 * @author MB yangtdo@qq.com
 * @date 2019-01-28
 */
public class UserUtils {

	/**
	 * 是否是验证码登录
	 * 
	 * @param useruame
	 *            用户名
	 * @param isFail
	 *            计数加1
	 * @param clean
	 *            计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean) {
		Map<String, Integer> loginFailMap = (Map<String, Integer>) EhCacheUtil.get("loginFailMap");
		if (loginFailMap == null) {
			loginFailMap = Maps.newHashMap();
			EhCacheUtil.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum == null) {
			loginFailNum = 0;
		}
		if (isFail) {
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean) {
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}
	
	/**
	 * 是否错误登录次数达到5次
	 * 
	 * @param useruame
	 *            用户名
	 * @param isFail
	 *            计数加1
	 * @param clean
	 *            计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateLoginNum(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>) EhCacheUtil.get("loginFailNumMap");
		if (loginFailMap == null) {
			loginFailMap = Maps.newHashMap();
			EhCacheUtil.put("loginFailNumMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum == null) {
			loginFailNum = 0;
		}
		if (isFail) {
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean) {
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 5;
	}
	
	public static boolean isSuperAdmin() {
		return isSuperAdmin(ShiroAuthManager.getUserId());
	}
	public static boolean isSuperAdmin(Long userId) {
		return AdminConst.SUPER_ADMIN_ID.equals(userId);
	}
	
}