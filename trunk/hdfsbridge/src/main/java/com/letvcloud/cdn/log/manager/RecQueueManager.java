package com.letvcloud.cdn.log.manager;

import com.letvcloud.cdn.log.model.RecoverFileInfo;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 缓存接收到的文件管理器
 * Created by liufeng1 on 2014/12/30.
 */
public class RecQueueManager {

    //缓存接byte数组
    private static final LinkedBlockingQueue<RecoverFileInfo> REC_QUEUE = new LinkedBlockingQueue<>(30);

    //缓存未恢复的文件名
    private static final LinkedBlockingQueue<RecoverFileInfo> UNREC_QUEUE = new LinkedBlockingQueue<>(30);

    public static LinkedBlockingQueue<RecoverFileInfo> getRecQueue() {
        return REC_QUEUE;
    }

    public static LinkedBlockingQueue<RecoverFileInfo> getUnRecQueue() {
        return UNREC_QUEUE;
    }
}
