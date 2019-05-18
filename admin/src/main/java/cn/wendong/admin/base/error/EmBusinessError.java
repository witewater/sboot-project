package cn.wendong.admin.base.error;

/**
 * 枚举异常参数
 * @author MB yangtdo@qq.com
 * @date 2019-01-06
 */
public enum EmBusinessError implements CommonError {
	
	//通用错误类型00001
	PARAMETER_VALIDATION_ERROR(00001,"参数不合法"),
	
	UNKNOWN_ERROR(00002,"未知错误")
	
	;

	private int errCode;
	private String errMsg;
	
	private EmBusinessError(int errCode,String errMsg){
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
	
	@Override
	public int getErrorCode() {
		return this.errCode;
	}

	@Override
	public String getErrorMsg() {
		return this.errMsg;
	}

	@Override
	public CommonError setErrorMsg(String msg) {
		this.errMsg = msg;
		return this;
	}

}
