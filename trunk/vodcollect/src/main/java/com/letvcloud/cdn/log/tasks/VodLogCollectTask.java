package com.letvcloud.cdn.log.tasks;

import com.letvcloud.cdn.log.manager.ReceFileQueueManager;
import com.letvcloud.cdn.log.manager.VodLogCollectServer;
import com.letvcloud.cdn.log.model.ReceFileElments;
import com.letvcloud.cdn.log.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.List;

/**
 * 日志收集任务
 * Created by liufeng1 on 2015/2/12.
 */
public class VodLogCollectTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(VodLogCollectTask.class);

    private String[] ports = Constants.ZMQ_SERVER_PORT.split(";");

    public void run() {

        while (VodLogCollectServer.isRun) {

            try {
                Jedis jedis = JedisUtil.JEDIS_POOL.getResource();

                // list[0]:key,list[1]:10.150.186.12:/letvlog/letv/cdn.log.gz
                List<String> list = jedis.blpop(
                        Constants.REDIS_TIMEOUT, Constants.REDIS_KEY_REQ);

                if (null != list && list.size() == 2) {

                    String redisRes = list.get(1);
                    String[] redisResArray = redisRes.split(":");

                    byte[] recBytes = getBytesFromZmq(redisResArray[0], redisResArray[1]);

                    if (null != recBytes) {

                        if ("NoFile".getBytes("UTF-8").length != recBytes.length) {

                            double fileSize = recBytes.length / 1024.0 / 1024;

                            ReceFileQueueManager.getQueue().put(
                                    new ReceFileElments(recBytes, redisRes, fileSize));

                            LOG.info("receive file:{},size:{}MB success,queue size {}",
                                    redisResArray[1], fileSize,
                                    ReceFileQueueManager.getQueue().size());

                            //保存从zmq收到的gzip文件
                            if (VodLogCollectServer.ENABLE_SAVEZMQFILES) {

                                String[] fileNameArrs = redisResArray[1].split("/");
                                String fileName = fileNameArrs[fileNameArrs.length - 1];
                                String yyyyMMdd = fileName.substring(0, fileName.indexOf(".") - 6);
                                String filePath = StringUtils.join(
                                        new String[]{
                                                Constants.ZMQ_FILEPATH,
                                                yyyyMMdd,
                                                fileName
                                        },"/");
                                FileUtils.writeByteArrayToFile(
                                        new File(filePath), recBytes);
                            }
                        } else {
                            LOG.error("request file {},but receive NoFile", redisRes);
                            jedis.rpush(Constants.REDIS_KEY_REQ_ERR, redisRes);
                        }
                    } else {
                        LOG.error("get file:{} error,rerpush to redis key:{}", redisRes,
                                Constants.REDIS_KEY_REQ_ERR);
                        jedis.rpush(Constants.REDIS_KEY_REQ_ERR, redisRes);
                    }
                }
                JedisUtil.JEDIS_POOL.returnResource(jedis);
            } catch (Exception e) {
                LOG.error(e.getLocalizedMessage());
            }
        }
    }

    /**
     * 从zmq获取bytes
     *
     * @param ip
     * @param filepath
     * @return
     * @throws InterruptedException
     */
    private byte[] getBytesFromZmq(String ip, String filepath) throws InterruptedException {

        byte[] recBytes = null;
        int retryTimes = 3;

        for (int i = 0; i < retryTimes; i++) {
            String port = ports[ArrayIndexUtil.getRandomPortIndex(ports.length)];
            String zmqUrl = "tcp://" + ip + ":" + port;

            //在zmq connect send recv之前先判断zmqurl是否有效
            //当某个zmqurl 失效的时候，当前thread会死在这里
            if (NetUtil.isIpPortAlive(ip, Integer.parseInt(port))) {

                StopWatch stopWatch = new StopWatch();
                stopWatch.start();

                recBytes = ZmqUtil.sendRec(filepath, zmqUrl);

                LOG.info("zmq connect send recv cost {}ms", stopWatch.getTime());

            } else {

                LOG.error("zmq connect net error, try sleep 1s");
                Thread.sleep(1000);
            }

            if (null != recBytes) {
                break;
            } else {
                LOG.error("get file:{} error,ip:{},port:{}", filepath, ip, port);
            }
        }
        return recBytes;
    }
}
