package cn.wendong.admin.core.exception;

import cn.wendong.admin.core.enums.ResultEnum;

/**
 * 业务异常处理
 * @author MB
 * @date 2018-12-02
 */
public class ResponseException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Integer code;//错误代码
    
    private String msg;//错误信息
    
	private Object[] params;//响应参数
    /**
     * 统一异常处理
     * @param resultEnum 状态枚举
     */
    public ResponseException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMessage();
    }

    /**
     * 统一异常处理
     * @param code 状态码
     * @param message 提示信息
     */
    public ResponseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }
    
    public ResponseException(Integer code, String message, Object[] params) {
		super(message);
		this.code = code;
        this.msg = message;
		this.params = params;
	}

	public ResponseException(Integer code, Object[] params) {
		super();
		this.code = code;
		this.params = params;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
	
	public Object[] getParams() {
		return params;
	}
}
