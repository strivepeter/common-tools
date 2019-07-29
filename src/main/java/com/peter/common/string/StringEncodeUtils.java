package com.peter.common.string;


import java.io.UnsupportedEncodingException;

import static org.apache.logging.log4j.util.Strings.isEmpty;

/**
 * @ClassName StringEncodeUtils
 * @Description 关于字符串的转码问题
 * @Author peter
 * @Date 2019/6/20 8:50
 * @Version 1.0
 */
public class StringEncodeUtils {
    private static final String GB2312 = "GB2312";
    private static final String ISO = "ISO-8859-1";
    private static final String UTF8 = "utf-8";
    private static final String GBK = "GBK";


    /**
     * 将字符串的编码格式转换为utf-8
     *
     * @param str 传入的字符串
     * @return String 的字符串
     */
    public static String toUTF8(String str) {
        if (isEmpty(str)) {
            return "";
        }
        try {
            if (str.equals(new String(str.getBytes(GB2312), GB2312))) {
                str = new String(str.getBytes(GB2312), UTF8);
                return str;
            }
            if (str.equals(new String(str.getBytes(ISO), ISO))) {
                str = new String(str.getBytes(ISO), UTF8);
                return str;
            }
            if (str.equals(new String(str.getBytes(GBK), GBK))) {
                str = new String(str.getBytes(GBK), UTF8);
                return str;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * ---------------测试---------------
     *
     * @param args
     */
    public static void main(String[] args) {

    }
}
