package cn.wendong.admin.base.error;

/**
 * 包装器业务异常类实现
 * @author MB yangtdo@qq.com
 * @date 2019-01-06
 */
public class BusinessException extends Exception implements CommonError {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CommonError commonError;

	// 接受EmBusinessError的传参用于构造业务异常
	public BusinessException(CommonError commonError) {
		super();
		this.commonError = commonError;
	}

	//接受自定义参数异常
	public BusinessException(CommonError commonError, String errMsg) {
		super();
		this.commonError = commonError;
		this.commonError.setErrorMsg(errMsg);
	}

	@Override
	public int getErrorCode() {
		return this.commonError.getErrorCode();
	}

	@Override
	public String getErrorMsg() {
		return this.commonError.getErrorMsg();
	}

	@Override
	public CommonError setErrorMsg(String msg) {
		 this.commonError.setErrorMsg(msg);
		 return this;
	}

}
