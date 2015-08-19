package com.letv.cdn.receiver.tasks;

import com.google.common.base.Stopwatch;
import com.letv.cdn.receiver.dao.RtmpdfnDaoLogReport;
import com.letv.cdn.receiver.model.ILogData;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liufeng1
 * @date 6/5/2015
 */
public class RtmpdfnTask implements Job {

    private static Logger LOG = LoggerFactory.getLogger(RtmpdfnTask.class);

    private RtmpdfnDaoLogReport rtmpdfnDao = new RtmpdfnDaoLogReport();

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Stopwatch stopwatch = new Stopwatch().start();
        List<ILogData> logList = new ArrayList<ILogData>();
        ILogData logData = RtmpdfnDaoLogReport.getQueue().poll();
        while (null != logData) {
            logList.add(logData);
            logData = RtmpdfnDaoLogReport.getQueue().poll();
            if (null == logData || logList.size() >= 5000) {
                try {
                    rtmpdfnDao.insertBatch(logList);
                    LOG.info(" RtmpdfnTask end,insert batch size {},cost {} ms ",
                            logList.size(), stopwatch.elapsedMillis());
                } catch (SQLException e) {
                    LOG.error(e.getMessage());
                }
            }
        }

    }
}
