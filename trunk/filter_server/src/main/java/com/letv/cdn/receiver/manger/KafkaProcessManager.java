package com.letv.cdn.receiver.manger;

import java.util.List;

import com.letv.cdn.common.Env;
import com.letv.cdn.common.StringUtil;
import com.letv.cdn.receiver.util.KafkaUtil;

/**
 * Kafka连接缓存
 * 
 * @author kk
 */
public class KafkaProcessManager{
    
    private static KafkaUtil[] kafkaArrPersistence = new KafkaUtil[DisruptorServer.NUM_EVENT_HANDLERS + 1];
    private static KafkaUtil[] kafkaArrMessage = new KafkaUtil[DisruptorServer.NUM_EVENT_HANDLERS];
    
    static {
        kafkaArrPersistence[0] = new KafkaUtil(StringUtil.isEmpty(Env.get("persistenceTopic")) ? "persistenceTopic"
                : Env.get("persistenceTopic"));
        for (int j = 1; j <= DisruptorServer.NUM_EVENT_HANDLERS; j++) {
            kafkaArrPersistence[j] = new KafkaUtil(StringUtil.isEmpty(Env.get("persistenceTopic")) ? "persistenceTopic"
                    : Env.get("persistenceTopic"));
        }
        for (int i = 0; i < DisruptorServer.NUM_EVENT_HANDLERS; i++) {
            kafkaArrMessage[i] = new KafkaUtil(StringUtil.isEmpty(Env.get("messageTopic")) ? "messageTopic"
                    : Env.get("messageTopic"));
        }
    }
    
    public static List getAllProcess() {
    
        // return java.util.Arrays.asList(kafkaArrMessage, kafkaArrPersistence);
        return null;
    }
    
    public static KafkaUtil getPersistenceProcessById(int index) {
    
        return kafkaArrPersistence[index];
    }
    
    public static KafkaUtil getMessageProcessById(int index) {
    
        return kafkaArrMessage[index];
    }
    
    public static void closeAllProcess() {
    
        for (KafkaUtil k : kafkaArrPersistence) {
            k.close();
        }
        for (KafkaUtil k : kafkaArrMessage) {
            k.close();
        }
    }
    
}
