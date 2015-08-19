package com.letv.cdn.receiver.servlet;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.common.Env;
import com.letv.cdn.receiver.exception.ValidateException;
import com.letv.cdn.receiver.manager.KafkaProcessManager;
import com.letv.cdn.receiver.model.NodeMapping;
import com.letv.cdn.receiver.servlet.support.AbstractBaseLiveMultipleServlet;
import com.letv.cdn.receiver.util.XMemcacheUtil;

/**
 * 直播flv服务端入口
 * 
 * @author kk
 */
public class FlvSSServlet extends AbstractBaseLiveMultipleServlet{
    
    private static final Logger LOG = LoggerFactory.getLogger(FlvSSServlet.class);
    
    private static final String FLV_TOPIC = Env.get("liveFlvServerTopic");
    
    private static enum PARAM {
        timestamp, streamid, bw, cip, sip, uuid
    };
    
    private static final String KEY_PREFIX = "flv";
    private static final String intranetTag = "999";
    private static final PARAM[] params = PARAM.values();
    
    @Override
    public boolean validateParams(Map<String, String> paramMap) throws ValidateException {
    
        if (null == paramMap || paramMap.size() < 6) {
            throw new ValidateException("args miss");
        }
        for (int i = 0; i < params.length; i++) {
            if (StringUtils.isEmpty(paramMap.get(params[i].toString()))) {
                throw new ValidateException(params[i].toString() + " miss");
            }
        }
        return true;
    }
    
    @Override
    public String[] splitParams(String str) throws Exception {
    
        return str.split("\n");
        
    }
    
    @Override
    public void handleParams(Map<String, String> param) throws Exception {
    
        // 默认内网
        String pid = intranetTag;
        String type = "out";
        
        String slogtime = param.get(PARAM.timestamp.toString()); // 时间戳
        param.put(PARAM.timestamp.toString() + "_bak", slogtime);
        String clientip = param.get(PARAM.cip.toString()); // 客户端IP
        String serverip = param.get(PARAM.sip.toString()); // 服务端IP
        if (NodeMapping.SPMapping.containsKey(clientip)) {// 如果可以映射成功，代表是外网
            type = "in";
            if (NodeMapping.SPMapping.containsKey(serverip)) {
                pid = NodeMapping.SPMapping.get(serverip);
            } else {
                pid = "0";
            }
        }
        param.put(PARAM.timestamp.toString(), slogtime);
        param.put("pid", pid);
        // 带宽计算
        long flow = Long.valueOf(param.get(PARAM.bw.toString()));// 一分钟内的流量合
        long bw = flow * 8 / 60;
        param.put(PARAM.bw.toString(), String.valueOf(bw));
        // 码率截取
        String streamid = param.get(PARAM.streamid.toString());
        String sp = "-1";
        int index = streamid.lastIndexOf("_");
        if (-1 != index) {
            sp = streamid.substring(index + 1);
        }
        param.put("sp", sp);
        // 其他字段，为了storm统一
        param.put("type", type);
    }
    
    @Override
    public void addCacheData(Object param) throws Exception {
    
        Map<String, String> paramMap = (Map<String, String>) param;
        String pid = paramMap.get("pid");
        String keu_prefix = KEY_PREFIX + "_" + paramMap.get(PARAM.streamid.toString()) + "_"
                + paramMap.get(PARAM.timestamp.toString());
        
        // 内网日志不会总到全国
        if (!StringUtils.equals(pid, intranetTag)) {
            String onlineKeyQ = keu_prefix + "_Q_online"; // 全国汇总
            long r2 = XMemcacheUtil.incrementToCache(onlineKeyQ, 1, 1, DEFAULT_EXPIRE_TIME);
            
            int bw = Math.round(Float.parseFloat(paramMap.get(PARAM.bw.toString())));
            String bwKeyQ = keu_prefix + "_Q_bw";
            long r4 = XMemcacheUtil.incrementToCache(bwKeyQ, bw, bw, DEFAULT_EXPIRE_TIME);
        }
    }
    
    @Override
    public void addStormData(Object param) throws Exception {
    
        Map<String, String> paramMap = (Map<String, String>) param;
        String pid = paramMap.get("pid");
        KafkaProcessManager
                .getServerMsgProcessById((Math.abs((int) sequence.next() % KAFKA_POOL_SIZE_FOR_LIVE_SERVER)))
                .sendObjectKafka(preprosBodyString(paramMap, pid), FLV_TOPIC);
    }
    
    @Override
    public String getFormat() {
    
        return "yyyyMMddHHmm";
    }
    
    private String preprosBodyString(Map<String, String> paramMap, String pid) {
    
        StringBuilder result = new StringBuilder();
        result.append(paramMap.get(PARAM.timestamp.toString())).append("\t");
        result.append(paramMap.get(PARAM.streamid.toString())).append("\t");
        result.append(paramMap.get("sp")).append("\t");
        String bw = null;
        try {
            bw = String.valueOf(Math.round(Float.parseFloat(paramMap.get(PARAM.bw.toString()))));
        } catch (NumberFormatException e) {
            bw = paramMap.get(PARAM.bw.toString());
        }
        result.append(bw).append("\t");
        result.append(paramMap.get(PARAM.sip.toString())).append("\t");
        result.append(paramMap.get(PARAM.cip.toString())).append("\t");
        result.append(paramMap.get("type")).append("\t");
        result.append(paramMap.get(PARAM.uuid.toString())).append("\t");
        result.append(pid);
        LOG.debug(result.toString());
        return result.toString();
    }
}
