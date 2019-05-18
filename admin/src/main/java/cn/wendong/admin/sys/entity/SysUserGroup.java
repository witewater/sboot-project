package cn.wendong.admin.sys.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

import cn.wendong.admin.common.base.BaseEntity;
import cn.wendong.admin.core.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户组
 * @author MB yangtdo@qq.com
 * @date 2019-01-23
 */
@Entity
@Table(name = "sys_user_group")
@Getter
@Setter
public class SysUserGroup extends BaseEntity<SysUserGroup>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8661115034519279337L;

	private String name;//名称
	
	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "text")
	private String rules;//规则

	private Byte status = StatusEnum.NORMAL.getCode();//状态
}
