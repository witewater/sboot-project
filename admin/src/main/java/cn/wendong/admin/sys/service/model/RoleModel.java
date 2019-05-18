package cn.wendong.admin.sys.service.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class RoleModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	@NotEmpty(message = "角色编号不能为空")
    private String name;
    @NotEmpty(message = "角色名称不能为空")
    private String title;
    /**
     * 数据范围
     */
	@NotNull
	@Length(min=1,max=1)
	@Pattern(regexp="0|1|2|3")
    private String dataScope;
    /**
	 * 状态1：正常，0：禁用
	 */
	@NotNull
	@Length(min = 1, max = 1)
	@Pattern(regexp = "1|0")
	private String status;

}
