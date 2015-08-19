package com.letv.cdn.receiver.schedule;

import com.letv.cdn.common.Env;
import com.letv.cdn.receiver.dao.CloudLivePresureDao;
import com.letv.cdn.receiver.tasks.ConsumeTask;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 定时任务 批量插入
 *
 * @author liufeng1
 * @date 9/4/2015
 */
@Component
public class CloudLivePreSchedule {

    @Autowired
    private CloudLivePresureDao cloudLivePresureDao;

    @Autowired
    private ThreadPoolTaskExecutor consumeExecutor;

    @Scheduled(cron = "${cloudLivePresureCron}")
    public void insertBatch(){
        for(int i=0; i<Integer.parseInt(Env.get("consumeThreadNum")); i++){
            consumeExecutor.execute(new ConsumeTask());
        }
    }
}
