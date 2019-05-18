package cn.wendong.admin.sys.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class LogModel {

	@NotNull
	@Length(min = 1, max = 50)
	private String name;//日志名称
	
	@NotNull
	@Length(min = 1, max = 1)
	@Pattern(regexp = "1|2|3")
	private String type;//日志类型
}
