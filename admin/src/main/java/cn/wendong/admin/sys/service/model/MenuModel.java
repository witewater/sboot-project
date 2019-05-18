package cn.wendong.admin.sys.service.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * 业务层数据模型
 * @author MB yangtdo@qq.com
 * @date 2019-01-15
 */
@Data
public class MenuModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	@NotEmpty(message = "标题不能为空")
    @Length(min=1,max=50)
    private String name;
    @NotNull(message = "父级菜单不能为空")
    private Long pid;
    private String pids;
    @NotEmpty(message = "url地址不能为空")
    @Length(max=200)
    private String url;
    @Length(max=50)
    private String icon;//图标
    @NotNull(message = "菜单类型不能为空")
  	@Length(max=2)
  	@Pattern(regexp="1|2|3")
    private Byte type;
    private Integer sort;//排序
    @Length(max=10)
 	private String target;//目标
    @Length(max=200)
	private String permission;//权限标识
    @NotNull(message = "菜单显示不能为空")
   	@Length(max=1)
   	@Pattern(regexp="0|1")
   	private Byte isShow;//是否在菜单中显示1显示，0 不显示
    /**
	 * 状态1：正常，0：禁用
	 */
	@NotNull
	@Length(min = 1, max = 1)
	@Pattern(regexp = "1|0")
	private Byte status;
}
