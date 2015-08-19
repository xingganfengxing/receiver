package com.letv.cdn.receiver.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.receiver.exception.ValidateException;
import com.letv.cdn.receiver.model.VodBandwidthMap;
import com.letv.cdn.receiver.servlet.support.AbstractBaseLiveMultipleServlet;
import com.letv.cdn.receiver.servlet.support.IVodBandwidthReceiver;

/**
 * 点播带宽服务端入口
 * 
 * @author kk
 */
public class VodBandwidthSSServlet extends AbstractBaseLiveMultipleServlet implements IVodBandwidthReceiver{
    
    private static final Logger LOG = LoggerFactory.getLogger(VodBandwidthSSServlet.class);
    
    private static enum PARAM {
        timestamp, splatid, platid, sip, tag, bw, type
    };
    
    private static final PARAM[] params = PARAM.values();
    
    @Override
    public boolean validateParams(Map<String, String> paramMap) throws ValidateException {
    
        if (null == paramMap || paramMap.size() < 7) {
            throw new ValidateException("args miss");
        }
        // for (int i = 0; i < params.length; i++) {
        // if (StringUtils.isEmpty(paramMap.get(params[i].toString()))) {
        // throw new ValidateException(params[i].toString() + " miss");
        // }
        // }
        return true;
    }
    
    @Override
    public String[] splitParams(String str) throws Exception {
    
        return str.split("\n");
        
    }
    
    @Override
    public void handleParams(Map<String, String> param) throws Exception {
    
        SimpleDateFormat sdft = new SimpleDateFormat("yyyyMMddHHmmss");
        param.put("timestamp_parse", String.valueOf(sdft.parse(param.get(PARAM.timestamp.toString())).getTime()));
        // 带宽计算
        // long flow = Long.valueOf(param.get(PARAM.bw.toString()));//
        // 一分钟内的流量合byte
        // long bw = flow * 8 / 60;
        // param.put(PARAM.bw.toString(), String.valueOf(flow));
    }
    
    @Override
    public void addCacheData(Object param) throws Exception {
    
        Map<String, String> paramMap = (Map<String, String>) param;
        // String format_time =
        // getMinutetime(paramMap.get(PARAM.timestamp.toString()), 5);// 5分钟时间取整
        String format_time = getMinutetime(paramMap.get(PARAM.timestamp.toString()), 15);// 15秒时间取整
        StringBuilder keu_prefix = new StringBuilder(format_time);
        keu_prefix.append("_").append(paramMap.get(PARAM.splatid.toString()));
        keu_prefix.append("_").append(paramMap.get(PARAM.platid.toString()));
        keu_prefix.append("_").append(paramMap.get(PARAM.tag.toString()));
        keu_prefix.append("_").append(paramMap.get(PARAM.type.toString()));
        keu_prefix.append("_bw");
        int bw = Math.round(Float.parseFloat(paramMap.get(PARAM.bw.toString())));
        VodBandwidthMap.incrementBw(keu_prefix.toString(), bw);// 内部缓存
        // long r = XMemcacheUtil.incrementToCache(keu_prefix.toString(), bw,
        // bw, DEFAULT_EXPIRE_TIME);
        // LOG.info("bw cache : {}", keu_prefix + ":" + r + "," + bw);
    }
    
    @Override
    public void addStormData(Object param) throws Exception {
    
        // addDB(param);
    }
    
    @Override
    public String getFormat() {
    
        return "yyyyMMddHHmm";
    }
    
    // @Override
    // public void addDB(Object param) throws Exception {
    //
    // Map<String, String> paramMap = (Map<String, String>) param;
    // StringBuilder result = new StringBuilder();
    // result.append(paramMap.get(PARAM.timestamp.toString())).append(",");
    // result.append(paramMap.get(PARAM.splatid.toString())).append(",");
    // result.append(paramMap.get(PARAM.platid.toString())).append(",");
    // result.append(paramMap.get(PARAM.sip.toString())).append(",");
    // result.append(paramMap.get(PARAM.tag.toString())).append(",");
    // result.append(paramMap.get(PARAM.bw.toString()));
    // SQLUtils.insert(result.toString(), SQLUtils.VOD_BANDWIDTH_SERVER);
    // }
    
    private static String getMinutetime(String time, int sec) throws ParseException {
    
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        long ms = sdf.parse(time).getTime();
        int m = sec * 1000;
        return sdf.format((ms / m + 1) * m);
    }
    
    public static void main(String[] args) {
    
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            // System.out.println(getMinutetime("20150522142016", 15));
            long ms = sdf.parse("2015052214516").getTime();
            System.out.println(ms);
        } catch (ParseException e) {
            e.printStackTrace();
            
        }
    }
}
