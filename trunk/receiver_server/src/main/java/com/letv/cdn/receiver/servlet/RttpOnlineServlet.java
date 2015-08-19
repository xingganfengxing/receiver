package com.letv.cdn.receiver.servlet;

import com.letv.cdn.common.Env;
import com.letv.cdn.receiver.manager.KafkaProcessManager;
import com.letv.cdn.receiver.servlet.support.AbstractLiveRttpServlet;

/**
 * rttp模块运行的上报,在线统计，周期上报
 * 
 * @author kk
 * 
 */
public class RttpOnlineServlet extends AbstractLiveRttpServlet{
    
    private static final String RTTP_ONLINE_TOPIC = Env.get("liveRttpOnlineTopic");
    
    @Override
    public void addStormData(Object param) throws Exception {
    
//        KafkaProcessManager.getRttpMsgById((Math.abs((int) sequence.next() % KAFKA_POOL_SIZE_FOR_LIVE_RTTP)))
//                .sendObjectKafka(param.toString(), RTTP_ONLINE_TOPIC);
    }
    
}
