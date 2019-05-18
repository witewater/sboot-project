package cn.wendong.admin.base.error;

/**
 * 统一错误信息 接口可扩展
 * @author MB yangtdo@qq.com
 * @date 2019-01-06
 */
public interface CommonError {

	public int getErrorCode();
	public String getErrorMsg();
	public CommonError setErrorMsg(String msg);
	
}
