package com.letvcloud.cdn.log.manager;

import com.letvcloud.cdn.log.tasks.LogRecCollectTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 日志恢复生产者管理服务
 *
 * @author liufeng1
 */
@Component
public class LogRecCollectServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogRecCollectServer.class);

//    @Autowired
    private static ThreadPoolTaskExecutor collectExecutor;

    public static boolean isRun = false;

    public static void startServer() {
        isRun = true;

        for (int i = 0; i < 30; i++) {
            collectExecutor.execute(new LogRecCollectTask());
        }
        LOGGER.info("LogRecCollectServer Is Running.");
    }

    public static void stopServer() {
        isRun = false;
        collectExecutor.shutdown();
        LOGGER.info("LogRecCollectServer Is stop.");
    }
}
