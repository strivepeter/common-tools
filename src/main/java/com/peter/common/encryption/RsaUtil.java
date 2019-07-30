package com.peter.common.encryption;

import com.peter.common.bytes.HexUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @ClassName: RsaUtil
 * @Description: rsa加解密
 * @Author: peter
 * @Date: 2019/07/29下午2:11
 * @Version: 1.0
 */
public class RsaUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * PROVIDER_NAME ： BouncyCastleProvider的名字
     * MAX_ENCRYPT_BLOCK ： RSA最大加密明文大小
     * MAX_DECRYPT_BLOCK ：RSA最大解密密文大小
     */
    private static final String PROVIDER_NAME = "BC";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 获取密钥对
     *
     * @return 密钥对
     * @throws Exception
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        return generator.generateKeyPair();
    }


    /**
     * 根据长度获取密钥对
     *
     * @param length 长度
     * @return 秘钥对
     * @throws Exception 异常
     */
    public static KeyPair getKeyPair(int length) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        return generator.generateKeyPair();
    }


    /**
     * 获取私钥
     *
     * @param privateKey 获取私钥的key
     * @return 返回私钥
     * @throws Exception 异常
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }


    /**
     * 获取公钥
     *
     * @param publicKey 获取公钥的字符串
     * @return 返回公钥
     * @throws Exception 异常
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @return 加密的后的字符串
     * @throws Exception 异常
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return new String(Base64.encodeBase64String(encryptedData));
    }


    /**
     * RSA解密
     *
     * @param data       解密自符串
     * @param privateKey 解密的私钥
     * @return 返回字符串
     * @throws Exception 抛出异常
     */
    public static String decrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, "UTF-8");
    }


    /**
     * 获取签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     * @return 返回字符串
     * @throws Exception 抛出异常
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("Sha1WithRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验证签名
     *
     * @param srcData   原始字符串
     * @param publicKey 公钥
     * @param sign      签名
     * @return 是否验签通过
     * @throws Exception 抛出异常
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("Sha256WithRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(sign.getBytes());
    }

    /**
     * 公钥验签字节数组
     *
     * @param publicKey 公钥
     * @param content   待签名数据
     * @param sign      签名值
     * @return 验签结果
     * @throws InvalidKeyException 公钥错误
     * @throws SignatureException  签名或数据错误
     */
    public static boolean checkSign(RSAPublicKey publicKey, byte[] content, byte[] sign) throws InvalidKeyException, SignatureException {
        Signature signature;
        try {
            signature = Signature.getInstance("Sha256WithRSA", PROVIDER_NAME);
        } catch (NoSuchAlgorithmException e) {
            //签名算法名是本工具类提供的，如果错了业务没有办法处理。所以这个异常不需要外部捕获。
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        signature.initVerify(publicKey);
        signature.update(content);
        return signature.verify(sign);
    }


    public static void main(String[] args) {
        try {
            // 生成密钥对
            KeyPair keyPair = getKeyPair();
            String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
            String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
            System.out.println("私钥:" + privateKey);
            System.out.println("公钥:" + publicKey);

            // RSA加密
            String data = "201906251609230000";
            String encryptData = encrypt(data, getPublicKey(publicKey));
            System.out.println("加密后内容:" + encryptData);

            // RSA解密
            String decryptData = decrypt(encryptData, getPrivateKey(privateKey));
            System.out.println("解密后内容:" + decryptData);


            // RSA签名
            String sign = sign(data, getPrivateKey(privateKey));
            System.out.println(String.format("签名: %s", sign.length()));
            // RSA验签
            boolean result = verify(data, getPublicKey(publicKey),sign);
            System.out.print("验签结果:" + result);


//            String sign = "RiZC2ctNUgN4NhxIar2GyJqU1Qe6tfjXl+60ajIA6kG3cFkUkjJbGxmQqjlOq1ovFtACIcJJ8BrV5y2aJS7ohHKVKRBdQqJohswkHuZbHHAfLcwDH8FuM1Cu1EBL7tBCZfQcPOma0DQE7WgjE1hfI/F0rzSMdcaysEBt2Zrtvdc=";
//            String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjFen6L4vhjUCL4saNk8ZaVUS/S4Sa3YSs932jyJ4bGcSQhPLzMLwqGbf/nvRPhJgpSN3FhssYOTSSAvvxAv3jJ7DDMVsaiu0+X6/hxeK2HBNA5OUTxFzViG/ghUfeU5KFHHFGoFr4FsZK6RC10J8vyjXqLQ2NVpn4L/gROme+TulETT0UP/NrQ8gCOpbFX/Di/rEIXBRX4oEGtUNwBfktCKu3HQpoQcbpnC0o9Eg8s51Ca0VLHSg3bWAZG3dqjNRDqS0+ItVwSVP9EgVYHNL5tMPAP+laP3oIqNVohK8WzAofVF2FtQvNfcfWa0ljNcBdb1CYyHw8xaCWy7vmBxc8QIDAQAB";
//            String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKX5K8qGPO0uwrl3/G6f8nxRYUU76UNSNAvGu6yWo2GJ673SXJE1q0n7bresb9SBfjJTEf4RkAh1zSXXzLKWrAzWr/dn8GIiUMDUnkld1FJ4FpPkFKg3sk6pynKbh44iJFrpytybaZ7Ws3OVYVI2R2zoYix0q1fIdU8FsDNQjoklAgMBAAECgYB5IU76cdoJaDUpUw5iVk2MuX/6HW1yeo4Ngi76GL6IszXfxaVVPQwT5HK7jhfXyeImLEZXXM33vrHISqTUOe66BzNYLTy/DekS4M3NsPBe3R5QLHLKwbL0ZmRC8bqFH/VcQq8wAAF7oRLfyalHlOClh2PYz8n3DvTgPUEgJnNkgQJBAPIna+fAZuU+LSjvG8IQtDENylfCLkC1pMa3IwPDEysdD7bbQAolfFM0KIAlyQtp6CQcIFLe4NcZKlSoQ+w8I3ECQQCvdqO2HLYrSqSo0pCZtWHaVW+VMOv8wh6n81RFUt452Agn7IZnD45OSDawFwmJMV2Lf/gCZeOJLDDtGQiU/371AkEAq8m1rlJ7bLQgW4LNwW4SNx6eZoW2RGfYfrTbXUbd6V8Tj9iY5NnVCj3dYZPsvXEjo8JkWAYPoekTc71vej7rsQJAVxymsK5Wfu3DDBHmOnnDyBrsl/w+3TEe+TEkYHqA2+T2P6P9IkOl2hWeGRSG5nb8w1pYLhCZw6uN6tin2xlx1QJAOI6EuzmD2SF7G7FxlgssS+DopCnrqHgW69L1E8dnpwYL0WWnVuu90MSMz/DQ4wVGCBtMpYojFzYQWRsbJu90JA==";
//
//            System.out.println(sign.length());
//            boolean result = checkSign((RSAPublicKey) getPublicKey(publicKey), data.getBytes("UTF-8"), HexUtils.hexStringToByte(sign));
//            System.out.print("验签结果:" + result);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("加解密异常");
        }
    }
}
