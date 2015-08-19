package com.letv.cdn.receiver.util;

import com.letv.cdn.receiver.model.LogData;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 日志缓存管理器
 * Created by liufeng1 on 2014/12/30.
 */
public class BlockingQueueManager {

    private static final LinkedBlockingQueue<LogData> QUEUE = new LinkedBlockingQueue<LogData>(10000);

    public static LinkedBlockingQueue<LogData> getQueue() {
        return QUEUE;
    }
}
