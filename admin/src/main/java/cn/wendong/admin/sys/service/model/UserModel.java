package cn.wendong.admin.sys.service.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * 页面数据显示
 * @author MB yangtdo@qq.com
 * @date 2019-02-24
 */
@Data
public class UserModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7739741492012366994L;
	
	private Long id;
	@NotNull
	private Long officeId;//机构id
	private String officeName;//机构名称
	@NotEmpty(message = "用户名不能为空")
	@Length(min = 5, max = 50)
    private String username;
    @NotEmpty(message = "用户昵称不能为空")
    @Size(min = 2, message = "用户昵称：请输入至少2个字符")
    private String nickname;
    @NotNull
	@Length(min = 1, max = 50)
    private String name;//用户姓名
    @Length(min = 1, max = 100)
    private Integer age;//年龄
    /**
     * 1:男; 2:女
     */
    @NotNull
	@Length(min = 1, max = 1)
	@Pattern(regexp = "1|2")
    private String sex;//性别
    @JsonIgnore
	private String password;//新密码
    @JsonIgnore
    private String oldPassword;//原密码
    @JsonIgnore
    private String conFirmPassword;//确认密码
    @Length(max = 50)
    private String phone;//手机
    @Length(max = 50)
    private String email;//邮箱
    @Length(max = 100)
	private String picture;//头像
	/**
	 * 图像完整路径
	 */
	private String pictureFullUrl;
	@Length(max = 2)
	private String uType;//用户类型，业务扩展用，读取字典
	/**
	 * 状态1：正常，0：禁用
	 */
	@NotNull
	@Length(min = 1, max = 1)
	@Pattern(regexp = "1|0")
	private String status;
	/**
	 * 用户所拥有的角色ID
	 */
	@JsonIgnore
	private List<String> roleIds;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //出参时间格式化
	private Date loginTime;//最后登录时间
	private String loginIp;//最后登录IP
	//原登录记录
	private String oldLoginIp; // 上次登陆IP
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //出参时间格式化
	private Date oldLoginDate; // 上次登陆日期
}
