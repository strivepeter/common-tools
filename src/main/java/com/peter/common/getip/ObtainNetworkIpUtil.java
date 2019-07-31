package com.peter.common.getip;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName ObtainNetworkIpUtil
 * @Description 获取请求者的IP的地址
 * @Author peter
 * @Date 2019/6/18 10:58
 * @Version 1.0
 */
public class ObtainNetworkIpUtil {
    private static final String UNKNOWN = "unknown";
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";

    private static Logger logger = LoggerFactory.getLogger(ObtainNetworkIpUtil.class);


    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     * request.getHeader("X-Forwarded-For");
     *
     * @param request 请求的request
     * @return 返回Ip的地址
     * @throws IOException 抛出的异常
     */
    public final static String getIpAddress(HttpServletRequest request) throws IOException {
        String ip;
        logger.info(String.format("最开始getIpAddress(HttpServletRequest) - Remote - String ip= %s", request.getRemoteAddr()));
        logger.info(String.format("最开始getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip= %s", request.getHeader("X-Forwarded-For")));
        if (request.getHeader(X_FORWARDED_FOR) == null) {
            ip = request.getRemoteAddr();
            logger.info("getIpAddress(HttpServletRequest) - Remote - String ip=" + ip);
        } else {
            ip = request.getHeader("X-Forwarded-For");
            logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
        }

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
                }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }
}
