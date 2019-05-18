package cn.wendong.admin.sys.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class OfficeModel {

	/**
	 * 状态1：正常，0：禁用
	 */
	@NotNull
	@Length(min = 1, max = 1)
	@Pattern(regexp = "1|0")
	private String status;
}
