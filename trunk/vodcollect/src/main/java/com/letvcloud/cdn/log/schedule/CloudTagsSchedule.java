package com.letvcloud.cdn.log.schedule;

import com.letvcloud.cdn.log.util.CloudTagsUtil;
import com.letvcloud.cdn.log.util.Env;
import com.letvcloud.cdn.log.util.JedisUtil;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * Created by feng on 2015/5/10.
 */
@Component
public class CloudTagsSchedule {
    private static Logger LOG = LoggerFactory.getLogger(CloudTagsSchedule.class);

    @Scheduled(cron="${cloudTagsCron}")
    public void getCloudTags() {
        Jedis jedis = JedisUtil.JEDIS_POOL.getResource();
        String cloudTags = jedis.get(Env.get("redisCloudTagsKey"));
        CloudTagsUtil.CLOUD_TAGS = cloudTags;
        JedisUtil.JEDIS_POOL.returnBrokenResource(jedis);
        LOG.info("reset cloudTags {} success",cloudTags);
    }
}
