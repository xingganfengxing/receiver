package com.letv.cdn.receiver.tasks;

import com.google.common.base.Stopwatch;
import com.letv.cdn.receiver.context.CtxInitListener;
import com.letv.cdn.receiver.dao.CloudLivePresureDao;
import com.letv.cdn.receiver.model.LogData;
import com.letv.cdn.receiver.util.BlockingQueueManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 持久化收到的日志
 * @author liufeng1
 * @date 14/4/2015
 */
public class ConsumeTask implements Runnable {

    private static Logger LOGGER = LoggerFactory.getLogger(ConsumeTask.class);

    public void run() {
        LOGGER.info("start to execute consumeTask");
        List<LogData> logList = new ArrayList<LogData>();
        LogData logData = BlockingQueueManager.getQueue().poll();
        while(null != logData){
            logList.add(logData);
            logData = BlockingQueueManager.getQueue().poll();
            if(null == logData || logList.size() >= 10000){
                insertBatch(logList);
            }
        }
    }

    private void insertBatch(List<LogData> logList){
        ApplicationContext ctx = CtxInitListener.getCTX();
        CloudLivePresureDao cloudLivePresureDao =(CloudLivePresureDao)ctx.getBean("cloudLivePresureDao");
        Stopwatch stopwatch = new Stopwatch().start();
        cloudLivePresureDao.insertBatch(logList);
        LOGGER.info("insert Batch size {} success,cost {}ms",
                logList.size(), stopwatch.elapsedMillis());
        logList.clear();
    }
}
