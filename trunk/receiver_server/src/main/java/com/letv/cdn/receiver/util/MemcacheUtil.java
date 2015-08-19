package com.letv.cdn.receiver.util;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.common.Env;
import com.letv.cdn.common.StringUtil;

/**
 * Memcache Util
 * 
 * @author kk
 */
public class MemcacheUtil{
    
    private static final Logger LOG = LoggerFactory.getLogger(MemcacheUtil.class);
    private static final String MEMCACHE_SERVER_IP;
    private static final int MEMCACHE_SERVER_PORT;
    public static final int MEMCACHED_POOL_SIZE;
    private static final String MEMCACHE_SERVER_USERNAME;
    private static final String MEMCACHE_SERVER_PASSWORD;
    // public static final int MEMCACHE_RETRY_MAX;
    // public static final int MEMCACHED_EXP_DEFAULT;
    public static final int MEMCACHED_TIMEOUT_DEFAULT = 5;
    public static final TimeUnit MEMCACHED_TIMEUNIT_DEFAULT = TimeUnit.SECONDS;
    static {
        MEMCACHE_SERVER_IP = StringUtil.isEmpty(Env.get("memcacheServerIp")) ? "127.0.0.1" : Env
                .get("memcacheServerIp");
        MEMCACHE_SERVER_PORT = StringUtil.isEmpty(Env.get("memcacheServerPort")) ? 11211 : Integer.parseInt(Env
                .get("memcacheServerPort"));
        MEMCACHE_SERVER_USERNAME = StringUtil.isEmpty(Env.get("memcacheServerUsername")) ? "" : Env
                .get("memcacheServerUsername");
        MEMCACHE_SERVER_PASSWORD = StringUtil.isEmpty(Env.get("memcacheServerPassword")) ? "" : Env
                .get("memcacheServerPassword");
        // MEMCACHE_RETRY_MAX = StringUtil.isEmpty(Env.get("memcacheRetryMax"))
        // ? 3 : Integer.parseInt(Env
        // .get("memcacheRetryMax"));
        // MEMCACHED_EXP_DEFAULT =
        // StringUtil.isEmpty(Env.get("memcachedExpDefault")) ? 0 :
        // Integer.parseInt(Env
        // .get("memcachedExpDefault"));
        MEMCACHED_POOL_SIZE = StringUtil.isEmpty(Env.get("memcachedPoolSize")) ? 1 : Integer.parseInt(Env
                .get("memcachedPoolSize"));
        LOG.info("memcacheServerIp:" + MEMCACHE_SERVER_IP + ",memcacheServerPort:" + MEMCACHE_SERVER_PORT
                + ",memcacheServerUsername:" + MEMCACHE_SERVER_USERNAME + ",memcacheServerPassword:"
                + MEMCACHE_SERVER_PASSWORD);
    }
    private static final MemcachedClient memClient = createClient();
    
    private static MemcachedClient createClient() {
    
        ConnectionFactoryBuilder connectionFactoryBuilder = new ConnectionFactoryBuilder();
        
        if (StringUtil.notEmpty(MEMCACHE_SERVER_USERNAME) && StringUtil.notEmpty(MEMCACHE_SERVER_PASSWORD)) {
            connectionFactoryBuilder.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY); // 指定使用Binary协议
            connectionFactoryBuilder.setOpTimeout(5000);
            AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" }, new PlainCallbackHandler(
                    MEMCACHE_SERVER_USERNAME, MEMCACHE_SERVER_PASSWORD));
            connectionFactoryBuilder.setAuthDescriptor(ad);
        }
        try {
            
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < MEMCACHED_POOL_SIZE; i++) {
                if (i > 0) {
                    buf.append(" ");
                }
                buf.append(MEMCACHE_SERVER_IP + ":" + MEMCACHE_SERVER_PORT);
            }
            return new MemcachedClient(connectionFactoryBuilder.build(), AddrUtil.getAddresses(buf.toString()));
        } catch (IOException e) {
            LOG.error("createClient err:", e);
        }
        return null;
    }
    
    public static boolean set(String key, Object value) throws Exception {
    
        Future<Boolean> f = memClient.set(key, 0, value);
        return getBooleanValue(f);
    }
    
    public static void asynSet(String key, Object value) throws Exception {
    
        memClient.set(key, 0, value);
    }
    
    public static boolean set(String key, int expire, Object value) throws Exception {
    
        Future<Boolean> f = memClient.set(key, expire, value);
        return getBooleanValue(f);
    }
    
    public static Object get(String key) {
    
        return memClient.get(key);
    }
    
    public static Object asyncGet(String key) throws Exception {
    
        Object obj = null;
        Future<Object> f = memClient.asyncGet(key);
        try {
            obj = f.get(MEMCACHED_TIMEOUT_DEFAULT, MEMCACHED_TIMEUNIT_DEFAULT);
        } catch (Exception e) {
            f.cancel(false);
            throw e;
        }
        return obj;
    }
    
    public static long increment(String key, int by, long defaultValue, int expire) {
    
        return memClient.incr(key, by, defaultValue, expire);
    }
    
    public static long asyncIncrement(String key, int by) throws Exception {
    
        Future<Long> f = memClient.asyncIncr(key, by);
        return getLongValue(f);
    }
    
    private static long getLongValue(Future<Long> f) throws Exception {
    
        try {
            Long l = f.get(MEMCACHED_TIMEOUT_DEFAULT, MEMCACHED_TIMEUNIT_DEFAULT);
            return l.longValue();
        } catch (Exception e) {
            f.cancel(false);
            throw e;
        }
    }
    
    private static boolean getBooleanValue(Future<Boolean> f) throws Exception {
    
        try {
            Boolean bool = f.get(MEMCACHED_TIMEOUT_DEFAULT, MEMCACHED_TIMEUNIT_DEFAULT);
            return bool.booleanValue();
        } catch (Exception e) {
            f.cancel(false);
            throw e;
        }
    }
    
    public static void disConnect() {
    
        if (memClient == null) {
            return;
        }
        memClient.shutdown();
    }
}
