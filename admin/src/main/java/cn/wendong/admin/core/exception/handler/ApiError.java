package cn.wendong.admin.core.exception.handler;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 统一错误信息
 * @author MB yangtdo@qq.com
 * @date 2019-01-19
 */
@Data
public class ApiError {
	
	private Integer status;//状态
	private String message;//信息
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timestamp;//时间戳

	private ApiError(){
		this.timestamp = LocalDateTime.now();
	}
	
	public ApiError(Integer status,String message){
		this();
		this.status=status;
		this.message=message;
	}
	
}
