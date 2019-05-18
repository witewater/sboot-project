package cn.wendong.admin.core.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 用户和密码（包含验证码）令牌类
 * @author MB
 * @date 2018-12-02
 */
public class SimpleUsernamePasswordToken extends UsernamePasswordToken{

	private static final long serialVersionUID = 1L;

	private String captcha;
	private boolean mobileLogin;
	
	public SimpleUsernamePasswordToken() {
		super();
	}

	public SimpleUsernamePasswordToken(String username, String password, String rememberMe, String captcha) {
		super(username, password, rememberMe);
		this.captcha = captcha;
	}
	
	public SimpleUsernamePasswordToken(String username, char[] password,
			boolean rememberMe, String host, String captcha, boolean mobileLogin) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
		this.mobileLogin = mobileLogin;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public boolean isMobileLogin() {
		return mobileLogin;
	}
	
}
