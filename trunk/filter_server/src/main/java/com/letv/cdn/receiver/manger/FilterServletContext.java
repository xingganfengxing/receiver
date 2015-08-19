package com.letv.cdn.receiver.manger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FilterServer控制
 * 
 * @author kk
 */
public class FilterServletContext implements ServletContextListener{
    
    private static final Logger log = LoggerFactory.getLogger(FilterServletContext.class);
    
    /**
     * Called when the container is shutting down.
     */
    public void contextDestroyed(ServletContextEvent sce) {
    
        try {
            DisruptorServer.stopDisruptorServer();
//            MessageServer.stopMessageServer();
            KafkaProcessManager.closeAllProcess();
        } catch (Exception ex) {
            log.error("Error stopping Filter", ex);
        }
        
    }
    
    /**
     * Called when the container is first started.
     */
    public void contextInitialized(ServletContextEvent sce) {
    
        try {
            DisruptorServer.startDisruptorServer();
//            MessageServer.startMessageServer();
            KafkaProcessManager.getAllProcess();
        } catch (Exception ex) {
            log.error("Filter failed to initialize", ex);
        }
    }
    
}
