package cn.wendong.admin.common.enums;

public interface IErrorCode {
	
	public int getHttpStatus();

	public int getCode();

	public String getMessageFormat();
	
}
