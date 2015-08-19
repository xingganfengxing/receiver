package com.letvcloud.cdn.log.tasks;

import com.letvcloud.cdn.log.manager.ReceFileQueueManager;
import com.letvcloud.cdn.log.manager.VodLogConsumeServer;
import com.letvcloud.cdn.log.model.ReceFileElments;
import com.letvcloud.cdn.log.service.LogProcess;
import com.letvcloud.cdn.log.util.Constants;
import com.letvcloud.cdn.log.util.JedisUtil;
import com.letvcloud.cdn.log.util.StatisUtil;
import com.letvcloud.cdn.log.util.ZipUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.text.MessageFormat;

/**
 * 日志消费任务
 * Created by liufeng1 on 2015/2/12.
 */
public class VodLogConsumeTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(VodLogCollectTask.class);

    private int threadIndex = 0;//线程索引

    public VodLogConsumeTask(int threadIndex) {
        this.threadIndex = threadIndex;
    }

    public void run() {

        while (VodLogConsumeServer.isRun) {

            ReceFileElments receFileElment = null;

            try {
                receFileElment = ReceFileQueueManager.getQueue().take();

                String[] redisResArray = receFileElment.getOrginPath().split(":");
                String repKey = MessageFormat.format(Constants.REDIS_KEY_REP, redisResArray[0]);

                String body = ZipUtils.recureUnZip(receFileElment.getReceBytes(), 5);

                String[] filepathArr = redisResArray[1].split("/");

                //日志处理，split，过滤，入kafka
                LogProcess.process(filepathArr[filepathArr.length - 1], body, threadIndex);

                double unZipFileSize = 0;
                unZipFileSize = body.getBytes("utf-8").length / 1024.0 / 1024;

                LOG.info("consume file:{},size:{}MB,unZip size:{}MB success",
                        redisResArray[1], receFileElment.getFileSize(), unZipFileSize);

                Jedis jedis = JedisUtil.JEDIS_POOL.getResource();
                jedis.rpush(repKey, redisResArray[1]);//添加到删除队列
                if (Constants.ENABLE_STATIS) {
                    StatisUtil.statFileInfo(jedis, receFileElment.getFileSize());
                }
                JedisUtil.JEDIS_POOL.returnResource(jedis);

            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }
    }
}
