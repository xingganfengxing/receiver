package com.letvcloud.cdn.log.util;

import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.List;

/**
 * 云视频用户Tag工具类
 * Created by liufeng1 on 2015/3/13.
 */
public class CloudTagsUtil {
    /**
     * 获取云视频用户Tags
     * @return
     */
    public static String CLOUD_TAGS = "";

    static {
        Jedis jedis = JedisUtil.JEDIS_POOL.getResource();
        CLOUD_TAGS = jedis.get(Env.get("redisCloudTagsKey"));
        JedisUtil.JEDIS_POOL.returnBrokenResource(jedis);
    }

    public static List<String> getCloudTags(){
        List<String> cloudTags = Arrays.asList(CLOUD_TAGS.split(","));
        return cloudTags;
    }
}
