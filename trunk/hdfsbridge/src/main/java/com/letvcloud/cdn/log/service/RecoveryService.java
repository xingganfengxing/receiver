package com.letvcloud.cdn.log.service;

import com.letvcloud.cdn.log.manager.LogRecCollectServer;
import com.letvcloud.cdn.log.manager.LogRecConsumeServer;
import com.letvcloud.cdn.log.manager.RecQueueManager;
import com.letvcloud.cdn.log.model.RecoverFileInfo;
import com.letvcloud.cdn.log.util.RecoverConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;

/**
 * 从本地gzip压缩文件重新恢复到hdfs
 *
 * @author liufeng1
 * @date 27/5/2015
 */
@Component
public class RecoveryService {

    private static Logger LOGGER = LoggerFactory.getLogger(RecoveryService.class);

    /**
     * 启动日志恢复服务
     */
    public void startRecovery() {
        new Thread("filefilter-Thread") {
            public void run() {
                FileFilter filefilter = new FileFilter() {
                    public boolean accept(File file) {
                        if (file.getName().matches("^201505260[89].*")) {
                            return true;
                        }
                        return false;
                    }
                };

                File file = new File(RecoverConstants.LOG_PATH);
                File[] files = file.listFiles(filefilter);
                LOGGER.info(files.length + "");

                for (int i = 0; i < files.length; i++) {
                    try {
                        RecQueueManager.getUnRecQueue().put(new RecoverFileInfo(i, files[i]));
                    } catch (InterruptedException e) {
                        LOGGER.error(e.getMessage());
                    }
                }
                LogRecCollectServer.isRun = false;
                LogRecConsumeServer.isRun = false;
            }
        }.start();

        LogRecCollectServer.startServer();
        LogRecConsumeServer.startServer();
    }

    /**
     * 停止日志恢复服务
     */
    public void stopRecovery() {
        LOGGER.info("start to kill RecoveryService");
        LogRecCollectServer.stopServer();
        LogRecConsumeServer.stopServer();
    }
}
