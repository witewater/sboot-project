package cn.wendong.tools.utils;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 *  封装各种生成唯一性ID算法的工具类.
 * @author MB
 * @date 2018-12-02
 */
public class Identities {

    private static SecureRandom random = new SecureRandom();

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间有-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid2() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    /**
     * 获取随机位数的字符串
     * @param length 随机位数
     */
    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            // 获取ascii码中的字符 数字48-57 小写65-90 大写97-122
            int range = random.nextInt(75)+48;
            range = range<97?(range<65?(range>57?114-range:range):(range>90?180-range:range)):range;
            sb.append((char)range);
        }
        return sb.toString();
    }
    
    /**
     * 获取指定位数的随机数
     *
     * @param num
     * @return
     */
    public static String getRandom(int num) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            sb.append(String.valueOf(random.nextInt(10)));
        }
        return sb.toString();
    }
    
}
