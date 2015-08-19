package com.letv.cdn.receiver.service;

import com.letv.cdn.receiver.manager.KafkaProcessManager;
import com.letv.cdn.receiver.model.DataResponse;
import com.letv.cdn.receiver.servlet.support.AbstractBaseLiveMultipleServlet;
import com.letv.cdn.receiver.util.KafkaUtil;
import com.letv.cdn.receiver.util.LogReportConstants;

/**
 * 原始日志入kafka
 *
 * @author liufeng1
 * @date 5/5/2015
 */
public class LogToKafkaService extends BaseService implements ILogReportService {

    private String topic;

    public LogToKafkaService(String topic) {
        this.topic = topic;
    }

    @Override
    public DataResponse handle(String reqBody) {

        KafkaUtil kafkaUtil = KafkaProcessManager.getServerMsgProcessById(
                (Math.abs((int) sequence.next() %
                        AbstractBaseLiveMultipleServlet.KAFKA_POOL_SIZE_FOR_LIVE_SERVER))
        );
        DataResponse dataResponse = new DataResponse();
        dataResponse.setResult(LogReportConstants.REP_TRUE);
        dataResponse.setMessage("success");
        kafkaUtil.sendObjectKafka(reqBody, topic);
        return dataResponse;
    }
}
