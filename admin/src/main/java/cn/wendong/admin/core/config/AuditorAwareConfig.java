package cn.wendong.admin.core.config;

import java.util.Optional;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import cn.wendong.admin.sys.entity.SysUser;

/**
 * 审计人自动赋值配置
 * Spring Data提供支持审计功能  
 * 说明：实体类的属性上增加@CreatedBy，@LastModifiedBy，@CreatedDate，@LastModifiedDate注解，并配置相应的配置项，即可实现审计功能
 * @author MB
 * @date 2018-12-02
 */
@Configuration
public class AuditorAwareConfig implements AuditorAware<SysUser> {
	@Override
	public Optional<SysUser> getCurrentAuditor() {
		Subject subject = SecurityUtils.getSubject();
		SysUser user = (SysUser) subject.getPrincipal();
		return Optional.ofNullable(user);
	}
}
