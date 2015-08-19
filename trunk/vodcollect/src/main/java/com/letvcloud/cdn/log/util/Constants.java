package com.letvcloud.cdn.log.util;

/**
 * Created by liufeng1 on 2014/12/24.
 */
public interface Constants {

    //redis 配置
    String REDIS_SERVER_IP = Env.get("redisServerIp");

    int REDIS_SERVER_PORT = Integer.parseInt(Env.get("redisServerPort"));

    int REDIS_TIMEOUT = Integer.parseInt(Env.get("redisTimeOut"));

    /**
     * zmq请求key
     */
    String REDIS_KEY_REQ = Env.get("rediskey_req");

    /**
     * zmq响应key
     */
    String REDIS_KEY_REP = Env.get("rediskey_rep");

    /**
     * 请求错误统计 key
     */
    String REDIS_KEY_REQ_ERR = Env.get("rediskey_req_err");

    //zmq 配置
    String ZMQ_SERVER_PORT = Env.get("zmqServerPort");

    /**
     * 是否添加统计模块
     */
    boolean ENABLE_STATIS = Boolean.parseBoolean(Env.get("enableStatis"));

    /**
     * 是否执行定时任务
     */
    boolean ENABLE_SCHEDULE = Boolean.parseBoolean(Env.get("enableSchedule"));

    /**
     * 收集线程数目
     */
    int VODLOG_COLLECT_THREAD_NUM = Integer.parseInt(Env.get("vodLogCollectThreadNum"));

    /**
     * 消费线程数目
     */
    int VODLOG_CONSUME_THREAD_NUM = Integer.parseInt(Env.get("vodLogConsumeThreadNum"));

    /**
     * 缓存接收到的文件管理器容量限制
     */
    int RECE_FILE_QUEUE_MAXSIZE = Integer.parseInt(Env.get("receFileQueueMaxSize"));

    /**
     * Kafka ip port配置
     */
    String KAFKA_SERVER_IP = Env.get("kafkaServerIp");

    /**
     * 本地缓存文件路径
     */
    String LOCAL_FILEPATH = Env.get("localfilepath");

    /**
     * ZMQ接收到的gzip压缩文件存储路径
     */
    String ZMQ_FILEPATH = Env.get("zmqfilepath");
}
