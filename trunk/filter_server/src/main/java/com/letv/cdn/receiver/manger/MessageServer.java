package com.letv.cdn.receiver.manger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.common.Env;
import com.letv.cdn.common.StringUtil;
import com.letv.cdn.receiver.util.MessageUtil;

/**
 * 消息处理服务
 * 
 * @author kk
 */
public class MessageServer{
    
    private static final Logger log = LoggerFactory.getLogger(MessageServer.class);
    
    private static MessageServer server;
    private static final String MESSAGE_TOPIC = StringUtil.isEmpty(Env.get("messageTopic")) ? "messageTopic" : Env
            .get("messageTopic");
    private static final int NUM_PERSISTENCE_PROCESSORS = 6;//
    private static final int NUM_MESSAGE_PROCESSORS = StringUtil.isEmpty(Env.get("numSendProcessors")) ? Runtime
            .getRuntime().availableProcessors() : Integer.parseInt(Env.get("numSendProcessors"));
    // private ThreadPoolExecutor threadsPoolMessage;
    private ThreadPoolExecutor threadsPoolpersistence;
    private volatile boolean isRun = false;
    
    private MessageServer() {
    
        threadsPoolpersistence = new ThreadPoolExecutor(NUM_PERSISTENCE_PROCESSORS, NUM_PERSISTENCE_PROCESSORS, 10l,
                TimeUnit.SECONDS, new ArrayBlockingQueue(5));
        // threadsPoolMessage = new ThreadPoolExecutor(NUM_MESSAGE_PROCESSORS,
        // NUM_MESSAGE_PROCESSORS, 10l,
        // TimeUnit.SECONDS, new ArrayBlockingQueue(5));
    }
    
    public static synchronized void startMessageServer() throws Exception {
    
        if (server != null)
            return;
        server = new MessageServer();
        try {
            server.start();
        } catch (Exception ex) {
            log.error("MessageServer start error: " + ex.getMessage());
            throw ex;
        }
        log.info("============ MessageServer Is Running. ============");
    }
    
    public static synchronized void stopMessageServer() throws Exception {
    
        if (server == null)
            return;
        try {
            server.stop();
            server = null;
        } catch (Exception ex) {
            log.error("MessageServer stop error: " + ex.getMessage());
            throw ex;
        }
        log.info("============ MessageServer Has stopped. ============");
    }
    
    private void start() throws Exception {
    
        isRun = true;
        for (int i = 0; i < NUM_PERSISTENCE_PROCESSORS; i++) {
            threadsPoolpersistence.execute(new PersistenceThread());
        }
        // for (int i = 0; i < NUM_MESSAGE_PROCESSORS; i++) {
        // threadsPoolMessage.execute(new SendThread());
        // }
    }
    
    private void stop() throws Exception {
    
        if (null != server) {
            isRun = false;
            threadsPoolpersistence.shutdown();
            // threadsPoolMessage.shutdown();
        }
    }
    
    class PersistenceThread implements Runnable{
        
        public PersistenceThread() {
        
        }
        
        public void run() {
        
            while (isRun) {
                try {
                    KafkaProcessManager.getPersistenceProcessById(0).sendObjectKafka(MessageUtil.takePersistence());
                } catch (Throwable t) {
                    log.error("PersistenceThread err:", t);
                }
            }
        }
    }
    
    /*
     * class SendThread implements Runnable{ KafkaUtil k = new
     * KafkaUtil(MESSAGE_TOPIC); public SendThread() { } public void run() { try
     * { while (isRun) { try { k.sendObjectKafka(MessageUtil.takeMessage()); }
     * catch (Throwable t) { log.error("SendThread err:", t); } } } finally {
     * k.close(); } } }
     */
}
