package cn.wendong.tools.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取客户端信息
 *
 * @author mubai
 * <p>
 * 2017年6月29日 下午4:23:15
 */
public class UserAgentUtils {

	public static String getBrowserInfo(){
		return getBrowserInfo(ServletContextUtils.getRequest());
	}
	
	public static String getClientOS(){
		return getClientOS(ServletContextUtils.getRequest());
	}
	
    /**
     * 获取客户端浏览器信息
     *
     * @param request
     * @return string
     */
    public static String getBrowserInfo(HttpServletRequest req) {
        String browserInfo = "other";
        String ua = req.getHeader("User-Agent").toLowerCase();
        String s;
        String version;
        String msieP = "msie ([\\d.]+)";
        String ieheighP = "rv:([\\d.]+)";
        String firefoxP = "firefox\\/([\\d.]+)";
        String chromeP = "chrome\\/([\\d.]+)";
        String operaP = "opr.([\\d.]+)";
        String safariP = "version\\/([\\d.]+).*safari";

        Pattern pattern = Pattern.compile(msieP);
        Matcher mat = pattern.matcher(ua);
        if (mat.find()) {
            s = mat.group();
            version = s.split(" ")[1];
            browserInfo = "ie " + version.substring(0, version.indexOf("."));
            return browserInfo;
        }

        pattern = Pattern.compile(firefoxP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            s = mat.group();
            version = s.split("/")[1];
            browserInfo = "firefox " + version.substring(0, version.indexOf("."));
            return browserInfo;
        }

        pattern = Pattern.compile(ieheighP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            s = mat.group();
            version = s.split(":")[1];
            browserInfo = "ie " + version.substring(0, version.indexOf("."));
            return browserInfo;
        }

        pattern = Pattern.compile(operaP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            s = mat.group();
            version = s.split("/")[1];
            browserInfo = "opera " + version.substring(0, version.indexOf("."));
            return browserInfo;
        }

        pattern = Pattern.compile(chromeP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            s = mat.group();
            version = s.split("/")[1];
            browserInfo = "chrome " + version.substring(0, version.indexOf("."));
            return browserInfo;
        }

        pattern = Pattern.compile(safariP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            s = mat.group();
            version = s.split("/")[1].split(" ")[0];
            browserInfo = "safari " + version.substring(0, version.indexOf("."));
            return browserInfo;
        }
        return browserInfo;
    }

    /**
     * 获取客户端操作系统信息
     *
     * @param request
     * @return string
     */
    public static String getClientOS(HttpServletRequest req) {
        String userAgent = req.getHeader("User-Agent");
        String cos = "unknow os";
        Pattern p = Pattern.compile(".*(Windows NT 6\\.2).*");
        Matcher m = p.matcher(userAgent);
        if (m.find()) {
            cos = "Win 8";
            return cos;
        }

        p = Pattern.compile(".*(Windows NT 6\\.1).*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "Win 7";
            return cos;
        }

        p = Pattern.compile(".*(Windows NT 5\\.1|Windows XP).*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "WinXP";
            return cos;
        }

        p = Pattern.compile(".*(Windows NT 5\\.2).*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "Win2003";
            return cos;
        }

        p = Pattern.compile(".*(Win2000|Windows 2000|Windows NT 5\\.0).*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "Win2000";
            return cos;
        }

        p = Pattern.compile(".*(Mac|apple|MacOS8).*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "MAC";
            return cos;
        }

        p = Pattern.compile(".*(WinNT|Windows NT).*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "WinNT";
            return cos;
        }

        p = Pattern.compile(".*Linux.*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "Linux";
            return cos;
        }

        p = Pattern.compile(".*(68k|68000).*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "Mac68k";
            return cos;
        }

        p = Pattern.compile(".*(9x 4.90|Win9(5|8)|Windows 9(5|8)|95/NT|Win32|32bit).*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "Win9x";
            return cos;
        }

        return cos;
    }

    // \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
    // 字符串在编译时会被转码一次,所以是 "\\b"
    // \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
    static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i" + "|windows (phone|ce)|blackberry"
            + "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp" + "|laystation portable)|nokia|fennec|htc[-_]"
            + "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser" + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

    // 移动设备正则匹配：手机端、平板
    static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
    static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

    /**
     * 检测是否是移动设备访问
     *
     * @param userAgent 浏览器标识
     * @return true:移动设备接入，false:pc端接入
     * @Title: check
     */
    public static boolean CheckMobile(String userAgent) {
        if (null == userAgent) {
            userAgent = "";
        }
        // 匹配
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);
        if (matcherPhone.find() || matcherTable.find()) {
            return true;
        } else {
            return false;
        }
    }
}
