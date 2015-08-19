package com.letv.cdn.receiver.servlet.support;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.receiver.util.Constants;
import com.letv.cdn.receiver.util.XMemcacheUtil;

/**
 * ReceiverVodServletContext
 * 
 * @author kk
 */
public class ReceiverVodServletContext implements ServletContextListener{
    
    private static final Logger log = LoggerFactory.getLogger(ReceiverVodServletContext.class);
    
    /**
     * Called when the container is shutting down.
     */
    public void contextDestroyed(ServletContextEvent sce) {
    
        try {
            SequenceServer.getInstance().stop();
        } catch (Exception ex) {
            log.error("Error stopping Receiver", ex);
        }
        
    }
    
    /**
     * Called when the container is first started.
     */
    public void contextInitialized(ServletContextEvent sce) {
    
        try {
            SequenceServer.getInstance().start();
        } catch (Exception ex) {
            log.error("Receiver failed to initialize", ex);
        }
    }
    
    static class SequenceServer{
        private static final SequenceServer sequenceServer = new SequenceServer();
        private final ExecutorService daemonExecutor = Executors.newFixedThreadPool(2);// 获取&更新序号状态线程
        private volatile boolean isRun = false;
        
        private SequenceServer() {
        
        }
        
        public static SequenceServer getInstance() {
        
            return sequenceServer;
        }
        
        public void start() throws Exception {
        
            isRun = true;
            daemonExecutor.execute(new updateFilterSequenceThread());
            daemonExecutor.execute(new updateLocalSequenceThread());
        }
        
        public void stop() throws Exception {
        
            if (isRun) {
                isRun = false;
                daemonExecutor.shutdown();
            }
        }
        
        class updateFilterSequenceThread implements Runnable{
            
            public updateFilterSequenceThread() {
            
            }
            
            public void run() {
            
                while (isRun) {
                    try {
                        int i = 0;
                        String name = null;
                        Object filter_curr_obj = null;
                        long min = Constants.filter_curr;
                        while (true) {// 依次取filter集群中最慢的一个
                            name = Constants.FILTER_SERVER_NAME + i + Constants.FILTER_LOCAL_SEQUENCER_KEY_CURR_POSTFIX;
                            filter_curr_obj = XMemcacheUtil.getFromCache(name);
                            if (null == filter_curr_obj) {
                                break;
                            }
                            long tmp = Long.parseLong(filter_curr_obj.toString());
                            if (i == 0) {
                                min = tmp;
                            }
                            if (tmp < min) {
                                min = tmp;
                            }
                            i++;
                        }
                        Constants.filter_curr = min;
                        Thread.sleep(1);
                    } catch (Throwable t) {
                        log.error("updateFilterSequenceThread err:", t);
                    }
                }
            }
        }
        
        class updateLocalSequenceThread implements Runnable{
            
            public updateLocalSequenceThread() {
            
            }
            
            public void run() {
            
                while (isRun) {
                    try {
                        XMemcacheUtil.saveToCache(Constants.RECEIVER_LOCAL_SEQUENCER_KEY_CURR, Constants.receiver_curr);
                        Thread.sleep(1);
                    } catch (Throwable t) {
                        log.error("updateLocalSequenceThread err:", t);
                    }
                }
            }
        }
    }
    
}
