package com.letv.cdn.receiver.servlet;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.receiver.exception.ValidateException;
import com.letv.cdn.receiver.manager.KafkaProcessManager;
import com.letv.cdn.receiver.servlet.support.AbstractLiveRtmpServlet;
import com.letv.cdn.receiver.util.SequenceUtil;
import com.letv.cdn.receiver.util.XMemcacheUtil;

/**
 * 直播rtmp客户端入口
 * 
 * @author kk
 */
public class RtmpCSServlet extends AbstractLiveRtmpServlet{
    
    private static final Logger LOG = LoggerFactory.getLogger(RtmpCSServlet.class);
    
    private static enum PARAM {
        ac, streamid, sp, err, cdnbits, p2pbits, st, pt, ht, ver, uuid, r, geo
    };
    
    private static final String KEY_PREFIX = "client";
    private static final PARAM[] params = PARAM.values();
    private static final SequenceUtil sequence = new SequenceUtil();
    
    @Override
    public boolean validateParams(Map<String, String> paramMap) throws ValidateException {
    
        if (null == paramMap || paramMap.size() < 13) {
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
    
        return new String[] { str };
        
    }
    
    @Override
    public void handleParams(Map<String, String> paramMap) {
    
        // 构造时间戳
        String slogtime = "";
        slogtime = threadLocal.get().format(new Date()).toString();
        paramMap.put("timestamp" + "_bak", slogtime);
        paramMap.put("timestamp", slogtime.substring(0, slogtime.length() - 2)); // 时间戳精确到分
        // 解析省份Id
        String ac = paramMap.get(PARAM.ac.toString());
        if (!"init".equals(ac)) {
            String geo = paramMap.get(PARAM.geo.toString());
            int index = geo.indexOf(".", 3);
            paramMap.put("pid", geo.substring(3, index));
        } else {
            paramMap.put("pid", "0");
        }
    }
    
    @Override
    public void addCacheData(Object param) throws Exception {
    
        Map<String, String> paramMap = (Map<String, String>) param;
        //String pid = paramMap.get("pid");
        String ac = paramMap.get(PARAM.ac.toString());
        if ("play".equals(ac) || "heart".equals(ac)) {
            String keu_prefix = KEY_PREFIX + "_" + paramMap.get(PARAM.streamid.toString()) + "_"
                    + paramMap.get("timestamp");
            // 统计在线人数 参数：key 递增值 默认值 有效期
            // String onlineKey = keu_prefix + "_" + pid + "_online"; // 分省汇总
            // long r1 = XMemcacheUtil.incrementToCache(onlineKey, 1, 1,
            // Constants.DEFAULT_EXPIRE_TIME);
            // LOG.debug("online cache : {}", onlineKey + ":" + r1);
            // 全国汇总
            String onlineKeyQ = keu_prefix + "_Q_online";
            long r2 = XMemcacheUtil.incrementToCache(onlineKeyQ, 1, 1, DEFAULT_EXPIRE_TIME);
            LOG.debug("onlineQ cache : {}", onlineKeyQ + ":" + r2);
        } else if ("buffer".equals(ac)) {// 统计卡顿
            String keu_prefix = KEY_PREFIX + "_" + paramMap.get(PARAM.streamid.toString()) + "_"
                    + paramMap.get("timestamp");
            // String slowKey = keu_prefix + "_" + pid + "_slow"; //
            // XMemcacheUtil.incrementToCache(slowKey, 1, 1,
            // Constants.DEFAULT_EXPIRE_TIME);
            // LOG.debug("slow cache : {}", slowKey);
            String slowKeyQ = keu_prefix + "_Q_slow";
            long r4 = XMemcacheUtil.incrementToCache(slowKeyQ, 1, 1, DEFAULT_EXPIRE_TIME);
            LOG.debug("slowQ cache : {}", slowKeyQ + ":" + r4);
        }
    }
    
    @Override
    public void addStormData(Object param) {
    
        Map<String, String> paramMap = (Map<String, String>) param;
        String pid = paramMap.get("pid");
//        KafkaProcessManager.getClientMsgById(Math.abs((int) (sequence.next() % KAFKA_POOL_SIZE_FOR_LIVE_RTMP_CLIENT)))
//                .sendObjectKafka(preprosBodyString(paramMap, pid));
    }
    
    private String preprosBodyString(Map<String, String> paramMap, String pid) {
    
        StringBuilder result = new StringBuilder();
        result.append(paramMap.get("timestamp")).append("\t");
        result.append(paramMap.get(PARAM.streamid.toString())).append("\t");
        result.append(paramMap.get(PARAM.sp.toString())).append("\t");
        result.append(paramMap.get("sip")).append("\t");
        result.append(paramMap.get(PARAM.uuid.toString())).append("\t");
        result.append(paramMap.get(PARAM.ac.toString())).append("\t");
        // result.append(paramMap.get(PARAM.err.toString())).append("\t");
        result.append(paramMap.get(PARAM.cdnbits.toString())).append("\t");
        result.append(paramMap.get(PARAM.p2pbits.toString())).append("\t");
        result.append(paramMap.get(PARAM.st.toString())).append("\t");
        result.append(paramMap.get(PARAM.pt.toString())).append("\t");
        result.append(paramMap.get(PARAM.ht.toString())).append("\t");
        // result.append(paramMap.get(PARAM.ver.toString())).append("\t");
        result.append(pid);
        LOG.debug(result.toString());
        return result.toString();
    }
}
