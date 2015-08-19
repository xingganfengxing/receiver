package com.letv.cdn.receiver.servlet.support;

import com.letv.cdn.common.Env;
import com.letv.cdn.receiver.tasks.RtmpdfnTask;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * LogReportServletContext
 *
 * @author liufeng1
 */
public class LogReportServletContext implements ServletContextListener {

    private static Logger LOG = LoggerFactory.getLogger(LogReportServletContext.class);
    private Scheduler scheduler = null;

    public void contextInitialized(ServletContextEvent sce) {
        SchedulerFactory schedulerfactory = new StdSchedulerFactory();
        try {
            scheduler = schedulerfactory.getScheduler();
            JobDetail job = JobBuilder.newJob(RtmpdfnTask.class).withIdentity("RtmpdfnTask", "Rtmpdfn").build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("rtmpdfnTrigger", "RtmpdfnTriggerGroup")
                    .withSchedule(CronScheduleBuilder.cronSchedule(Env.get("rtmpdfnCron")))
                    .startNow().build();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}
