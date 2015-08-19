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
    
    private static final Logger log = LoggerFactory.getLogger(Constants.class);
    
    public static final int MEMCACHED_BUFFER_SIZE;
    public static final int INDEX_MASK;
    public static final String DEPENDENCY_RECEIVER_SERVER_NAME;
    public static final String FILTER_SERVER_NAME;
    public static final int FILTER_SERVER_ID;
    public static final int CLUSTER_NUM;
    public static long LOCAL_SEQUENCE_CURR;
    public static final String FILTER_LOCAL_SEQUENCER_KEY_CURR_POSTFIX = "_filter_sequence_curr";
    public static final String RECEIVER_LOCAL_SEQUENCER_KEY_CURR_POSTFIX = "_sequence_curr";
    public static volatile long receiver_curr = -1L;
    // public static final String RECEIVER_LOCAL_SEQUENCER_KEY_FAIL_POSTFIX =
    // "_sequence_fail";
    public static final String FILTER_LOCAL_SEQUENCER_KEY_CURR;
    public static final String RECEIVER_LOCAL_SEQUENCER_KEY_CURR;
    // public static final String RECEIVER_LOCAL_SEQUENCER_KEY_FAIL;
    
    static {
        DEPENDENCY_RECEIVER_SERVER_NAME = StringUtil.isEmpty(Env.get("dependencyReceiverServerName")) ? "cdn-receiver"
                : Env.get("dependencyReceiverServerName");
        MEMCACHED_BUFFER_SIZE = StringUtil.isEmpty(Env.get("memcachedBufferSize")) ? 1024 : Integer.parseInt(Env
                .get("memcachedBufferSize"));
        INDEX_MASK = MEMCACHED_BUFFER_SIZE - 1;
        FILTER_SERVER_NAME = StringUtil.isEmpty(Env.get("filterServerName")) ? "cdn-filter_0" : Env
                .get("filterServerName");
        // FILTER_SERVER_NAME = "cdn-filter";
        // RECEIVER_LOCAL_SEQUENCER_KEY_FAIL = DEPENDENCY_RECEIVER_SERVER_NAME +
        // RECEIVER_LOCAL_SEQUENCER_KEY_FAIL_POSTFIX;
        FILTER_LOCAL_SEQUENCER_KEY_CURR = FILTER_SERVER_NAME + FILTER_LOCAL_SEQUENCER_KEY_CURR_POSTFIX;
        RECEIVER_LOCAL_SEQUENCER_KEY_CURR = DEPENDENCY_RECEIVER_SERVER_NAME + RECEIVER_LOCAL_SEQUENCER_KEY_CURR_POSTFIX;
        // MemcachedClient memcachedClient = MemcacheUtil.createClient();
        try {
            Object str = XMemcacheUtil.getFromCache(FILTER_LOCAL_SEQUENCER_KEY_CURR);
            if (null == str) {
                LOCAL_SEQUENCE_CURR = -1;
                XMemcacheUtil.saveToCache(Constants.FILTER_LOCAL_SEQUENCER_KEY_CURR, 0, LOCAL_SEQUENCE_CURR);// 初始化
            } else {
                LOCAL_SEQUENCE_CURR = Long.parseLong(str.toString());
            }
        } catch (Exception e) {
            log.error("init Constants err:", e);
        }
        // memcachedClient.shutdown();
        FILTER_SERVER_ID = Integer.parseInt(Env.get("filterServerId"));
        CLUSTER_NUM = Integer.parseInt(Env.get("clusterNum"));
        log.info("dependencyReceiverServerName:" + DEPENDENCY_RECEIVER_SERVER_NAME + ",filterServerName:"
                + FILTER_SERVER_NAME + ",filterLocalSequencerKeyCurr:" + FILTER_LOCAL_SEQUENCER_KEY_CURR
                + ",localSequenceCurr:" + LOCAL_SEQUENCE_CURR + ",receiverLocalSequencerKeyCurr:"
                + RECEIVER_LOCAL_SEQUENCER_KEY_CURR + ",filterServerId:" + FILTER_SERVER_ID + ",clusterNum:"
                + CLUSTER_NUM);
    }
    
}
