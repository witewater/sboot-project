package cn.wendong.admin.core.utils;

import cn.wendong.tools.utils.ServletContextUtils;

/**
 * URL包装
 * @author MB yangtdo@qq.com
 * @date 2018-12-06
 */
public class URLWrapper {
	
	//请求路径
	private String url;
	
	public URLWrapper(String url){
		this.url = url;
	}
	
	/**
	 * 封装URL地址，自动添加应用上下文路径
	 * @param url
	 * @return
	 */
	public String getPathUrl() {
        return ServletContextUtils.getRequest().getContextPath() + url;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
