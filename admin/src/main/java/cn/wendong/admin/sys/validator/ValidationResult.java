package cn.wendong.admin.sys.validator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

/**
 * 验证结果信息返回
 * @author MB yangtdo@qq.com
 * @date 2019-01-16
 */
@Data
public class ValidationResult {

	//校验结果是否有错
	private boolean hasErrors = false;
	//存放错误信息
	private Map<String,String> errorMsgMap = new HashMap<>();
	
	//通过格式化信息获取错误结果
	public String getErrorMsg(){
		return StringUtils.join(errorMsgMap.values().toArray(),",");
	}
}
