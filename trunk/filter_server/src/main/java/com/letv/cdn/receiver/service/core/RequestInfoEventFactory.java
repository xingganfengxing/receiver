package com.letv.cdn.receiver.service.core;

import com.letv.cdn.receiver.model.RequestInfo;
import com.lmax.disruptor.EventFactory;

/**
 * 请求信息实例工厂
 * 
 * @author kk
 */
public class RequestInfoEventFactory implements EventFactory{
    
    public Object newInstance() {

        return new RequestInfo();
    }
}
