package com.letv.cdn.receiver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.common.Env;
import com.letv.cdn.common.StringUtil;

/**
 * 全局常量
 * 
 * @author kk
 */
public class Constants{
    
    private static final Logger LOG = LoggerFactory.getLogger(Constants.class);
    
    public static final int MEMCACHED_BUFFER_SIZE;
    public static final int INDEX_MASK;
    // public static final int MEMCACHED_POOL_SIZE;
    
    // public static final int LIVE_SERVER_MEMCACHED_THREAD_MAX;
    // public static final int LIVE_SERVER_MEMCACHED_THREAD_CORE;
    // public static final int LIVE_SERVER_MEMCACHED_QUEUE_SIZE;
    //
    // public static final int LIVE_SERVER_KAFKA_THREAD_MAX;
    // public static final int LIVE_SERVER_KAFKA_THREAD_CORE;
    // public static final int LIVE_SERVER_KAFKA_QUEUE_SIZE;
    
    public static final String RECEIVER_SERVER_NAME;
    public static final String FILTER_SERVER_NAME = "cdn-filter_";
    public static final String RESULT_SUCCESS = "true";
    public static final String RESULT_FAILED = "false";
    // public static long LOCAL_SEQUENCE_CURR;
    public static volatile long filter_curr = -1L;
    public static volatile long receiver_curr = -1L;
    public static final String RECEIVER_LOCAL_SEQUENCER_KEY_CURR_POSTFIX = "_sequence_curr";
    public static final String FILTER_LOCAL_SEQUENCER_KEY_CURR_POSTFIX = "_filter_sequence_curr";
    public static final String RECEIVER_LOCAL_SEQUENCER_KEY_CURR;
    
    static {
        RECEIVER_SERVER_NAME = StringUtil.isEmpty(Env.get("receiverServerName")) ? "cdn-receiver" : Env
                .get("receiverServerName");
        // LIVE_SERVER_MEMCACHED_THREAD_MAX =
        // StringUtils.isEmpty(Env.get("liveServerMemcachedThreadMax")) ?
        // Runtime
        // .getRuntime().availableProcessors() * 2 :
        // Integer.parseInt(Env.get("liveServerMemcachedThreadMax"));
        // LIVE_SERVER_MEMCACHED_QUEUE_SIZE =
        // StringUtils.isEmpty(Env.get("liveServerMemcachedQueueSize")) ? 5000
        // : Integer.parseInt(Env.get("liveServerMemcachedQueueSize"));
        // LIVE_SERVER_KAFKA_THREAD_CORE =
        // StringUtils.isEmpty(Env.get("liveServerWorkerThreadCore")) ? Runtime
        // .getRuntime().availableProcessors() * 2 :
        // Integer.parseInt(Env.get("liveServerWorkerThreadCore"));
        
        MEMCACHED_BUFFER_SIZE = StringUtil.isEmpty(Env.get("memcachedBufferSize")) ? 1024 : Integer.parseInt(Env
                .get("memcachedBufferSize"));
        INDEX_MASK = MEMCACHED_BUFFER_SIZE - 1;
        RECEIVER_LOCAL_SEQUENCER_KEY_CURR = RECEIVER_SERVER_NAME + RECEIVER_LOCAL_SEQUENCER_KEY_CURR_POSTFIX;
        
        try {
            Object str = XMemcacheUtil.getFromCache(RECEIVER_LOCAL_SEQUENCER_KEY_CURR);
            if (null == str) {
                XMemcacheUtil.saveToCache(Constants.RECEIVER_LOCAL_SEQUENCER_KEY_CURR, 0, receiver_curr);
            } else {
                receiver_curr = Long.parseLong(str.toString());
            }
        } catch (Exception e) {
            LOG.error("init Constants err:", e);
        }
        // memcachedClient.shutdown();
        LOG.info("receiverServerName:" + RECEIVER_SERVER_NAME + ",receiverLocalSequencerKeyCurr:"
                + RECEIVER_LOCAL_SEQUENCER_KEY_CURR + ",localSequenceCurr:" + receiver_curr);
    }
    
}
