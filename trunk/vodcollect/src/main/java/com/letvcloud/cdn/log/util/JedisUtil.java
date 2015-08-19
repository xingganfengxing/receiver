package com.letvcloud.cdn.log.util;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 初始化JedisKey
 * Created by liufeng1 on 2014/12/24.
 */
public class JedisUtil {


    private static JedisPoolConfig CONFIG = initConfig();

    public static final JedisPool JEDIS_POOL = new JedisPool(
            CONFIG, Constants.REDIS_SERVER_IP, Constants.REDIS_SERVER_PORT);

    private static JedisPoolConfig initConfig() {

        JedisPoolConfig config = new JedisPoolConfig();

        //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        config.setBlockWhenExhausted(true);

        //设置的逐出策略类名, 默认DefaultEvictionPolicy
        // (当连接超过最大空闲时间,或连接数超过最大空闲连接数)
        config.setJmxEnabled(true);

        //MBean ObjectName = new ObjectName("org.apache.commons.pool2:
        // type=GenericObjectPool,name=" + "pool" + i);
        config.setJmxNamePrefix("pool");

        //是否启用后进先出, 默认true
        config.setLifo(true);

        //最大空闲连接数, 默认8个
        config.setMaxIdle(8);

        //最大连接数, 默认8个
        config.setMaxTotal(30);

        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),
        // 如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        config.setMaxWaitMillis(-1);

        //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        config.setMinEvictableIdleTimeMillis(1800000);

        //最小空闲连接数, 默认0
        config.setMinIdle(0);

        //每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        config.setNumTestsPerEvictionRun(3);

        //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,
        // 不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
        config.setSoftMinEvictableIdleTimeMillis(1800000);

        //在获取连接的时候检查有效性, 默认false
        config.setTestOnBorrow(false);

        //在空闲时检查有效性, 默认false
        config.setTestWhileIdle(false);

        //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        config.setTimeBetweenEvictionRunsMillis(-1);
        return config;
    }

    /**
     * 初始化key
     */
    public static void initKey() {

        Jedis jedis = JEDIS_POOL.getResource();

        String[] keys = new String[]{
                "dayFileCountKey",
                "dayFileSizeKey",
                "hourFileCountKey",
                "hourFileSizeKey",
                "minuteFileCountKey",
                "minuteFileSizeKey",
                "dayLogCountKey",
                "hourLogCountKey",
                "minuteLogCountKey",
                "dayLogSizeKey",
                "hourLogSizeKey",
                "minuteLogSizeKey",
                "dayLogFailCountKey",
                "hourLogFailCountKey",
                "minuteLogFailCountKey",
                "redisCloudTagsKey"
        };

        for (String key : keys) {
            if (StringUtils.isEmpty(jedis.get(Env.get(key)))) {
                jedis.set(Env.get(key), "0");
            }
        }

        JEDIS_POOL.returnResource(jedis);
    }

    public static long getRedisKeyLen(String key) {

        Jedis jedis = JedisUtil.JEDIS_POOL.getResource();
        long queueLen = jedis.llen(key);
        JedisUtil.JEDIS_POOL.returnBrokenResource(jedis);
        return queueLen;
    }
}
