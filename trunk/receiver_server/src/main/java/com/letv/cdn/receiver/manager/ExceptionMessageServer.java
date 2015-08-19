package com.letv.cdn.receiver.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.receiver.model.ReceiverExceptionData;
import com.letv.cdn.receiver.util.ExceptionMessageUtil;

/**
 * 异常消息处理服务
 * 
 * @author kk
 */
public class ExceptionMessageServer{
    
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionMessageServer.class);
    private static final Logger logException = LoggerFactory.getLogger("receiver_exception_msg");
    
    private static ExceptionMessageServer server;
    private ExecutorService threadsPoolException = Executors.newFixedThreadPool(1);
    private volatile boolean isRun = false;
    
    private ExceptionMessageServer() {
    
    }
    
    public static synchronized void startExceptionMessageServer() throws Exception {
    
        if (server != null)
            return;
        server = new ExceptionMessageServer();
        try {
            server.start();
        } catch (Exception ex) {
            LOG.error("ExceptionMessageServer start error: " + ex.getMessage());
            throw ex;
        }
        LOG.info("============ ExceptionMessageServer Is Running. ============");
    }
    
    public static synchronized void stopExceptionMessageServer() throws Exception {
    
        if (server == null)
            return;
        try {
            server.stop();
            server = null;
        } catch (Exception ex) {
            LOG.error("ExceptionMessageServer stop error: " + ex.getMessage());
            throw ex;
        }
        LOG.info("============ ExceptionMessageServer Has stopped. ============");
    }
    
    private void start() throws Exception {
    
        isRun = true;
        threadsPoolException.execute(new ExceptionMessageThread());
    }
    
    private void stop() throws Exception {
    
        if (null != server) {
            isRun = false;
            threadsPoolException.shutdown();
        }
    }
    
    class ExceptionMessageThread implements Runnable{
        
        public ExceptionMessageThread() {
        
        }
        
        public void run() {
        
            while (isRun) {
                try {
                    ReceiverExceptionData data = (ReceiverExceptionData) ExceptionMessageUtil.takeException();
                    logException.info(data.toString());
                } catch (Throwable t) {
                    LOG.error("ExceptionMessageThread err:", t);
                }
            }
        }
    }
    
}
