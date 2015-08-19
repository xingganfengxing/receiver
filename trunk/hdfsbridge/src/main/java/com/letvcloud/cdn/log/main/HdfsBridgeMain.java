package com.letvcloud.cdn.log.main;

import com.letvcloud.cdn.log.model.Commands;
import com.letvcloud.cdn.log.service.GetRtmpAccLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * hdfs文件交互统一入口
 * @author liufeng1
 * @date 3/6/2015
 */
public class HdfsBridgeMain {

    private static Logger LOG = LoggerFactory.getLogger(HdfsBridgeMain.class);

    public static void main(String[] args) {

        if (args.length < 1) {
            LOG.error("args miss ");
            return;
        }
        AbstractApplicationContext ctx =
                new ClassPathXmlApplicationContext("spring/applicationContext.xml");

        //启动从HDFS文件系统中下载
        if (StringUtils.equals(Commands.GET_RTMPACCLOG.toString(), args[0])) {
            GetRtmpAccLogService schedule = (GetRtmpAccLogService)
                    ctx.getBean("getRtmpAccLogService");
            schedule.downloadFile();
        }
        ctx.close();
    }
}
