package com.letvcloud.cdn.log.service;

import com.letvcloud.cdn.log.model.LogData;
import com.letvcloud.cdn.log.model.ParseLineResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * 解析一行日志
 * Created by liufeng1 on 2014/12/23.
 */
public class LogParse {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogParse.class);

    /**
     * 解析一行日志
     *
     * @param line                一行日志
     * @param enablePrintErrorLog 是否开启解析错误日志的打印
     * @return
     */
    //TODO url字段
    public static ParseLineResult parseLog(String line, boolean enablePrintErrorLog) {
        LogData logData = new LogData();
        ParseLineResult parseLineResult = new ParseLineResult();
        String[] temp = line.split("\"");// 总体拆分

        try {
            int tmp = temp[0].indexOf("[");
            int tmp2 = temp[0].indexOf(" ");

            if (temp.length < 3 || tmp + 1 > temp[0].length() - 2
                    || tmp2 + 1 > tmp - 1 || temp[1].length() - 9 < 0) {
                if (enablePrintErrorLog) {
                    LOGGER.error("==parse log error=={}", line);
                }
                parseLineResult.setParseSuccess(false);
                return parseLineResult;
            }

            String ptime = temp[0].substring(tmp + 1, temp[0].length() - 2);
            String userip = temp[0].substring(tmp2 + 1, tmp - 1);
            String serverip = temp[0].substring(0, temp[0].indexOf(":"));
            String[] tmpArr = temp[2].split(" ");

            //找被引号分割后，包含带宽的字符串
            int i = 8;
            while (i < temp.length && temp[i].length() < 20) {
                i++;
            }

            String bwContain = temp[i];
            String[] bwArr = bwContain.split(" ");

            //如果不是数字，取第九列
            String bandwith = bwArr[bwArr.length - 2];
            if (!bandwith.matches("\\d*")) {
                bandwith = tmpArr[2];
            }

            String hitMiss = bwArr[2];

            String httpcode = tmpArr[1];
            String responsetime = tmpArr[3];

            tmp = temp[1].indexOf("?");

            String requestUrl = "";
            if (tmp > 0) {
                String[] urlArray = temp[1].substring(0, tmp).split(" ");
                if (urlArray.length >= 2) {
                    requestUrl = urlArray[1];
                }
            }

            tmpArr = temp[1].substring(tmp + 1, temp[1].length() - 9).split("&");
            HashMap<String, String> map = new HashMap<String, String>();
            String[] tmpArr2 = null;

            for (String str : tmpArr) {
                tmpArr2 = str.split("=");
                map.put(tmpArr2[0], tmpArr2.length < 2 ? "" : tmpArr2[1]);
            }

            String maliu = map.containsKey("b") ? map.get("b") : "";
            String platid = map.containsKey("platid") ? map.get("platid") : "";
            String splatid = map.containsKey("splatid") ? map.get("splatid") : "";
            String playid = map.containsKey("playid") ? map.get("playid") : "";
            String sign = map.containsKey("sign") ? map.get("sign") : "";
            String geo = map.containsKey("geo") ? map.get("geo") : "";
            String mmsid = map.containsKey("mmsid") ? map.get("mmsid") : "";

            String streamId = map.containsKey("stream_id") ? map.get("stream_id") : "";
            if (map.containsKey("lb_src_streamid")) {
                streamId = map.get("lb_src_streamid");
            }
            String customerName = "";

            if ((StringUtils.isEmpty(platid) || "0".equals(platid)) && "coopdown".equals(sign)) {
                platid = "102";
            }

            //云视频 需要b后面那个customerName
            if ("2".equals(platid)) {
                String yuntag = sign.contains("bcloud_") ? sign.substring(7) : sign;
                customerName = platid + "." + splatid + "b" + yuntag;
            }

            //判断 CDN用户plantid是空。并且需要customerName
            if ("102".equals(platid)) {
                if (map.containsKey("tag")) {
                    customerName = map.get("tag");
                }
            }

            logData.setPtime(ptime);
            logData.setBandwidth(bandwith);
            logData.setMaliu(maliu);
            logData.setHttpcode(httpcode);
            logData.setUserip(userip);
            logData.setServerip(serverip);
            logData.setPlatid(platid);
            logData.setSplatid(splatid);
            logData.setSign(sign);
            logData.setPlayid(playid);
            logData.setResponsetime(responsetime);
            logData.setCustomerName(customerName);
            logData.setGeo(geo);
            logData.setHitMiss(hitMiss);
            logData.setRequestUrl(requestUrl);
            logData.setRefererHeader(temp[3]);
            logData.setStreamId(streamId);
            logData.setMmsid(mmsid);
            parseLineResult.setLogData(logData);

        } catch (Exception e) {
            parseLineResult.setParseSuccess(false);
            if (enablePrintErrorLog) {
                LOGGER.error("==parse log error=={}", line);
            }
            LOGGER.error(e.getMessage());
        }
        return parseLineResult;
    }
}
