package com.letvcloud.cdn.log.service;

import com.google.common.base.Stopwatch;
import com.letvcloud.cdn.log.util.Constants;
import com.letvcloud.cdn.log.util.DateUtil;
import com.letvcloud.cdn.log.util.HdfsExecCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 从HDFS中下载rtmp访问日志定时任务
 */
@Component
public class GetRtmpAccLogService {
    private static final Logger LOG = LoggerFactory.getLogger(GetRtmpAccLogService.class);
    private static final String OUTPUT_PATH = "/storm/rtmpaccbeta/splitlogs/";

    public void downloadFile() {
        Stopwatch stopwatch = new Stopwatch().start();
        String yesterday = DateUtil.getYesterDay(Constants.DAY_FORMAT);
        String outpath = OUTPUT_PATH + yesterday;
        String downloadPath = "/data/soft/vodcollect/originlogs";
        List<String> args = new ArrayList<String>();
        args.add("-get");
        args.add(outpath);
        args.add(downloadPath);
        try {
            HdfsExecCommand.excute(args);
        } catch (Exception e) {
            LOG.error("get rtmpacclog error,caused by {}", e);
        }
        LOG.info("GetRtmpAccLog success cost {}ms", stopwatch.elapsedMillis());
    }
}
