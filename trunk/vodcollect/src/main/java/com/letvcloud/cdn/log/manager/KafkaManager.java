package com.letvcloud.cdn.log.manager;

import com.letvcloud.cdn.log.util.Constants;
import com.letvcloud.cdn.log.util.KafkaUtil;

/**
 * KafKa集中管理器
 * Created by liufeng1 on 2014/12/26.
 */
public class KafkaManager {

    public static final KafkaUtil[] KAFKA_UTILS = new KafkaUtil[Constants.VODLOG_CONSUME_THREAD_NUM];

    static {
        for (int i = 0; i < KAFKA_UTILS.length; i++) {
            KAFKA_UTILS[i] = new KafkaUtil();
        }
    }

    public static void close() {
        for (KafkaUtil ku : KAFKA_UTILS) {
            ku.close();
        }
    }
}
