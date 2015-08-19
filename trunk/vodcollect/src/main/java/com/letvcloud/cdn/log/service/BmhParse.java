package com.letvcloud.cdn.log.service;

import com.letvcloud.cdn.log.model.LogData;
import org.apache.commons.lang3.StringUtils;

/**
 * 爆米花指定格式
 * @author liufeng1
 * @date 22/4/2015
 */
public class BmhParse {
    /**
     * 获取爆米花指定格式
     * remoteip:访问主机IP identity:标识符 authuser:授权用户 date:请求时间
     * request:请求URL status:状态码 bytes：请求字节 referrer：引用页
     * agent：用户代理 localip：本地主机(节点IP) time-taken：请求耗时     *
     *
     * @param logData
     * @param line
     * @return
     */
    public static String getBaomihuaLine(LogData logData, String line) {

        String remoteip = logData.getUserip();
        String identity = "-";                               //暂无
        String authuser = "-";                               //暂无
        String date = logData.getPtime();

        String[] quotesArray = line.split("\"");
        String request = "\"" + quotesArray[1] + "\"";       //request line

        String status = logData.getHttpcode();
        String bytes = logData.getBandwidth();
        String referrer = "\"" + logData.getRefererHeader() + "\"";      //referer header
        String agent = "\"" + quotesArray[5] + "\"";         //user-agent header
        String localip = logData.getServerip();

        String[] lineStrs = new String[]{
                remoteip, identity, authuser, date, request, status, bytes, referrer,
                agent, localip, logData.getResponsetime()
        };

        return StringUtils.join(lineStrs, "\t");
    }
}
