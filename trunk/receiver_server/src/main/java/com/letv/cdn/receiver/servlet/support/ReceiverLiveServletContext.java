package com.letv.cdn.receiver.servlet.support;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.receiver.manager.DaemonMappingManager;
import com.letv.cdn.receiver.manager.ExceptionMessageServer;
import com.letv.cdn.receiver.manager.KafkaProcessManager;
import com.letv.cdn.receiver.manager.VodBandwidthServer;
import com.letv.cdn.receiver.util.MemcacheUtil;

/**
 * ReceiverLiveServletContext
 * 
 * @author kk
 */
public class ReceiverLiveServletContext implements ServletContextListener{
    
    private static final Logger LOG = LoggerFactory.getLogger(ReceiverLiveServletContext.class);
    
    /**
     * Called when the container is shutting down.
     */
    public void contextDestroyed(ServletContextEvent sce) {
    
        try {
            MemcacheUtil.disConnect();
            KafkaProcessManager.closeAllProcess();
            ExceptionMessageServer.stopExceptionMessageServer();
            VodBandwidthServer.stopVodBandwidthServer();
        } catch (Exception ex) {
            LOG.error("Error stopping Receiver", ex);
        }
        
    }
    
    /**
     * Called when the container is first started.
     */
    public void contextInitialized(ServletContextEvent sce) {
    
        try {
            int period = 24 * 60 * 60 * 1000; // 定时时间间隔
            // Calendar calendar = Calendar.getInstance();
            // int year = calendar.get(Calendar.YEAR);
            // int month = calendar.get(Calendar.MONTH);
            // int day = calendar.get(Calendar.DAY_OF_MONTH);// 每天
            // calendar.set(year, month, day, 9, 40, 00);
            Timer timer = new Timer(true);
            timer.schedule(new DaemonMappingManager(), 0, period);
            // timer.schedule(new DaemonMappingManager(), 0,
            // 60*2*1000);
            // timer.schedule(new DaemonMappingManager(), 0);
            ExceptionMessageServer.startExceptionMessageServer();
            VodBandwidthServer.startVodBandwidthServer();
        } catch (Exception ex) {
            LOG.error("Receiver failed to initialize", ex);
        }
    }
    
}
