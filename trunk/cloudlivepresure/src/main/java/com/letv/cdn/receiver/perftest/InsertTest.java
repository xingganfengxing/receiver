package com.letv.cdn.receiver.perftest;

import com.google.common.base.Stopwatch;
import com.letv.cdn.receiver.dao.CloudLivePresureDao;
import com.letv.cdn.receiver.model.LogData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liufeng1
 * @date 16/4/2015
 */
public class InsertTest {

    private static final Logger LOG = LoggerFactory.getLogger(InsertTest.class);
    private static ApplicationContext context;
    private static final String[] SPRING_CONF_PATH = {"classpath*:applicationContext.xml"};

    public static void main(String[] args) {
        if (context == null) {
            context = new ClassPathXmlApplicationContext(SPRING_CONF_PATH);
        }

        CloudLivePresureDao cloudLivePresureDao = (CloudLivePresureDao) context.getBean("cloudLivePresureDao");

        List<LogData> logList = new ArrayList<LogData>();
        for (int i = 0; i < 10000; i++) {
            LogData logData = new LogData();
            logData.setClientIp("127.0.0.1");
            logData.setLoadingDuration("test");
            logData.setLogType(1);
            logData.setTaskId("23");
            logData.setOriginRoute("testtest");
            logData.setPlaybackDuration("sdasfsdf");
            logData.setServerIp("127.0.0.1");
            logData.setStreamId("232");
            logData.setStreamIndex(3);
            logData.setTimestamp("2015-04-16 17:44:37");
            logList.add(logData);
        }
        Stopwatch stopWatch = new Stopwatch().start();
        cloudLivePresureDao.insertBatch(logList);
        LOG.info("batch insert cost " + stopWatch.elapsedMillis() + "ms=====================================");
    }
}
