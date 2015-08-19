package com.letv.cdn.receiver.servlet;

import com.letv.cdn.common.Env;
import com.letv.cdn.receiver.manager.KafkaProcessManager;
import com.letv.cdn.receiver.servlet.support.AbstractLiveRttpServlet;

/**
 * rttp卡顿比指标
 * 
 * @author kk
 * 
 */
public class RttpKaServlet extends AbstractLiveRttpServlet{
    
    private static final String RTTP_KA_TOPIC = Env.get("liveRttpKaTopic");
    
    @Override
    public void addStormData(Object param) throws Exception {
    
//        KafkaProcessManager.getRttpMsgById((Math.abs((int) sequence.next() % KAFKA_POOL_SIZE_FOR_LIVE_RTTP)))
//                .sendObjectKafka(param.toString(), RTTP_KA_TOPIC);
    }
    
}
