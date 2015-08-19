package com.letvcloud.cdn.log.manager;

import com.letvcloud.cdn.log.main.CollectMain;
import com.letvcloud.cdn.log.tasks.VodLogCollectTask;
import com.letvcloud.cdn.log.util.Constants;
import com.letvcloud.cdn.log.util.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 点播数据采集服务
 *
 * @author liufeng1
 */
public class VodLogCollectServer {

    private static final Logger LOG = LoggerFactory.getLogger(VodLogCollectServer.class);

    // 是否保存从zmq收到的gzip
    public static boolean ENABLE_SAVEZMQFILES = Boolean.parseBoolean(Env.get("enableSaveZmqFiles"));

    public static boolean isRun = false;

    private static  ThreadPoolTaskExecutor collectTaskExecutor =
            (ThreadPoolTaskExecutor) CollectMain.CTX.getBean("collectExecutor");

    public static void startServer() {

        isRun = true;
        for (int i = 0; i < Constants.VODLOG_COLLECT_THREAD_NUM; i++) {
            collectTaskExecutor.execute(new VodLogCollectTask());
        }
        LOG.info("VodLogCollectServer Is Running.");
    }

    public static void stopServer(){
        isRun = false;
        collectTaskExecutor.shutdown();
        LOG.info("VodLogCollectServer Is stop.");
    }
}
