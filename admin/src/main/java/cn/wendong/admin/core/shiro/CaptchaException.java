package cn.wendong.admin.core.shiro;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 验证码异常处理类
 * @author MB yangtdo@qq.com
 * @date 2018-12-02
 */
public class CaptchaException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public CaptchaException() {
		super();
	}

	public CaptchaException(String message, Throwable cause) {
		super(message, cause);
	}

	public CaptchaException(String message) {
		super(message);
	}

	public CaptchaException(Throwable cause) {
		super(cause);
	}

}
