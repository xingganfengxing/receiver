package com.letv.cdn.receiver.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU缓存
 * 
 * @author kk
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V>{
    
    private final int MAX_CACHE_SIZE;
    
    public LRUCache(int cacheSize, boolean accessOrder) {
    
        super((int) Math.ceil(cacheSize / 0.75) + 1, 0.75f, accessOrder);
        MAX_CACHE_SIZE = cacheSize;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
    
        return size() > MAX_CACHE_SIZE;
    }
    
    @Override
    public String toString() {
    
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : entrySet()) {
            sb.append(String.format("%s:%s ", entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }
    
}
