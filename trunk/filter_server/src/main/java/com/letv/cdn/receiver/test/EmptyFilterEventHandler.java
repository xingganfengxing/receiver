package com.letv.cdn.receiver.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.receiver.manger.DisruptorServer;
import com.letv.cdn.receiver.manger.KafkaProcessManager;
import com.letv.cdn.receiver.model.RequestInfo;
import com.lmax.disruptor.EventHandler;

public class EmptyFilterEventHandler implements EventHandler<RequestInfo>{
    private static final Logger log = LoggerFactory.getLogger(EmptyFilterEventHandler.class);
    private final int ordinal; // 当前消费线程的编号
    
    public EmptyFilterEventHandler() {
    
        this(0);
    }
    
    public EmptyFilterEventHandler(Integer ordinal) {
    
        this.ordinal = ordinal;
    }
    
    public void onEvent(RequestInfo requestInfo, long sequence, boolean endOfBatch) throws Exception {
    
        if ((sequence % DisruptorServer.NUM_EVENT_HANDLERS) == ordinal) {
            // System.out.println("Filter next: " + sequence);
            String body = requestInfo.getBody();
            String[] bodyLine = body.split("\n");
            for (String line : bodyLine) {
                try {
                    KafkaProcessManager.getMessageProcessById(ordinal).sendObjectKafka(line);
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
            }
        }
    }
}
