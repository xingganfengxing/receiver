package com.letvcloud.cdn.log.service;

import com.letvcloud.cdn.log.manager.KafkaManager;
import com.letvcloud.cdn.log.model.ParseLineResult;
import com.letvcloud.cdn.log.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 日志处理，split，过滤，入kafka，保留原始日志
 * Created by liufeng1 on 2014/12/23.
 */
public class LogProcess {

    private static final Logger LOG = LoggerFactory.getLogger(LogProcess.class);
    private static final String messageTopic = Env.get("messageTopic");
    private static KafkaUtil[] KAFKA_UTILS = KafkaManager.KAFKA_UTILS;
    public static boolean ENABLE_SAVEERRORLOG = Boolean.parseBoolean(Env.get("enableSaveErrorLog"));

    /**
     * 解析，过滤，入kafka,保留原始日志
     *
     * @param fileName
     * @param body
     * @param threadIndex
     */
    public static void process(String fileName, String body, int threadIndex) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        boolean isKafkaAlive = NetUtil.isKafkaAlive(Constants.KAFKA_SERVER_IP);
        StringBuilder write2LocalBuilder = new StringBuilder("");
        Map<String, StringBuilder> logMap = new HashMap<>();  //key 文件路径 value 文件内容

        int succCount = 0, failCount = 0;
        double sendKafkaSize = 0;

        StringTokenizer stringTokenizer = new StringTokenizer(body, "\n");
        while (stringTokenizer.hasMoreElements()) {
            String line = stringTokenizer.nextToken();
            if (!StringUtils.isEmpty(line)) {

                //flv,hls直播原始日志收集
                LiveLogBuild.buildLiveLogMap(logMap,line);

                //解析
                ParseLineResult parseLineResult = LogParse.parseLog(line, false);

                if (parseLineResult.isParseSuccess()) {

                    //过滤
                    parseLineResult = LogFilter.filterLog(parseLineResult, line, false);

                    //cdn,cloud原始日志收集
                    logMap = LogSave.buildLogMap(parseLineResult, logMap, line);

                    if (parseLineResult.isParseSuccess()) {
                        succCount++;
                        String join = parseLineResult.getLogData().toLine();

                        //入kafka
                        if (isKafkaAlive) {
                            KAFKA_UTILS[threadIndex].withTopic(messageTopic).sendObjectKafka(join);
                            sendKafkaSize += join.getBytes().length;
                        } else {
                            write2LocalBuilder.append(join + "\n");
                        }
                    }
                }
                if (!parseLineResult.isParseSuccess()) {
                    failCount++;
                }
            }
        }

        //保留原始日志
        LogSave.saveDisasterLog(write2LocalBuilder, fileName);
        LogSave.saveLogMap(logMap);

        if (isKafkaAlive) {
            StatisUtil.statLogInfo(succCount, failCount, sendKafkaSize);
        } else {
            LOG.error("kafka server is not alive,saved to local");
        }
        LOG.info("process total {} success {}, fail {}, cost {} ms",
                succCount + failCount, succCount, failCount, stopWatch.getTime());
    }
}
