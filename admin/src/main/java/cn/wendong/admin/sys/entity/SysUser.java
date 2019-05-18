package cn.wendong.admin.sys.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.wendong.admin.common.base.BaseEntity;
import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.core.enums.UserIsAdminEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户实体
 * 
 * @author MB
 * @date 2018-12-01
 */
@Entity
@Table(name = "sys_user")
@Getter
@Setter
public class SysUser extends BaseEntity<SysUser> {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -1906537547432303809L;
	
	/**
     * 这样则表示该属性，在数据库中的名称是 username，并且使唯一的且不能为空的
     */
    @Column(name = "username",unique = true,nullable = false)
	private String username;//用户名
    @JsonIgnore
	private String password;//密码
	@JsonIgnore
	private String salt;//加密盐
	private String nickname;//昵称
	private String name;//用户姓名
	private String picture;//头像
	private Integer age;//年龄
	private String sex;//性别
	private String phone;//手机
	private String email;//电子邮件
	private String remark;//备注
	private String uType;//用户类型
	private byte rank;//用户级别
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "office_id",nullable = true)
	@JsonIgnore
	private SysOffice sysOffice; // 部门
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //出参时间格式化
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//入参时，请求报文只需要传入yyyymmddhhmmss字符串进来，则自动转换为Date类型数据
	private Date loginTime;//最后登录时间
	private String loginIp;//最后登录IP
	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY)//@ManyToOne的注解，所以这个属性就是外键
	@JoinColumn(name = "create_by", nullable = false, updatable = false)
	@JsonIgnore
	private SysUser createBy;
	@LastModifiedBy
	@ManyToOne(fetch = FetchType.LAZY)//@ManyToOne的注解，所以这个属性就是外键
	@JoinColumn(name = "update_by")
	@JsonIgnore
	private SysUser updateBy;
	
	private Byte isRole = UserIsAdminEnum.NO.getCode();

	private Byte status = StatusEnum.NORMAL.getCode();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sys_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@JsonIgnore
	private Set<SysRole> roles = new HashSet<>(0);

	/**
	 * 不是数据库字段
	 */
	@Transient
	private String oldLoginIp; // 上次登陆IP
	@Transient
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //出参时间格式化
	private Date oldLoginDate; // 上次登陆日期
}