package com.peter.common.string;


import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @ClassName StringUtil
 * @Description 关于字符串相关的方法
 * @Author peter
 * @Date 2019/7/29 13:54
 * @Version 1.0
 */
public class StringUtil {

    /**
     * 转换成字符串方法，其中如果是Integer格式的返回0，如果是Double格式的返回0.0
     *
     * @param object 给的参数
     * @return String的字符串
     */
    public static String toString(Object object) {
        if (object == null) {
            if (object instanceof Integer) {
                return "0";
            }
            if (object instanceof Double) {
                return "0.0";
            }
            return "";
        } else {
            return object.toString();
        }
    }


    /**
     * 清空字符串，如果为“”则转换成null
     *
     * @param src 给的参数
     * @return String的字符串
     */
    public static String emptyString2Null(String src) {
        if (src != null) {
            if ("".equals(src)) {
                src = null;
            }
        }
        return src;
    }


    /**
     * 转化成可在hql中使用的字符串 例：1,2 转为 '1','2'
     *
     * @param string 给的参数
     * @return 返回String字符串
     */
    public static String formatIds(String string) {
        if (string != null && string != "") {
            String[] id = string.split(",");
            StringBuffer idsStr = new StringBuffer();
            for (String str : id) {
                idsStr.append("'" + str + "',");
            }
            return idsStr.toString().substring(0, idsStr.length() - 1);
        } else {
            return "";
        }
    }


    /**
     * 用户身份证号码的打码隐藏加星号加* 18位和非18位身份证处理均可成功处理 参数异常直接返回null
     *
     * @param idCardNum 身份证号码或者银行卡号
     * @param front     需要显示前几位
     * @param end       需要显示末几位
     * @return 处理完成的身字符串
     */
    public static String cardNumMask(String idCardNum, int front, int end) {
        //字符串不能为空
        if (StringUtils.isEmpty(idCardNum)) {
            return null;
        }
        //需要截取的长度不能大于身份证号长度
        if ((front + end) > idCardNum.length()) {
            return null;
        }
        //需要截取的不能小于0
        if (front < 0 || end < 0) {
            return null;
        }
        //计算*的数量
        int asteriskCount = idCardNum.length() - (front + end);
        StringBuffer asteriskStr = new StringBuffer();
        for (int i = 0; i < asteriskCount; i++) {
            asteriskStr.append("*");
        }
        String regex = "(\\w{" + String.valueOf(front) + "})(\\w+)(\\w{" + String.valueOf(end) + "})";
        return idCardNum.replaceAll(regex, "$1" + asteriskStr + "$3");
    }


    public static void main(String[] args) {
        System.out.println(cardNumMask("6227003241901588939", 6, 4));


        try {
            Integer x = Integer.parseInt("2569uuu");
            String  str = "1kk";
            if (str.contains(".")){
                System.out.println("ddd");
            }
            int coin = Integer.parseInt(str);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
