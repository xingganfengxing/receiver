package com.letvcloud.cdn.log.service;

import com.letvcloud.cdn.log.model.LiveLog;
import com.letvcloud.cdn.log.util.TimeUtils;

/**
 * 解析 ptime,协议,域名
 *
 * @author liufeng1
 * @date 22/4/2015
 */
public class LiveLogParse {

    public static LiveLog parse(String line) {
        LiveLog liveLog = new LiveLog();
        int startIndex = line.indexOf("[") + 1;
        int endIndex = line.indexOf("]");

        String ptime = TimeUtils.format(
                line.substring(startIndex, endIndex),
                TimeUtils.SOURCETIME_FORMAT,
                TimeUtils.MILLI_TIME_FORMAT
        );

        //url中包含.ts .m3u8 HLS协议;.flv  FLV协议
        String protocol = "";
        if (line.contains(".m3u8") || line.contains(".ts")) {
            protocol = "hls";
        }
        if (line.contains(".flv")) {
            protocol = "flv";
        }

        startIndex = line.indexOf(LiveLogBuild.LIVE_FLAG)
                + LiveLogBuild.LIVE_FLAG.length() + 1;
        endIndex = line.indexOf(" ", startIndex);
        String domain = line.substring(startIndex, endIndex);

        liveLog.setPtime(ptime);
        liveLog.setProtocol(protocol);
        liveLog.setDomain(domain);
        return liveLog;
    }
}
