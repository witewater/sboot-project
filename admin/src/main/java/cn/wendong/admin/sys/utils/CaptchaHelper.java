package cn.wendong.admin.sys.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.session.Session;

/**
 * 验证码工具类
 * 
 * @author MB yangtdo@qq.com
 * @date 2018-12-22
 */
public class CaptchaHelper {

	private static int width = 120;
	private static int height = 45;
	private static int charLength = 4;// 验证码文本字符长度
	private static String exChars = "iolIOL10";// 排除字符
	public static final String VALIDATE_CODE = "validateCode";

	/**
	 * 向页面输出验证码
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void createImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 设置响应头信息，通知浏览器不要缓存
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		String w = request.getParameter("width");
		String h = request.getParameter("height");
		if (StringUtils.isNumeric(w) && StringUtils.isNumeric(h)) {
			width = NumberUtils.toInt(w);
			height = NumberUtils.toInt(h);
		}
		String s = getRandomCode();
		request.getSession().setAttribute(VALIDATE_CODE, s);
		OutputStream out = response.getOutputStream();
		ImageIO.write(CaptchaHelper.genCaptcha(s), "JPEG", out);
		out.close();
	}

	/**
	 * 判断验证码是否正确
	 * 
	 * @param request
	 * @param validateCode
	 * @return
	 */
	public static boolean validate(HttpServletRequest request, String validateCode) {
		if (validateCode == null) {
			return false;
		} else {
			String code = (String) request.getSession().getAttribute(VALIDATE_CODE);
			return validateCode.toUpperCase().trim().equals(code.toUpperCase().trim());
		}
	}

	/**
	 * 判断验证码是否正确
	 * 
	 * @param request
	 * @param validateCode
	 * @return
	 */
	public static boolean validate(Session session, String validateCode) {
		String code = (String) session.getAttribute(VALIDATE_CODE);
		if (StringUtils.isBlank(validateCode) || StringUtils.isBlank(code)) {
			return false;
		} else {
			return validateCode.toUpperCase().trim().equals(code.toUpperCase().trim());
		}
	}

	public static void clearCode(HttpServletRequest request) {
		request.getSession().setAttribute(VALIDATE_CODE, UUID.randomUUID().toString());
	}

	/**
	 * 生成随机验证码
	 */
	public static String getRandomCode() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		int i = 0;
		while (i < charLength) {
			int t = random.nextInt(123);
			if ((t >= 97 || (t >= 65 && t <= 90) || (t >= 48 && t <= 57))
					&& (exChars == null || exChars.indexOf((char) t) < 0)) {
				sb.append((char) t);
				i++;
			}
		}
		return sb.toString();
	}

	/**
	 * 生成验证码图片
	 * 
	 * @param randomCode
	 *            验证码
	 */
	public static BufferedImage genCaptcha(String randomCode) {
		// 创建画布
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		// 填充背景
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		Random random = new Random();
		// 绘制干扰线
		g.setColor(getRandColor(40, 150));
		for (int i = 0; i < 30; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int xl = random.nextInt(width / 2);
			int yl = random.nextInt(width / 2);
			g.drawLine(x, y, x + xl, y + yl + 20);
		}

		// 添加噪点
		float rate = 0.1f;
		int area = (int) (rate * width * height);
		for (int i = 0; i < area; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			image.setRGB(x, y, getRandColor(100, 200).getRGB());
		}

		// 绘制验证码
		int size = height - 4;
		String[] fontTypes = { "Arial", "Arial Black", "Algerian", "Calibri" };
		Font font = new Font(fontTypes[random.nextInt(fontTypes.length)], Font.ITALIC, size);
		g.setFont(font);
		char[] chars = randomCode.toCharArray();
		for (int i = 0; i < randomCode.length(); i++) {
			g.drawChars(chars, i, 1, ((width - 10) / randomCode.length()) * i + 5, height / 2 + size / 2 - 6);
		}

		g.dispose();
		return image;
	}

	/**
	 * 获取相应范围的随机颜色
	 * 
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 */
	private static Color getRandColor(int min, int max) {
		min = min > 255 ? 255 : min;
		max = max > 255 ? 255 : max;
		Random random = new Random();
		int r = min + random.nextInt(max - min);
		int g = min + random.nextInt(max - min);
		int b = min + random.nextInt(max - min);
		return new Color(r, g, b);
	}

}
