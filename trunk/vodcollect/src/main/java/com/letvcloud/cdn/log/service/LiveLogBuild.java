package com.letvcloud.cdn.log.service;

import com.letvcloud.cdn.log.model.LiveLog;
import com.letvcloud.cdn.log.util.StrBuilderMapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 直播hls,flv外网原始日志收集
 *
 * @author liufeng1
 * @date 3/6/2015
 */
public class LiveLogBuild {
    public static final String LIVE_FLAG = "clive_cname";
    private static final Logger LOG = LoggerFactory.getLogger(LiveLogBuild.class);
    /**
     * 直播hls,flv外网原始日志收集
     *
     * @param logMap
     * @param
     */
    public static void buildLiveLogMap(Map<String, StringBuilder> logMap, String line) {
        //live原始日志收集
        if (line.contains(LIVE_FLAG)) {
            LOG.info("receive hls or flv log:{}",line);
            LiveLog liveLog = LiveLogParse.parse(line);
            String dayTime = liveLog.getPtime().substring(0, 8);
            //日期/live_协议_域名_天
            String key = StringUtils.join(
                    new String[]{
                            dayTime + "/live",
                            liveLog.getProtocol(),
                            liveLog.getDomain(),
                            dayTime
                    }, "_"
            );
            StrBuilderMapUtils.appendValue(logMap, key, line);
        }
    }
}
