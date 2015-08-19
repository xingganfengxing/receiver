package com.letvcloud.cdn.log.util;

import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 网络工具类
 * Created by liufeng1 on 2015/1/12.
 */
public class NetUtil {
    private static Logger LOG = LoggerFactory.getLogger(NetUtil.class);

    /**
     * 判断给定的ip,port是否联通
     *
     * @param ip   对应ip地址
     * @param port 对应端口
     * @return true 连通 false 连接异常
     */
    public static boolean isIpPortAlive(String ip, int port) {
        boolean result = true;
        TelnetClient telnetClient = new TelnetClient();
        try {
            telnetClient.connect(ip, port);
        } catch (IOException e) {
            result = false;
            LOG.error(e.getMessage());
        } finally {
            try {
                telnetClient.disconnect();
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
        return result;
    }

    /**
     * 判断kafka集群网络通
     *
     * @param ipPort
     * @return
     */
    public static boolean isKafkaAlive(String ipPort) {
        String[] ipportsStr = ipPort.split(",");
        for (String ipPorts : ipportsStr) {
            String[] ipPortArr = ipPorts.split(":");
            if (!isIpPortAlive(ipPortArr[0], Integer.parseInt(ipPortArr[1]))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取本机ip地址
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getIp() {
        String ip = "127.0.0.1";
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
            ip =  addr.getHostAddress().toString();
        } catch (UnknownHostException e) {
            LOG.error(e.getMessage());
        }
        return ip;
    }
 }
