package com.letvcloud.cdn.log.schedule;

import com.letvcloud.cdn.log.util.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 本地缓存文件上传到kafka定时任务
 * Created by liufeng1 on 2015/1/27.
 */
@Component
public class Local2KafkaSchedule {

    private static Logger LOG = LoggerFactory.getLogger(Local2KafkaSchedule.class);

    @Scheduled(cron="${local2KafkaCron}")
    public void local2Kafka() {

        LOG.info("Local2KafkaSchedule running");

        if (NetUtil.isKafkaAlive(Constants.KAFKA_SERVER_IP)) {
            KafkaUtil[] kafkaUtils = new KafkaUtil[10];
            for (int i = 0; i < kafkaUtils.length; i++) {
                kafkaUtils[i] = new KafkaUtil();
            }
            try {
                File localDir = new File(Constants.LOCAL_FILEPATH);
                File[] files = localDir.listFiles();
                for (File theFile : files) {
                    long fileLastModified = theFile.lastModified();
                    long msPlus = System.currentTimeMillis() - fileLastModified;

                    if (msPlus / 1000 / 60 > 5) {//5分钟前的文件可以发送到kafka
                        String[] lines = FileUtils.readFileToString(theFile).split("\n");
                        for (String line : lines) {
                            int index = ArrayIndexUtil.getRandomPortIndex(kafkaUtils.length);
                            kafkaUtils[index].withTopic(Env.get("messageTopic")).sendObjectKafka(line);
                        }
                        FileUtils.deleteQuietly(theFile);
                        LOG.info("Local2KafkaSchedule resend file:{} to kafka success",theFile.getName());
                    }
                }
            } catch (Exception e) {
                LOG.error(e.getMessage());
            } finally {
                for (int i = 0; i < kafkaUtils.length; i++) {
                    kafkaUtils[i].close();
                }
            }
        } else {
            LOG.error("kafka server is not alive");
        }
        LOG.info("Local2KafkaSchedule ok");
    }
}
