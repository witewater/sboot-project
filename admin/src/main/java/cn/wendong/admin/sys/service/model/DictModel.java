package cn.wendong.admin.sys.service.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * 
 * @author MB yangtdo@qq.com
 * @date 2019-01-15
 */
@Data
public class DictModel {

	private Long id;
	@NotEmpty(message = "字典标识不能为空")
	private String name;
	@NotEmpty(message = "字典标题不能为空")
	private String title;
	@NotNull(message = "字典类型不能为空")
	private Byte type;
	@NotEmpty(message = "字典值不能为空")
	private String value;
	@NotNull(message = "字典备注不能为空")
	private String remark;//备注
	/**
	 * 状态1：正常，0：禁用
	 */
	@NotNull
	@Length(min = 1, max = 1)
	@Pattern(regexp = "1|0")
	private String status;
	
}
