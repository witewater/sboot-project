package cn.wendong.admin.core.xssfilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * xss请求包装
 * @author MB yangtdo@qq.com
 * @date 2019-02-08
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name) {
       // Whitelist whitelist = Whitelist.relaxed();

        String[] params = super.getParameterValues(name);
        if(params != null){
            for (int i=0; i<params.length; i++) {
               // params[i] = Jsoup.clean(params[i], whitelist).trim();
            }
        }
        return params;
    }
}
