package com.letvcloud.cdn.log.util;

import redis.clients.jedis.Jedis;

/**
 * 收发统计工具类
 * Created by liufeng1 on 2014/12/24.
 */
public class StatisUtil {

    /**
     * 每日文件数累计
     */
    public final static String DAY_FILECOUNT_KEY = Env.get("dayFileCountKey");

    /**
     * 每日接收到的文件大小统计
     */
    public final static String DAY_FILESIZE_KEY = Env.get("dayFileSizeKey");

    /**
     * 近一小时内收到的文件数统计
     */
    public final static String HOUR_FILECOUNT_KEY = Env.get("hourFileCountKey");

    /**
     * 近一小时内收到的文件大小统计
     */
    public final static String HOUR_FILESIZE_KEY = Env.get("hourFileSizeKey");

    /**
     * 近一分钟内收到的文件数统计
     */
    public final static String MINUTE_FILECOUNT_KEY = Env.get("minuteFileCountKey");

    /**
     * 近一分钟内收到的文件大小统计
     */
    public final static String MINUTE_FILESIZE_KEY = Env.get("minuteFileSizeKey");

    /**
     * 每日处理日志行数
     */
    public final static String DAY_LOGCOUNT_KEY = Env.get("dayLogCountKey");

    /**
     * 每小时处理日志行数
     */
    public final static String HOUR_LOGCOUNT_KEY = Env.get("hourLogCountKey");

    /**
     * 每分钟处理日志行数
     */
    public final static String MINUTE_LOGCOUNT_KEY = Env.get("minuteLogCountKey");

    /**
     * 每日处理日志大小
     */
    public final static String DAY_LOGSIZE_KEY = Env.get("dayLogSizeKey");

    /**
     * 每小时处理日志大小
     */
    public final static String HOUR_LOGSIZE_KEY = Env.get("hourLogSizeKey");

    /**
     * 每分钟处理日志大小
     */
    public final static String MINUTE_LOGSIZE_KEY = Env.get("minuteLogSizeKey");

    /**
     * 每日过滤掉的日志行数
     */
    public final static String DAY_LOGFAIL_COUNT_KEY = Env.get("dayLogFailCountKey");

    /**
     * 每小时过滤掉的日志行数
     */
    public final static String HOUR_LOGFAIL_COUNT_KEY = Env.get("hourLogFailCountKey");

    /**
     * 每分钟过滤掉的日志行数
     */
    public final static String MINUTE_LOGFAIL_COUNT_KEY = Env.get("minuteLogFailCountKey");

    public final static String DAY_DETAIL_KEY = Env.get("dayDetailKey");

    public final static String HOUR_DETAIL_KEY = Env.get("hourDetailKey");

    public final static String MINUTE_DETAIL_KEY = Env.get("minuteDetailKey");

    /**
     * 统计接收到的文件信息
     *
     * @param jedis
     * @param fileSize
     */
    public static void statFileInfo(Jedis jedis, double fileSize) {
        jedis.incr(DAY_FILECOUNT_KEY);
        jedis.incr(HOUR_FILECOUNT_KEY);
        jedis.incr(MINUTE_FILECOUNT_KEY);
        jedis.incrByFloat(DAY_FILESIZE_KEY, fileSize);
        jedis.incrByFloat(HOUR_FILESIZE_KEY, fileSize);
        jedis.incrByFloat(MINUTE_FILESIZE_KEY, fileSize);
    }

    /**
     * 统计处理的日志信息
     *
     * @param jedis
     * @param logCount
     */
    public static void statLogInfo(Jedis jedis, long logCount, long failCount, double logSize) {
        jedis.incrBy(DAY_LOGCOUNT_KEY, logCount);
        jedis.incrBy(HOUR_LOGCOUNT_KEY, logCount);
        jedis.incrBy(MINUTE_LOGCOUNT_KEY, logCount);

        jedis.incrBy(DAY_LOGFAIL_COUNT_KEY, failCount);
        jedis.incrBy(HOUR_LOGFAIL_COUNT_KEY, failCount);
        jedis.incrBy(MINUTE_LOGFAIL_COUNT_KEY, failCount);

        jedis.incrByFloat(DAY_LOGSIZE_KEY, logSize);
        jedis.incrByFloat(HOUR_LOGSIZE_KEY, logSize);
        jedis.incrByFloat(MINUTE_LOGSIZE_KEY, logSize);
    }

    public static void resetDayStatis(Jedis jedis) {
        jedis.set(DAY_FILECOUNT_KEY, "0");
        jedis.set(DAY_FILESIZE_KEY, "0");
        jedis.set(DAY_LOGCOUNT_KEY, "0");
        jedis.set(DAY_LOGSIZE_KEY, "0");
        jedis.set(DAY_LOGFAIL_COUNT_KEY, "0");
    }

    public static void resetHourStatis(Jedis jedis) {
        jedis.set(HOUR_FILECOUNT_KEY, "0");
        jedis.set(HOUR_FILESIZE_KEY, "0");
        jedis.set(HOUR_LOGCOUNT_KEY, "0");
        jedis.set(HOUR_LOGSIZE_KEY, "0");
        jedis.set(HOUR_LOGFAIL_COUNT_KEY, "0");
    }

    public static void resetMinuteStatis(Jedis jedis) {
        jedis.set(MINUTE_FILECOUNT_KEY, "0");
        jedis.set(MINUTE_FILESIZE_KEY, "0");
        jedis.set(MINUTE_LOGCOUNT_KEY, "0");
        jedis.set(MINUTE_LOGSIZE_KEY, "0");
        jedis.set(MINUTE_LOGFAIL_COUNT_KEY, "0");
    }

    /**
     * 发送到kafka相关统计
     *
     * @param succCount     解析成功条数
     * @param failCount     解析失败条数
     * @param sendKafkaSize 发送的日志大小
     */
    public static void statLogInfo(int succCount,int failCount, double sendKafkaSize) {
        Jedis jedis = JedisUtil.JEDIS_POOL.getResource();
        if (Constants.ENABLE_STATIS) {
            double logSize = sendKafkaSize / 1024 / 1024;
            StatisUtil.statLogInfo(jedis, succCount, failCount, logSize);
        }
        JedisUtil.JEDIS_POOL.returnResource(jedis);
    }
}
