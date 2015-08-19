package com.letv.cdn.receiver.service;

import com.letv.cdn.common.Env;
import com.letv.cdn.receiver.model.RtmpdfnData;
import com.letv.cdn.receiver.util.LogReportConstants;

/**
 * @author liufeng1
 * @date 5/5/2015
 */
public class ServiceFactory {

    public static ILogReportService getServiceInstance(String type){

        if(LogReportConstants.TRMPDFN.equals(type)){
            String topic = Env.get("liveRtmpDfnTopic");
            return new ParamsToKafkaService(new RtmpdfnData(),topic);
        }

        if(LogReportConstants.RTMPACC.equals(type)){
            String topic = Env.get("liveRtmpAccTopic");
            return new LogToKafkaService(topic);
        }
        return null;
    }
}
