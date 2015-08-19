package com.letv.cdn.receiver.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.letv.cdn.common.web.JsonResult;
import com.letv.cdn.common.web.ResponseUtil;
import com.letv.cdn.receiver.model.DataResponse;
import com.letv.cdn.receiver.model.LiveData;
import com.letv.cdn.receiver.service.LiveDisplayService;
import com.letv.cdn.receiver.servlet.support.BaseServlet;
import com.letv.cdn.receiver.util.Constants;
import com.letv.cdn.receiver.util.MemcacheUtil;

/**
 * Created by liujs on 14-11-6.
 * 
 * @modify by sunyan 直播服务器上报servlet
 */
public class LiveClientDisplayServlet extends BaseServlet{
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(LiveClientDisplayServlet.class);
    private static String STREAMID = "streamid", TYPE = "type";
    String KEY_PREFIX = "client";
    public static LiveDisplayService liveDisplayService;
    static {
        String[] SPRING_CONF_PATH = { "classpath*:applicationContext.xml" };
        ApplicationContext context = new ClassPathXmlApplicationContext(SPRING_CONF_PATH);
        liveDisplayService = (LiveDisplayService) context.getBean("liveDisplayService");
    }
    
    @Override
    protected void execute(HttpServletRequest request, HttpServletResponse response) {
    
        // MemcachedClient memcachedClient = null;
        JsonResult jsonResult;
        DataResponse dataResponse = new DataResponse();
        try {
            Map<String, String> paramMap = getParameterMap(request);
            if (paramMap.size() < 2) {
                jsonResult = JsonResult.gen(400, "args miss");
                ResponseUtil.sendJsonNoCache(response, jsonResult);
                return;
            }
            // 条件：stream_id type
            String streamid = paramMap.get(LiveClientDisplayServlet.STREAMID); // 流ID
            String type = paramMap.get(LiveClientDisplayServlet.TYPE);// 粒度
            if (!(type.equals("0") || type.equals("1") || type.equals("2"))) {
                dataResponse.setResult(Constants.RESULT_FAILED);
                dataResponse.setMessage("参数type错误：其中 0:1分钟粒度  1：五分钟粒度  2：一小时粒度");
                return;
            }
            // memcachedClient = MemcachedComunication.getSafeSession();
            // 获取当前24小时数据
            SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmm");
            String endCacheTime = sd.format(new Date());
            long t = sd.parse(endCacheTime).getTime() - 60 * 1000 * 30;
            String endMysqlTime = sd.format(new Date(t));
            long t1 = sd.parse(endCacheTime).getTime() - 60 * 1000 * 60 * 24;
            String startMysqlTime = sd.format(new Date(t1));
            
            // 获取前半小时的缓存
            long time = System.currentTimeMillis();
            String valueC = getCacheData(endCacheTime, streamid, null);
            long ctime = (System.currentTimeMillis() - time);
            LOG.info("查询缓存耗时ms：" + ctime);
            
            // 获取mysql数据
            long time1 = System.currentTimeMillis();
            String valueM = liveDisplayService.queryClient(startMysqlTime, endMysqlTime, streamid);
            long mtime = (System.currentTimeMillis() - time1);
            LOG.info("查询mysql耗时ms：" + mtime);
            
            String message = "";
            if (type.equals("0")) {// 1分钟粒度
                message = valueC + "---------------</br>" + valueM;
            } else {// 五分钟粒度和1小时粒度
                message = generateLiveData(valueC + valueM, type);
            }
            dataResponse.setResult(Constants.RESULT_SUCCESS);
            dataResponse.setMessage("</br>-----cache查询耗时ms：" + ctime + "-----</br>" + "-----mysql查询耗时ms：" + mtime
                    + "----</br>" + message);
            LOG.info("查询耗时ms：" + (System.currentTimeMillis() - time));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            dataResponse.setResult(Constants.RESULT_FAILED);
            dataResponse.setMessage(e.getMessage());
        } finally {
            try {
                // MemcachedComunication.realeseMemcached(memcachedClient);
                writeResponse(response, dataResponse);
            } catch (Exception e) {
                LOG.error("writeResponse err: ", e);
            }
        }
    }
    
    private String mapToString(Map<String, LiveData> maps) {
    
        StringBuilder sb = new StringBuilder();
        for (LiveData data : maps.values()) {
            sb.append(data.toString());
            sb.append("</br>");
        }
        return sb.toString();
    }
    
    private String generateLiveData(String value, String type) throws Exception {
    
        Map<String, LiveData> maps = new TreeMap<String, LiveData>(new Comparator<String>(){
            public int compare(String obj1, String obj2) {
            
                return obj2.compareTo(obj1); // 降序排序
            }
        });
        for (String s : value.split("</br>")) {
            if (StringUtils.isNotBlank(s)) {
                String[] arr = s.split("\t");
                String ptimelen = "";
                if (type.equals("1")) {
                    if (Integer.valueOf(arr[0].substring(11)).intValue() < 5) {
                        ptimelen = arr[0].substring(0, 11) + "0";
                    } else {
                        ptimelen = arr[0].substring(0, 11) + "5";
                    }
                } else {
                    ptimelen = arr[0].substring(0, 10);
                }
                Long visit = Long.valueOf(arr[1]);
                Long bw = Long.valueOf(arr[2]);
                if (maps.containsKey(ptimelen)) {
                    LiveData liveTmp = maps.get(ptimelen);
                    if (liveTmp.compareBw(bw) < 0) {
                        liveTmp.setBw(bw);
                    }
                    if (liveTmp.compareVisit(visit) < 0) {
                        liveTmp.setBw(visit);
                    }
                } else {
                    maps.put(ptimelen, new LiveData(ptimelen, visit, bw));
                }
            }
        }
        return mapToString(maps);
    }
    
    /**
     * 1分钟实时数据进入缓存，主要统计在线人数
     * 
     * @throws ParseException
     */
    private String getCacheData(String startCache, String streamId, MemcachedClient memcachedClient)
            throws ParseException {
    
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmm");
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < 31; i++) {
            long t = sd.parse(startCache).getTime() - 60 * 1000L * i;
            String timestamp = sd.format(new Date(t));
            long onlines = 0;
            long slows = 0;
            String keu_prefix = KEY_PREFIX + "_" + streamId + "_" + timestamp;
            String onlineKey = keu_prefix + "_Q_online";// 在线人数
            String slowKeyQ = keu_prefix + "_Q_slow";// 卡顿
            
            Object ob = MemcacheUtil.get(onlineKey);
            Object ob1 = MemcacheUtil.get(slowKeyQ);
            if (ob != null) {
                Long online = Long.valueOf(ob.toString());
                onlines += online.longValue();
            }
            if (ob1 != null) {
                Long slow = Long.valueOf(ob1.toString());
                slows += slow.longValue();
            }
            sb.append(timestamp).append("\t").append(onlines).append("\t").append(slows);
            sb.append("</br>");
        }
        return sb.toString();
    }
    
    public LiveDisplayService getLiveDisplayService() {
    
        return liveDisplayService;
    }
    
//    public void setLiveDisplayService(LiveDisplayService liveDisplayService) {
//    
//        this.liveDisplayService = liveDisplayService;
//    }
}
