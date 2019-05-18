package cn.wendong.admin.core.shiro.session;

import java.util.Collection;
import org.apache.shiro.session.Session;

/**
 * session管理接口
 * @author MB yangtdo@qq.com
 * @date 2018-12-14
 */
public abstract interface SessionDAO extends org.apache.shiro.session.mgt.eis.SessionDAO {
	
	public abstract Collection<Session> getActiveSessions(boolean paramBoolean);

	public abstract Collection<Session> getActiveSessions(boolean paramBoolean, Object paramObject,
			Session paramSession);
}
