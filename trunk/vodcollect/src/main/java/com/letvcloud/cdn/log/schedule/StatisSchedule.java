package com.letvcloud.cdn.log.schedule;

import com.letvcloud.cdn.log.manager.ReceFileQueueManager;
import com.letvcloud.cdn.log.util.Constants;
import com.letvcloud.cdn.log.util.JedisUtil;
import com.letvcloud.cdn.log.util.StatisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 重置Redis统计信息的定时任务
 * Created by liufeng1 on 2014/12/24.
 */
@Component
public class StatisSchedule {

    private static Logger LOG = LoggerFactory.getLogger(StatisSchedule.class);

    @Scheduled(cron="${statisDayCron}")
    public void statisDay(){
        if(Constants.ENABLE_SCHEDULE){
            Jedis jedis = JedisUtil.JEDIS_POOL.getResource();
            try {
                int dayFileCount = Integer.parseInt(jedis.get(StatisUtil.DAY_FILECOUNT_KEY));
                double dayFileSize = Double.parseDouble(jedis.get(StatisUtil.DAY_FILESIZE_KEY));
                long dayLogCount = Long.parseLong(jedis.get(StatisUtil.DAY_LOGCOUNT_KEY));
                long dayLogFailCount = Long.parseLong(jedis.get(StatisUtil.DAY_LOGFAIL_COUNT_KEY));
                double dayLogSize = Double.parseDouble(jedis.get(StatisUtil.DAY_LOGSIZE_KEY));
                double succRate = dayLogCount * 1.0 / (dayLogCount + dayLogFailCount) * 100;

                LOG.info("day_file_count:{},day_file_size:{}MB,day_log_count:{}," +
                                "day_log_fail_count:{},succ_rate:{}%,day_log_size:{}MB",
                        dayFileCount,
                        String.format("%.2f", dayFileSize),
                        dayLogCount,
                        dayLogFailCount,
                        String.format("%.2f", succRate),
                        String.format("%.2f", dayLogSize)
                );
                String key = new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                        + "_" + StatisUtil.DAY_DETAIL_KEY;

                jedis.rpush(key,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                                + " : dayFileCount->" + dayFileCount
                                + ",dayFileSize->" + String.format("%.2f", dayFileSize) + "MB"
                                + ",dayLogCount->" + dayLogCount
                                + ",dayLogFailCount->" + dayLogFailCount
                                + ",dayLogSuccRate->" + String.format("%.2f", succRate) + "%"
                                + ",dayLogSize->" + String.format("%.2f", dayLogSize) + "MB");
                StatisUtil.resetDayStatis(jedis);

                //清空前一天的统计
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -1);
                String preDayStr = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

                String dayDetailKey = preDayStr + "_" + StatisUtil.DAY_DETAIL_KEY;
                String hourDetailKey = preDayStr + "_" + StatisUtil.HOUR_DETAIL_KEY;
                String minuteDetailKey = preDayStr + "_" + StatisUtil.MINUTE_DETAIL_KEY;
                jedis.del(dayDetailKey);
                jedis.del(hourDetailKey);
                jedis.del(minuteDetailKey);

                LOG.info("StatisSchedule resetDayStatis success ");
            } catch (Exception e) {
                LOG.error(e.getMessage());
            } finally {
                JedisUtil.JEDIS_POOL.returnResource(jedis);
            }
        }
    }

    @Scheduled(cron="${statisHourCron}")
    public void statisHour(){
        if(Constants.ENABLE_SCHEDULE) {
            Jedis jedis = JedisUtil.JEDIS_POOL.getResource();
            try {
                int hourFileCount = Integer.parseInt(jedis.get(StatisUtil.HOUR_FILECOUNT_KEY));
                double hourFileSize = Double.parseDouble(jedis.get(StatisUtil.HOUR_FILESIZE_KEY));
                long hourLogCount = Long.parseLong(jedis.get(StatisUtil.HOUR_LOGCOUNT_KEY));
                long hourLogFailCount = Long.parseLong(jedis.get(StatisUtil.HOUR_LOGFAIL_COUNT_KEY));
                double hourLogSize = Double.parseDouble(jedis.get(StatisUtil.HOUR_LOGSIZE_KEY));
                double succRate = hourLogCount * 1.0 / (hourLogCount + hourLogFailCount) * 100;

                LOG.info("hour_file_count:{},hour_file_size:{}MB,hour_log_count:{}," +
                                "hour_log_fail_count:{},succ_rate:{}%,hour_log_size:{}MB",
                        hourFileCount,
                        String.format("%.2f", hourFileSize),
                        hourLogCount,
                        hourLogFailCount,
                        String.format("%.2f", succRate),
                        String.format("%.2f", hourLogSize)
                );
                String key = new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                        + "_" + StatisUtil.HOUR_DETAIL_KEY;
                jedis.rpush(key,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                                + " : hourFileCount->" + hourFileCount
                                + ",hourFileSize->" + String.format("%.2f", hourFileSize) + "MB"
                                + ",hourLogCount->" + hourLogCount
                                + ",hourLogFailCount->" + hourLogFailCount
                                + ",hourLogSize->" + String.format("%.2f", hourLogSize) + "MB");
                StatisUtil.resetHourStatis(jedis);
                LOG.info("StatisSchedule resetHourStatis success ");
            } catch (Exception e) {
                LOG.error(e.getMessage());
            } finally {
                JedisUtil.JEDIS_POOL.returnResource(jedis);
            }
        }
    }

    @Scheduled(cron="${statisMinuteCron}")
    public void statisMinute() {
        if (Constants.ENABLE_SCHEDULE) {
            Jedis jedis = JedisUtil.JEDIS_POOL.getResource();
            try {
                long remoteLen = jedis.llen(Constants.REDIS_KEY_REQ);
                int localLen = ReceFileQueueManager.getQueue().size();
                int minuteFileCount = Integer.parseInt(jedis.get(StatisUtil.MINUTE_FILECOUNT_KEY));
                double minuteFileSize = Double.parseDouble(jedis.get(StatisUtil.MINUTE_FILESIZE_KEY));
                long minuteLogCount = Long.parseLong(jedis.get(StatisUtil.MINUTE_LOGCOUNT_KEY));
                long minuteLogFailCount = Long.parseLong(jedis.get(StatisUtil.MINUTE_LOGFAIL_COUNT_KEY));
                double minuteLogSize = Double.parseDouble(jedis.get(StatisUtil.MINUTE_LOGSIZE_KEY));
                double succRate = minuteLogCount * 1.0 / (minuteLogCount + minuteLogFailCount) * 100;

                LOG.info(
                        "minute_file_count:{}," +
                                "minute_file_size:{}MB," +
                                "minute_log_count:{}," +
                                "minute_log_fail_count:{}," +
                                "minute_log_succRate:{}%," +
                                "minute_log_size:{}MB," +
                                "jedis file len {},ReceFileQueue size {}",
                        minuteFileCount,
                        String.format("%.2f", minuteFileSize),
                        minuteLogCount,
                        minuteLogFailCount,
                        String.format("%.2f", succRate),
                        String.format("%.2f", minuteLogSize),
                        remoteLen,
                        localLen
                );
                String key = new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                        + "_" + StatisUtil.MINUTE_DETAIL_KEY;

                jedis.rpush(key,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                                + " : minuteFileCount->" + minuteFileCount
                                + ",secondLogCount->" + minuteLogCount / 60
                                + ",secondLogFailCount->" + minuteLogFailCount / 60
                                + ",secondLogSuccRate->" + String.format("%.2f", succRate) + "%"
                                + ",secondLogSize->" + String.format("%.2f", minuteLogSize / 60) + "MB"
                                + ",minuteFileSize->" + String.format("%.2f", minuteFileSize) + "MB"
                                + ",redisQueueLen->" + jedis.llen(Constants.REDIS_KEY_REQ));
                StatisUtil.resetMinuteStatis(jedis);
                LOG.info("StatisSchedule resetMinuteStatis success ");
            } catch (Exception e) {
                LOG.error(e.getMessage());
            } finally {
                JedisUtil.JEDIS_POOL.returnResource(jedis);
            }
        }
    }
}
