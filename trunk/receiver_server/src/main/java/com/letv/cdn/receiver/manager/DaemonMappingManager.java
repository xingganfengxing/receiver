package com.letv.cdn.receiver.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.letv.cdn.common.Env;
import com.letv.cdn.common.StringUtil;
import com.letv.cdn.receiver.model.NPMappingModel;
import com.letv.cdn.receiver.model.NodeMapping;
import com.letv.cdn.receiver.model.SNMappingModel;

public class DaemonMappingManager extends TimerTask{
    private static final Logger LOG = LoggerFactory.getLogger(DaemonMappingManager.class);
    private HttpClient client;
    private GetMethod method;
    private Map<String, String> SNMap; // 保存serverip与CDN节点对应关系
    private Map<String, String> NPMap; // 保存CDN节点与省份对应关系
    private Map<String, String> SPMap; // 保存serverip与省份对应关系
    private static String SNUrl;
    private static String NPUrl;
    private Gson gson = new Gson();
    // private long sysTimeCounter;
    
    static {
        SNUrl = StringUtil.isEmpty(Env.get("SNUrl")) ? "http://g3.letv.com/stat/hosts?cdnid=999999" : Env.get("SNUrl");
        NPUrl = StringUtil.isEmpty(Env.get("NPUrl")) ? "http://g3.letv.com/stat/nodes?format=1" : Env.get("NPUrl");
    }
    
    private void requestSNMapping(String url) {
    
        SNMap = new HashMap<String, String>();
        // sysTimeCounter = System.currentTimeMillis();
        String body = sendRequest(url);
        if (StringUtils.isNotEmpty(body)) {
            List<SNMappingModel> snmapList = gson.fromJson(body, new TypeToken<List<SNMappingModel>>(){
            }.getType());
            String host_tmp = "";
            for (SNMappingModel model : snmapList) {
                host_tmp = model.getHost0();
                if (null != host_tmp) {
                    SNMap.put(host_tmp, model.getEname());
                }
                host_tmp = model.getHost1();
                if (null != host_tmp) {
                    SNMap.put(host_tmp, model.getEname());
                }
                host_tmp = model.getHost2();
                if (null != host_tmp) {
                    SNMap.put(host_tmp, model.getEname());
                }
            }
        }
        LOG.info("call SNMap interface consumes length,body:" + body.length() + ",SNMap:" + SNMap.size());
    }
    
    private void requestNPMapping(String url) {
    
        NPMap = new HashMap<String, String>();
        // sysTimeCounter = System.currentTimeMillis();
        String body = sendRequest(url);
        if (StringUtils.isNotEmpty(body)) {
            List<NPMappingModel> npmapList = gson.fromJson(body, new TypeToken<List<NPMappingModel>>(){
            }.getType());
            for (NPMappingModel model : npmapList) {
                NPMap.put(model.getNodename(), model.getProvinceid());
            }
        }
        LOG.info("call NPMap interface consumes length,body:" + body.length() + ",NPMap:" + NPMap.size());
    }
    
    /**
     * 将serverip与省份进行映射
     */
    private void getRelationMapping() {
    
        // sysTimeCounter = System.currentTimeMillis();
        SPMap = new HashMap<String, String>();
        String key;
        String value;
        for (Entry<String, String> e : SNMap.entrySet()) {
            key = e.getKey();
            value = NPMap.get(e.getValue());
            if (null != key && null != value) {
                SPMap.put(key, value);
            }
        }
        
        if (MapUtils.isNotEmpty(SPMap)) {
            synchronized (NodeMapping.SPMapping) {
                NodeMapping.SPMapping.clear();
                NodeMapping.SPMapping.putAll(SPMap);
            }
        } else {
            LOG.error("size of SPMap is 0, please check");
        }
    }
    
    private String sendRequest(String url) {
    
        String body = "";
        client = new HttpClient();
        method = new GetMethod(url);
        // 设置timeout
        method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        // 设置请求重试处理
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        int statusCode;
        try {
            statusCode = client.executeMethod(method);
            // 判断访问状态码
            if (statusCode != 200) {
                LOG.error("获取映射关系失败");
                return body;
            }
            body = method.getResponseBodyAsString();
        } catch (HttpException e) {
            LOG.error("请求映射关系失败", e);
        } catch (IOException e) {
            LOG.error("请求映射关系失败", e);
        }
        return body;
    }
    
    @Override
    public void run() {
    
        requestSNMapping(SNUrl);
        requestNPMapping(NPUrl);
        getRelationMapping();
    }
}
