package com.letv.cdn.receiver.model;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.receiver.util.XMemcacheUtil;

public class VodBandwidthMap{
    
    private static final Logger LOG = LoggerFactory.getLogger(VodBandwidthMap.class);
    
    private static final ConcurrentMap<String, Integer> bwSum = new ConcurrentHashMap<String, Integer>(2000);
    
    public static void incrementBw(String q, int bw) {
    
        Integer oldVal, newVal;
        do {
            oldVal = bwSum.putIfAbsent(q, bw);
            if (null == oldVal) {
                break;
            }
            newVal = oldVal + bw;
        } while (!bwSum.replace(q, oldVal, newVal));
    }
    
    public static void pushBw() {
    
        synchronized (bwSum) {
            String key;
            int value;
            for (Entry<String, Integer> e : bwSum.entrySet()) {
                key = e.getKey();
                value = e.getValue().intValue();
                try {
                    long r = XMemcacheUtil.incrementToCache(key, value, value, 35 * 60 * 1000);
                    LOG.info("bw cache : {}", key + ":" + r + "," + value);
                } catch (Exception ex) {
                    LOG.error("pushBw err:", ex);
                }
            }
            int s = bwSum.size();
            bwSum.clear();
            LOG.info("bwSum clear:" + s);
        }
    }
}
