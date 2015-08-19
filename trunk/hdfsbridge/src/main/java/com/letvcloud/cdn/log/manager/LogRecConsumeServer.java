package com.letvcloud.cdn.log.manager;

import com.letvcloud.cdn.log.tasks.LogRecConsumeTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 日志恢复消费者管理服务
 *
 * @author liufeng1
 */
@Component
public class LogRecConsumeServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogRecConsumeServer.class);

    public static boolean isRun = false;

//    @Autowired
    private static ThreadPoolTaskExecutor consumeExecutor;

    public static void startServer() {

        isRun = true;

        for (int i = 0; i <30; i++) {
            consumeExecutor.execute(new LogRecConsumeTask());
        }
        LOGGER.info("LogRecConsumeServer Is Running.");
    }

    public static void stopServer(){
        isRun = false;
        consumeExecutor.shutdown();
        LOGGER.info("LogRecConsumeServer Is stop.");
    }
}
