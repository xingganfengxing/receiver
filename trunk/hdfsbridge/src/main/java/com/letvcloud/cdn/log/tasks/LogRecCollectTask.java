package com.letvcloud.cdn.log.tasks;

import com.letvcloud.cdn.log.manager.LogRecCollectServer;
import com.letvcloud.cdn.log.manager.RecQueueManager;
import com.letvcloud.cdn.log.model.RecoverFileInfo;
import com.letvcloud.cdn.log.util.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志恢复生产者
 */
public class LogRecCollectTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogRecCollectTask.class);

    public void run() {
        while (LogRecCollectServer.isRun || RecQueueManager.getUnRecQueue().size() > 0) {
            try {
                RecoverFileInfo recoverFileInfo = RecQueueManager.getUnRecQueue().take();
                byte[] bytes = FileUtils.readFileToByteArray(recoverFileInfo.getFile());
                recoverFileInfo.setContent(ZipUtils.recureUnZip(bytes,3));
                RecQueueManager.getRecQueue().put(recoverFileInfo);
                LOGGER.info("unzip file:{} success,index:{}",
                        recoverFileInfo.getFile().getAbsoluteFile(),recoverFileInfo.getIndex());
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
