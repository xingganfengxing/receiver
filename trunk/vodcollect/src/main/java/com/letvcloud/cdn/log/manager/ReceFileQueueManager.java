package com.letvcloud.cdn.log.manager;

import java.util.concurrent.LinkedBlockingQueue;

import com.letvcloud.cdn.log.model.ReceFileElments;
import com.letvcloud.cdn.log.util.Constants;

/**
 * 缓存接收到的文件管理器
 * Created by liufeng1 on 2014/12/30.
 */
public class ReceFileQueueManager {

    //缓存接收到的文件
    private static final LinkedBlockingQueue<ReceFileElments> QUEUE = new LinkedBlockingQueue<>(Constants.RECE_FILE_QUEUE_MAXSIZE);

    public static LinkedBlockingQueue<ReceFileElments> getQueue() {
        return QUEUE;
    }
}
