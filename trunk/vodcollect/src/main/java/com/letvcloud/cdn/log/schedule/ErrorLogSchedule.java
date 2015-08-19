package com.letvcloud.cdn.log.schedule;

import com.letvcloud.cdn.log.service.LogProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 是否保留错误日志定时任务
 * Created by liufeng1 on 2014/12/24.
 */
//@Component
public class ErrorLogSchedule {

    private static Logger LOG = LoggerFactory.getLogger(ErrorLogSchedule.class);

//    @Scheduled(cron="${enableSaveErrorLogCron}")
    public void enableSaveErrorLog(){
        LOG.info("originlog-set-false-schedule running");
        LogProcess.ENABLE_SAVEERRORLOG = true;
        LOG.info("originlog-set-false-schedule end");
    }

//    @Scheduled(cron="${disableSaveErrogLogCron}")
    public void disableSaveErrogLog(){
        LOG.info("originlog-set-true-schedule running");
        LogProcess.ENABLE_SAVEERRORLOG = false;
        LOG.info("originlog-set-true-schedule end");
    }
}
