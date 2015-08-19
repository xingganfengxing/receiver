package com.letv.cdn.receiver.manager;

import java.util.List;

import com.letv.cdn.receiver.servlet.support.AbstractBaseLiveMultipleServlet;
import com.letv.cdn.receiver.util.KafkaUtil;

/**
 * Kafka连接缓存
 * 
 * @author sunyan
 */
public class KafkaProcessManager{
    
    // private static KafkaUtil[] kafkaArrLiveRtmpServer = new
    // KafkaUtil[AbstractLiveRtmpServlet.KAFKA_POOL_SIZE_FOR_LIVE_RTMP_SERVER];
    private static KafkaUtil[] kafkaArrLiveServer = new KafkaUtil[AbstractBaseLiveMultipleServlet.KAFKA_POOL_SIZE_FOR_LIVE_SERVER];
    // private static KafkaUtil[] kafkaArrLiveRtmpClient = new
    // KafkaUtil[AbstractLiveRtmpServlet.KAFKA_POOL_SIZE_FOR_LIVE_RTMP_CLIENT];
    // private static KafkaUtil[] kafkaArrLiveRttpClient = new
    // KafkaUtil[AbstractLiveRttpServlet.KAFKA_POOL_SIZE_FOR_LIVE_RTTP];
    
    static {
        for (int j = 0; j < kafkaArrLiveServer.length; j++) {
            kafkaArrLiveServer[j] = new KafkaUtil(KafkaUtil.ASYNC_TYPE);
        }
        // for (int j = 0; j < kafkaArrLiveRtmpServer.length; j++) {
        // kafkaArrLiveRtmpServer[j] = new KafkaUtil(
        // StringUtil.isEmpty(Env.get("liveServerTopic")) ? "liveServerTopic" :
        // Env.get("liveServerTopic"),
        // KafkaUtil.SYNC_TYPE);
        // }
        // for (int i = 0; i < kafkaArrLiveRtmpClient.length; i++) {
        // kafkaArrLiveRtmpClient[i] = new KafkaUtil(
        // StringUtil.isEmpty(Env.get("liveClientTopic")) ? "liveClientTopic" :
        // Env.get("liveClientTopic"),
        // KafkaUtil.SYNC_TYPE);
        // }
        // for (int i = 0; i < kafkaArrLiveRttpClient.length; i++) {
        // kafkaArrLiveRttpClient[i] = new KafkaUtil(KafkaUtil.SYNC_TYPE);
        // }
    }
    
    public static KafkaUtil getServerMsgProcessById(int index) {
    
        return kafkaArrLiveServer[index];
    }
    
    // public static KafkaUtil getClientMsgById(int index) {
    //
    // return kafkaArrLiveRtmpClient[index];
    // }
    //
    // public static KafkaUtil getRttpMsgById(int index) {
    //
    // return kafkaArrLiveRttpClient[index];
    // }
    
    public static List getAllProcess() {
    
        // return java.util.Arrays.asList(kafkaArrMessage, kafkaArrPersistence);
        return null;
    }
    
    public static void closeAllProcess() {
    
        for (KafkaUtil k : kafkaArrLiveServer) {
            k.close();
        }
        // for (KafkaUtil k : kafkaArrLiveRtmpClient) {
        // k.close();
        // }
        // for (KafkaUtil k : kafkaArrLiveRttpClient) {
        // k.close();
        // }
    }
    
}
