package com.peter.common.encryption;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * @ClassName Base64Util
 * @Description 将字符串简单的加密
 * @Author peter
 * @Date 2019/7/3 17:15
 * @Version 1.0
 */
public class Base64Util {
    private static final Logger LOG = LoggerFactory.getLogger(Base64Util.class);
    private static final String UTF_8 = "UTF-8";

    /**
     * 加密字符串
     *
     * @param inputData  加密的内容
     * @return 返回加密的字符串
     */
    public static String decodeData(String inputData) {
        try {
            if (null == inputData) {
                return null;
            }
            return new String(Base64.decodeBase64(inputData.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            LOG.error(inputData, e);
        }
        return null;
    }

    /**
     * 解密加密后的字符串
     *
     * @param inputData 解密的内容
     * @return 解密后的内容
     */
    public static String encodeData(String inputData) {
        try {
            if (null == inputData) {
                return null;
            }
            return new String(Base64.encodeBase64(inputData.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            LOG.error(inputData, e);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(Base64Util.encodeData("62042119910617451X"));
        String enStr = Base64Util.encodeData("我是中文");
        System.out.println(Base64Util.decodeData(enStr));
    }

}
