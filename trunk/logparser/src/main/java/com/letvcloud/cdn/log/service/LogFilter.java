package com.letvcloud.cdn.log.service;

import com.letvcloud.cdn.log.model.LogData;
import com.letvcloud.cdn.log.model.ParseLineResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 过滤一行日志
 * Created by liufeng1 on 2014/12/23.
 */
public class LogFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogFilter.class);

    /**
     * 过滤一行日志
     *
     * @param parseLineResult
     */
    public static ParseLineResult filterLog(
            ParseLineResult parseLineResult, String line, boolean enablePrintErrorLog) {

        LogData logData = parseLineResult.getLogData();
        //判断platid,splatid非空
        if (StringUtils.isEmpty(logData.getPlatid()) ||
                StringUtils.isEmpty(logData.getSplatid())) {

            //允许CDN(platid=102) cloud(platid=2) splatid为空
            if(!("102".equals(logData.getPlatid())
                    || "2".equals(logData.getPlatid()))){
                parseLineResult.setParseSuccess(false);
                if (enablePrintErrorLog) {
                    LOGGER.error("==empty_platidorsplatid=={}", line);
                }
                return parseLineResult;
            }
        }

        //不收直播的m3u8业务
        if ("10".equals(logData.getPlatid()) && line.contains(".m3u8?")) {
            parseLineResult.setParseSuccess(false);
            return parseLineResult;
        }

        //云视频过滤逻辑 2.203,2.206,2.207 后面的yuntag必须是数字
        if ("2".equals(logData.getPlatid())) {
            if (logData.getCustomerName().matches("2.20[3,6,7]b")) {
                String sign = logData.getSign();
                String yuntag = sign.contains("bcloud_") ? sign.substring(7) : sign;
                if (!yuntag.matches("\\d*")) {
                    if (enablePrintErrorLog) {
                        LOGGER.error("==customNameerror=={}", line);
                    }
                    parseLineResult.setParseSuccess(false);
                    return parseLineResult;
                }
            }
        }

        //cdn过滤逻辑
        if ("102".equals(logData.getPlatid())) {
            if (logData.getCustomerName().length() > 32) {
                parseLineResult.setParseSuccess(false);
                if (enablePrintErrorLog) {
                    LOGGER.error("==customNameerror=={}", line);
                }
                return parseLineResult;
            }
        }
        return parseLineResult;
    }
}
