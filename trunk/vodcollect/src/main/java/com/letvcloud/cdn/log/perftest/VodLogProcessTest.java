package com.letvcloud.cdn.log.perftest;

import com.letvcloud.cdn.log.manager.KafkaManager;
import com.letvcloud.cdn.log.manager.VodLogConsumeServer;
import com.letvcloud.cdn.log.service.LogProcess;
import com.letvcloud.cdn.log.util.JedisUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by liufeng1 on 2015/2/28.
 */
public class VodLogProcessTest {

    public static void main(String[] args) throws IOException {
        VodLogConsumeServer.startServer();
        JedisUtil.initKey();
        String body = FileUtils.readFileToString(new File("F://test.txt"));
        LogProcess.process("test.txt", body, 1);
        VodLogConsumeServer.stopServer();
        KafkaManager.close();
        JedisUtil.JEDIS_POOL.close();
    }
}
