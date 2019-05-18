package cn.wendong.admin.core.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import cn.wendong.admin.core.utils.AppUtils;
import lombok.Data;

/**
 * 项目配置
 * 
 * @author MB
 * @date 2018-12-02
 */
@Data
@Component
@ConfigurationProperties(prefix = "sboot")
public class ProjectProperties {
	// 上传文件路径
	private String fileUploadPath;
	// 上传文件静态访问路径
	private String staticPathPattern = "/upload/**";
	// cookie记住登录信息时间，默认7天
	private Integer rememberMeTimeout = 7;
	// Session会话超时时间，默认30分钟
	private Integer globalSessionTimeout = 1800;
	// Session会话检测间隔时间，默认15分钟
	private Integer sessionValidationInterval = 900;
	//是否允许多账户登录
	private boolean multiAccountLogin = true;//允许
	//是否开启验证码
	private boolean useCaptcha = false;
	//后台访问路径
	private String adminPath;
	
	/* xss防护设置 */
    private boolean xssEnabled = true;//是否启用xss防护
    // 拦截规则，可通过“,”隔开多个
    private String xssUrlPatterns = "/*";
    // 忽略规则，可通过“,”隔开多个
    private String xssExcludes = "/favicon.ico,/img/*,/js/*,/css/*,/lib/*";
	
	public String getFileUploadPath() {
		if (fileUploadPath == null) {
			return AppUtils.getProjectPath() + "/upload/";
		}
		return fileUploadPath;
	}

}
