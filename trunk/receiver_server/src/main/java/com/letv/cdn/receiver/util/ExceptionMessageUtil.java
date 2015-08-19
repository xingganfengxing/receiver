package com.letv.cdn.receiver.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 异常消息缓存
 * 
 * @author kk
 */
public class ExceptionMessageUtil{
    
    private static BlockingQueue queueException = new LinkedBlockingQueue(5000);
    
    // private static BlockingQueue queueMessage = new
    // LinkedBlockingQueue(1000);
    
    public static boolean addqueueException(Object obj) {
    
        return queueException.offer(obj);
    }
    
    public static Object takeException() throws InterruptedException {
    
        return queueException.take();
    }
    
    public static int getExceptionSize() {
    
        return queueException.size();
    }
}
