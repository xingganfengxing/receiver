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
 * 直播客户端入口 Created by liujs on 14-11-6.
 * 
 * @modify by sunyan 直播服务器上报servlet
 */
public class RtmpSSServlet extends AbstractBaseLiveMultipleServlet{
    
    private static final Logger LOG = LoggerFactory.getLogger(RtmpSSServlet.class);
    private static final String RTMP_TOPIC = Env.get("liveRtmpServerTopic");
    private static final String intranetTag = "999";
    private static final String KEY_PREFIX = "rtmp";
    
    private static enum PARAM {
        timestamp, streamid, sp, bw, cip, sip, type, uuid
    };
    
    private static final PARAM[] params = PARAM.values();
    
    // private static final SequenceUtil sequence = new SequenceUtil();
    
    @Override
    public boolean validateParams(Map<String, String> paramMap) throws ValidateException {
    
        if (null == paramMap || paramMap.size() < 8) {
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
    public void handleParams(Map<String, String> paramMap) {
    
        String pid = null;
        String slogtime = paramMap.get(PARAM.timestamp.toString()); // 时间戳
        paramMap.put(PARAM.timestamp.toString() + "_bak", slogtime);
        String serverip = paramMap.get(PARAM.sip.toString()); // 服务端IP
        // 内网无需映射省份ID，外网需要
        if (StringUtils.equals(paramMap.get(PARAM.type.toString()), "in")) {
            pid = intranetTag;
        } else {
            if (NodeMapping.SPMapping.containsKey(serverip)) {
                pid = NodeMapping.SPMapping.get(serverip);
            } else {
                pid = "0";
            }
        }
        paramMap.put(PARAM.timestamp.toString(), slogtime.substring(0, slogtime.length() - 2)); // 时间戳精确到分
        paramMap.put("pid", pid);
    }
    
    @Override
    public void addCacheData(Object param) throws Exception {
    
        Map<String, String> paramMap = (Map<String, String>) param;
        String pid = paramMap.get("pid");
        String keu_prefix = KEY_PREFIX + "_" + paramMap.get(PARAM.streamid.toString()) + "_"
                + paramMap.get(PARAM.timestamp.toString());
        // 统计在线人数 参数：key 递增值 默认值 有效期
        // String onlineKey = keu_prefix + "_" + pid + "_online"; // 分省汇总
        // long r1 = XMemcacheUtil.incrementToCache(onlineKey, 1, 1,
        // Constants.DEFAULT_EXPIRE_TIME);
        // LOG.debug("online cache : {}", onlineKey + ":" + r1);
        //
        // // 统计带宽 TODO 45ru
        // String bwKey = keu_prefix + "_" + pid + "_bw";
        // int bw =
        // Math.round(Float.parseFloat(paramMap.get(PARAM.bw.toString())));
        // long r3 = XMemcacheUtil.incrementToCache(bwKey, bw, bw,
        // Constants.DEFAULT_EXPIRE_TIME);
        // LOG.debug("bw cache : {}", bwKey + ":" + r3);
        
        // 内网日志不会总到全国
        if (!StringUtils.equals(pid, intranetTag)) {
            
            String onlineKeyQ = keu_prefix + "_Q_online"; // 全国汇总
            long r2 = XMemcacheUtil.incrementToCache(onlineKeyQ, 1, 1, DEFAULT_EXPIRE_TIME);
            LOG.info("onlineQ cache : {}", onlineKeyQ + ":" + r2 + ",cip: " + paramMap.get(PARAM.cip.toString()));
            
            int bw = Math.round(Float.parseFloat(paramMap.get(PARAM.bw.toString())));
            String bwKeyQ = keu_prefix + "_Q_bw";
            long r4 = XMemcacheUtil.incrementToCache(bwKeyQ, bw, bw, DEFAULT_EXPIRE_TIME);
            LOG.info("bwQ cache : {}", bwKeyQ + ":" + r4);
        }
    }
    
    @Override
    public void addStormData(Object param) {
    
        Map<String, String> paramMap = (Map<String, String>) param;
        String pid = paramMap.get("pid");
        KafkaProcessManager.getServerMsgProcessById((int) (sequence.next() % KAFKA_POOL_SIZE_FOR_LIVE_SERVER))
                .sendObjectKafka(preprosBodyString(paramMap, pid), RTMP_TOPIC);
    }
    
    private String preprosBodyString(Map<String, String> paramMap, String pid) {
    
        StringBuilder result = new StringBuilder();
        result.append(paramMap.get(PARAM.timestamp.toString())).append("\t");
        result.append(paramMap.get(PARAM.streamid.toString())).append("\t");
        result.append(paramMap.get(PARAM.sp.toString())).append("\t");
        String bw = null;
        try {
            bw = String.valueOf(Math.round(Float.parseFloat(paramMap.get(PARAM.bw.toString()))));
        } catch (NumberFormatException e) {
            bw = paramMap.get(PARAM.bw.toString());
        }
        result.append(bw).append("\t");
        result.append(paramMap.get(PARAM.sip.toString())).append("\t");
        result.append(paramMap.get(PARAM.cip.toString())).append("\t");
        result.append(paramMap.get(PARAM.type.toString())).append("\t");
        result.append(paramMap.get(PARAM.uuid.toString())).append("\t");
        result.append(pid);
        LOG.debug(result.toString());
        return result.toString();
    }
    
    public static void main(String[] args) {
    
        System.out.println(LoggerFactory.getLogger(RtmpSSServlet.class));
    }
    
}
