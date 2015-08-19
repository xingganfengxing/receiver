package com.letv.cdn.receiver.service;

import com.letv.cdn.receiver.manager.KafkaProcessManager;
import com.letv.cdn.receiver.model.DataResponse;
import com.letv.cdn.receiver.model.ILogData;
import com.letv.cdn.receiver.servlet.support.AbstractBaseLiveMultipleServlet;
import com.letv.cdn.receiver.util.KafkaUtil;
import com.letv.cdn.receiver.util.LogReportConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 带参数的kafka写入ParamsToKafkaService
 *
 * @author liufeng1
 * @date 5/5/2015
 */
public class ParamsToKafkaService extends BaseService implements ILogReportService {

    private static Logger LOG = LoggerFactory.getLogger(ParamsToKafkaService.class);

    private ILogData logData;
    private String topic;

    public ParamsToKafkaService(ILogData logData, String topic){
        this.logData = logData;
        this.topic = topic;
    }

    @Override
    public DataResponse handle(String reqBody) {

        KafkaUtil kafkaUtil =  KafkaProcessManager.getServerMsgProcessById(
                (Math.abs((int) sequence.next() %
                        AbstractBaseLiveMultipleServlet.KAFKA_POOL_SIZE_FOR_LIVE_SERVER))
        );

        String[] lines = reqBody.split("\n");
        DataResponse dataResponse = new DataResponse();
        for (String line : lines) {
            Map map = getParameterMap(line);
            if (logData.mapToILogData(map)) {
                kafkaUtil.sendObjectKafka(logData.toLine(), topic);
                dataResponse.setResult(LogReportConstants.REP_TRUE);
            } else {
                dataResponse.setResult(LogReportConstants.REP_FALSE);
                dataResponse.setMessage(reqBody);
                LOG.error("lack params,reqBody: {}", reqBody);
            }
        }

        return dataResponse;
    }
}
