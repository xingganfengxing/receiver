package com.letvcloud.cdn.log.schedule;

import com.letvcloud.cdn.log.manager.VodLogCollectServer;
import com.letvcloud.cdn.log.util.Constants;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 是否保留错误日志定时任务
 * Created by liufeng1 on 2014/12/24.
 */
@Component
public class ZmqLogSchedule {

    private static Logger LOG = LoggerFactory.getLogger(ZmqLogSchedule.class);

//    @Scheduled(cron="${enableSaveZmqLogCron}")
    public void enableSaveZmqLog(){
        LOG.info("zmqlog-set-true-schedule running");
        VodLogCollectServer.ENABLE_SAVEZMQFILES = true;
        LOG.info("zmqlog-set-true-schedule end");
    }

//    @Scheduled(cron="${disableSaveZmqLogCron}")
    public void disableSaveZmqLog(){
        LOG.info("zmqlog-set-false-schedule running");
        VodLogCollectServer.ENABLE_SAVEZMQFILES = false;
        LOG.info("zmqlog-set-false-schedule end");
    }

    @Scheduled(cron="${deleteExpireLogCron}")
    public void deleteExpireLog(){

        LOG.info("deleteExpireLog running");

        File zmqfiles = new File(Constants.ZMQ_FILEPATH);
        File[] dayFiles = zmqfiles.listFiles();

        for(File dayFile : dayFiles){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                Date fileDate = sdf.parse(dayFile.getName());
                Calendar nowInstance = Calendar.getInstance();
                nowInstance.set(Calendar.HOUR_OF_DAY,0);
                nowInstance.set(Calendar.MINUTE,0);
                nowInstance.set(Calendar.SECOND,0);

                long interDay = (nowInstance.getTimeInMillis() - fileDate.getTime()) / (60 * 60 * 24 * 1000);
                if(interDay >= 2){      //只保留2天的数据
                    FileUtils.deleteQuietly(dayFile);
                }

            } catch (ParseException e) {
                LOG.error(e.getMessage());
            }
        }

        LOG.info("deleteExpireLog end");
    }
}
