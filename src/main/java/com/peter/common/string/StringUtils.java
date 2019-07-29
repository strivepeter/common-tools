package com.peter.common.string;

/**
 * @ClassName StringUtils
 * @Description 关于字符串相关的方法
 * @Author peter
 * @Date 2019/7/29 13:54
 * @Version 1.0
 */
public class StringUtils {
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
     * ---------------测试---------------
     *
     * @param args
     */
    public static void main(String[] args) {

    }
}
