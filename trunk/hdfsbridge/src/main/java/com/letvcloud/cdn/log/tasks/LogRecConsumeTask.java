package com.letvcloud.cdn.log.tasks;

import com.letvcloud.cdn.log.manager.LogRecConsumeServer;
import com.letvcloud.cdn.log.manager.RecQueueManager;
import com.letvcloud.cdn.log.model.RecoverFileInfo;
import com.letvcloud.cdn.log.service.RecProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志消费任务
 * Created by liufeng1 on 2015/2/12.
 */
public class LogRecConsumeTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogRecConsumeTask.class);

    public void run() {

        while (LogRecConsumeServer.isRun || RecQueueManager.getRecQueue().size() > 0) {
            try {
                RecoverFileInfo recoverFileInfo = RecQueueManager.getRecQueue().take();
                RecProcess.process(recoverFileInfo);
                LOGGER.info("consume file:{} success,index:{}",
                        recoverFileInfo.getFile().getAbsolutePath(),recoverFileInfo.getIndex());
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
        }

    }
}
