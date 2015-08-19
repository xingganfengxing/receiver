package com.letvcloud.cdn.log.main;

import com.letvcloud.cdn.log.manager.KafkaManager;
import com.letvcloud.cdn.log.manager.VodLogCollectServer;
import com.letvcloud.cdn.log.manager.VodLogConsumeServer;
import com.letvcloud.cdn.log.util.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * app入口
 * Created by liufeng1 on 2015/1/4.
 */
public class CollectMain {

    private static Logger LOG = LoggerFactory.getLogger(CollectMain.class);

    public static ApplicationContext CTX =
            new ClassPathXmlApplicationContext("spring/applicationContext.xml");

    public static void main(String[] args) throws Exception {

        VodLogCollectServer.startServer();
        VodLogConsumeServer.startServer();
        JedisUtil.initKey();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                LOG.info("start to kill vodcollect");
                VodLogCollectServer.stopServer();
                VodLogConsumeServer.stopServer();
                KafkaManager.close();
                JedisUtil.JEDIS_POOL.close();
            }
        });
    }
}
