package com.letv.cdn.receiver.servlet;

import com.letv.cdn.common.Env;
import com.letv.cdn.receiver.manager.KafkaProcessManager;
import com.letv.cdn.receiver.servlet.support.AbstractLiveRttpServlet;

/**
 * r2h发出的空piece数量
 * 
 * @author kk
 * 
 */
public class RttpEmptyServlet extends AbstractLiveRttpServlet{
    
    private static final String RTTP_EMPTY_TOPIC = Env.get("liveRttpEmptyTopic");
    
    @Override
    public void addStormData(Object param) throws Exception {
    
//        KafkaProcessManager.getRttpMsgById((Math.abs((int) sequence.next() % KAFKA_POOL_SIZE_FOR_LIVE_RTTP)))
//                .sendObjectKafka(param.toString(), RTTP_EMPTY_TOPIC);
    }
    
}
