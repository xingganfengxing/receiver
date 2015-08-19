package com.letv.cdn.receiver.servlet;

import com.letv.cdn.common.Env;
import com.letv.cdn.receiver.manager.KafkaProcessManager;
import com.letv.cdn.receiver.servlet.support.AbstractLiveRttpServlet;

/**
 * 与各种服务器连接的统计日志
 * 
 * @author kk
 * 
 */
public class RttpSvrServlet extends AbstractLiveRttpServlet{
    
    private static final String RTTP_SVR_TOPIC = Env.get("liveRttpSvrTopic");
    
    @Override
    public void addStormData(Object param) throws Exception {
    
//        KafkaProcessManager.getRttpMsgById((Math.abs((int) sequence.next() % KAFKA_POOL_SIZE_FOR_LIVE_RTTP)))
//                .sendObjectKafka(param.toString(), RTTP_SVR_TOPIC);
    }
    
}
