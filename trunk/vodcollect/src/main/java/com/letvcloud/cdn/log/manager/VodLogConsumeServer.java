package com.letvcloud.cdn.log.manager;

import com.letvcloud.cdn.log.main.CollectMain;
import com.letvcloud.cdn.log.tasks.VodLogConsumeTask;
import com.letvcloud.cdn.log.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 点播数据消费服务
 *
 * @author liufeng1
 */
public class VodLogConsumeServer {

    private static final Logger LOG = LoggerFactory.getLogger(VodLogConsumeServer.class);

    public static boolean isRun = false;

    private static ThreadPoolTaskExecutor consumeTaskExecutor =
            (ThreadPoolTaskExecutor) CollectMain.CTX.getBean("consumeExecutor");

    public static void startServer() {

        isRun = true;

        for (int i = 0; i < Constants.VODLOG_CONSUME_THREAD_NUM; i++) {
            consumeTaskExecutor.execute(new VodLogConsumeTask(i));
        }
        LOG.info("VodLogConsumeServer Is Running.");
    }

    public static void stopServer(){
        isRun = false;
        consumeTaskExecutor.shutdown();
        LOG.info("VodLogConsumeServer Is stop.");
    }

}
