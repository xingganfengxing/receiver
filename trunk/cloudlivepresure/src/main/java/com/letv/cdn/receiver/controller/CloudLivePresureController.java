package com.letv.cdn.receiver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.letv.cdn.common.web.ResponseUtil;
import com.letv.cdn.receiver.controller.common.DefaultController;
import com.letv.cdn.receiver.model.LogData;
import com.letv.cdn.receiver.model.LogsJsonObj;
import com.letv.cdn.receiver.util.BlockingQueueManager;
import com.letv.cdn.receiver.util.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 云直播压力测试对接存储
 *
 * @author liufeng1
 */
@Controller
public class CloudLivePresureController extends DefaultController {

    private static Logger LOGGER = LoggerFactory.getLogger(CloudLivePresureController.class);

    @RequestMapping("/clpc")
    public void handleCloudLivePresure(HttpServletResponse rp, HttpServletRequest rq) throws IOException {
        InputStream is = rq.getInputStream();
        String logLines = StreamUtils.getBodyString(is);
        try {
            LogsJsonObj logsJsonObj = JSON.parseObject(logLines, LogsJsonObj.class);
            List<LogData> logDatas = logsJsonObj.getLogs();
            for (LogData logData : logDatas) {
                BlockingQueueManager.getQueue().put(logData);
            }
            ResponseUtil.sendMessageNoCache(rq, rp, "ok");
        } catch (Exception e) {
            LOGGER.error("res err json str:{}", logLines);
            ResponseUtil.sendMessageNoCache(rq, rp, "error");
            LOGGER.error(e.getMessage());
        }
    }
}
