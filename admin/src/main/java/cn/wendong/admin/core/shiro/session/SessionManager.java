package cn.wendong.admin.core.shiro.session;

import java.util.Collection;
import java.util.Date;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

/**
 * session管理
 * @author MB yangtdo@qq.com
 * @date 2018-12-14
 */
public class SessionManager extends DefaultWebSessionManager {
	
	public void validateSessions() {
		super.validateSessions();
	}

	protected Session retrieveSession(SessionKey sessionKey) {
		try {
			return super.retrieveSession(sessionKey);
		} catch (UnknownSessionException e) {
		}
		return null;
	}

	public Date getStartTimestamp(SessionKey key) {
		try {
			return super.getStartTimestamp(key);
		} catch (InvalidSessionException e) {
		}
		return null;
	}

	public Date getLastAccessTime(SessionKey key) {
		try {
			return super.getLastAccessTime(key);
		} catch (InvalidSessionException e) {
		}
		return null;
	}

	public long getTimeout(SessionKey key) {
		try {
			return super.getTimeout(key);
		} catch (InvalidSessionException e) {
		}
		return 0L;
	}

	public void setTimeout(SessionKey key, long maxIdleTimeInMillis) {
		try {
			super.setTimeout(key, maxIdleTimeInMillis);
		} catch (InvalidSessionException localInvalidSessionException) {
		}
	}

	public void touch(SessionKey key) {
		try {
			super.touch(key);
		} catch (InvalidSessionException localInvalidSessionException) {
		}
	}

	public String getHost(SessionKey key) {
		try {
			return super.getHost(key);
		} catch (InvalidSessionException e) {
		}
		return null;
	}

	public Collection<Object> getAttributeKeys(SessionKey key) {
		try {
			return super.getAttributeKeys(key);
		} catch (InvalidSessionException e) {
		}
		return null;
	}

	public Object getAttribute(SessionKey sessionKey, Object attributeKey) {
		try {
			return super.getAttribute(sessionKey, attributeKey);
		} catch (InvalidSessionException e) {
		}
		return null;
	}

	public void setAttribute(SessionKey sessionKey, Object attributeKey, Object value) {
		try {
			super.setAttribute(sessionKey, attributeKey, value);
		} catch (InvalidSessionException localInvalidSessionException) {
		}
	}

	public Object removeAttribute(SessionKey sessionKey, Object attributeKey) {
		try {
			return super.removeAttribute(sessionKey, attributeKey);
		} catch (InvalidSessionException e) {
		}
		return null;
	}

	public void stop(SessionKey key) {
		try {
			super.stop(key);
		} catch (InvalidSessionException localInvalidSessionException) {
		}
	}

	public void checkValid(SessionKey key) {
		try {
			super.checkValid(key);
		} catch (InvalidSessionException localInvalidSessionException) {
		}
	}

	protected Session doCreateSession(SessionContext context) {
		try {
			return super.doCreateSession(context);
		} catch (IllegalStateException e) {
		}
		return null;
	}

	protected Session newSessionInstance(SessionContext context) {
		Session session = super.newSessionInstance(context);
		session.setTimeout(getGlobalSessionTimeout());
		return session;
	}

	public Session start(SessionContext context) {
		SimpleSession session;
		try {
			return super.start(context);
		} catch (NullPointerException e) {
			session = new SimpleSession();
			session.setId(Integer.valueOf(0));
		}
		return session;
	}
}
