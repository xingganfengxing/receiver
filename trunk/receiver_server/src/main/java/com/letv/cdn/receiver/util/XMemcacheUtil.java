package com.letv.cdn.receiver.util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.yanf4j.core.impl.StandardSocketOption;
import com.letv.cdn.common.Env;
import com.letv.cdn.common.StringUtil;

public class XMemcacheUtil{
    
    private static final Logger LOG = LoggerFactory.getLogger(XMemcacheUtil.class);
    private static final String MEMCACHE_SERVER_IP;
    private static final int MEMCACHE_SERVER_PORT;
    public static final int MEMCACHED_POOL_SIZE;
    public static final int MEMCACHED_TIMEOUT_DEFAULT = 5000;
    static {
        MEMCACHE_SERVER_IP = StringUtil.isEmpty(Env.get("memcacheServerIp")) ? "127.0.0.1" : Env
                .get("memcacheServerIp");
        MEMCACHE_SERVER_PORT = StringUtil.isEmpty(Env.get("memcacheServerPort")) ? 11211 : Integer.parseInt(Env
                .get("memcacheServerPort"));
        MEMCACHED_POOL_SIZE = StringUtil.isEmpty(Env.get("memcachedPoolSize")) ? 2 : Integer.parseInt(Env
                .get("memcachedPoolSize"));
        LOG.info("memcacheServerIp:" + MEMCACHE_SERVER_IP + ",memcacheServerPort:" + MEMCACHE_SERVER_PORT
                + ",memcachedPoolSize:" + MEMCACHED_POOL_SIZE + ",memcachedTimeoutDefault:" + MEMCACHED_TIMEOUT_DEFAULT);
    }
    
    private static final MemcachedClient memcachedClient = createClient();
    
    private static MemcachedClient createClient() {
    
        try {
            MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(MEMCACHE_SERVER_IP + ":"
                    + MEMCACHE_SERVER_PORT));
            builder.setCommandFactory(new BinaryCommandFactory());
            builder.setSocketOption(StandardSocketOption.SO_RCVBUF, 32 * 1024);// 设置接收缓存区为32K，默认16K
            builder.setSocketOption(StandardSocketOption.SO_SNDBUF, 16 * 1024); // 设置发送缓冲区为16K，默认为8K
            builder.setSocketOption(StandardSocketOption.TCP_NODELAY, false); // 启用nagle算法，提高吞吐量，默认关闭
            builder.setConnectionPoolSize(MEMCACHED_POOL_SIZE);
            return builder.build();
        } catch (IOException e) {
            LOG.error("createClient err:", e);
            return null;
        }
    }
    
    public static boolean saveToCache(String key, Object value) throws TimeoutException, InterruptedException,
            MemcachedException {
    
        return memcachedClient.set(key, 0, value);
    }
    
    public static boolean saveToCache(String key, int expire, Object value) throws TimeoutException,
            InterruptedException, MemcachedException {
    
        return memcachedClient.set(key, expire, value);
    }
    
    public static long incrementToCache(String key, int by, long defaultValue, int expire) throws TimeoutException,
            InterruptedException, MemcachedException {
    
        return memcachedClient.incr(key, by, defaultValue, MEMCACHED_TIMEOUT_DEFAULT, expire);
    }
    
    public static void flushAll() throws TimeoutException, InterruptedException, MemcachedException {
    
        memcachedClient.flushAll();
    }
    
    public static Object getFromCache(String key) throws TimeoutException, InterruptedException, MemcachedException {
    
        return memcachedClient.get(key);
    }
    
    public static void disConnect() throws IOException {
    
        if (memcachedClient == null) {
            return;
        }
        memcachedClient.shutdown();
    }
}
