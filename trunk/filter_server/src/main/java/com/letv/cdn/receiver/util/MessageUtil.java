package com.letv.cdn.receiver.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息缓存
 * 
 * @author kk
 */
public class MessageUtil{
    
    private static BlockingQueue queuePersistence = new LinkedBlockingQueue(5000);
   // private static BlockingQueue queueMessage = new LinkedBlockingQueue(1000);
    
    public static void addPersistence(Object obj) {
    
        queuePersistence.add(obj);
    }
    
    public static Object takePersistence() throws InterruptedException {
    
        return queuePersistence.take();
    }
    
    public static int getPersistenceSize() {
    
        return queuePersistence.size();
    }
    
//    public static void addMessage(Object obj) {
//    
//        queueMessage.add(obj);
//    }
//    
//    public static Object takeMessage() throws InterruptedException {
//    
//        return queueMessage.take();
//    }
//    
//    public static int getMessageSize() {
//    
//        return queueMessage.size();
//    }
}
