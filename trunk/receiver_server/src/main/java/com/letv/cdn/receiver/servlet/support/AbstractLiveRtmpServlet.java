package com.letv.cdn.receiver.servlet.support;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.letv.cdn.common.Env;
import com.letv.cdn.receiver.exception.LiveExceptionHandler;
import com.letv.cdn.receiver.exception.ReceiverExceptionHandler;
import com.letv.cdn.receiver.model.ReceiverExceptionData;
import com.letv.cdn.receiver.util.Constants;
import com.letv.cdn.receiver.util.JsonWapper;

/**
 * 直播rtmp业务流程控制
 * 
 * @author kk
 */
public abstract class AbstractLiveRtmpServlet extends BaseServlet implements IliveRtmpReceiver{
    
    private static final Logger LOG = LoggerFactory.getLogger(AbstractLiveRtmpServlet.class);
    public static final int KAFKA_POOL_SIZE_FOR_LIVE_RTMP_SERVER = StringUtils.isEmpty(Env
            .get("kafkaPoolSizeForLiveRtmpServer")) ? 1 : Integer.parseInt(Env.get("kafkaPoolSizeForLiveRtmpServer"));
    public static final int KAFKA_POOL_SIZE_FOR_LIVE_RTMP_CLIENT = StringUtils.isEmpty(Env
            .get("kafkaPoolSizeForLiveRtmpClient")) ? 1 : Integer.parseInt(Env.get("kafkaPoolSizeForLiveRtmpClient"));
    public static final int DEFAULT_EXPIRE_TIME = 35 * 60 * 1000;
    public static final int LIVE_SERVER_WORKER_THREAD_MAX = StringUtils.isEmpty(Env.get("liveServerWorkerThreadMax")) ? Runtime
            .getRuntime().availableProcessors() * 2 : Integer.parseInt(Env.get("liveServerWorkerThreadMax"));
    public static final int LIVE_SERVER_WORKER_THREAD_CORE = StringUtils.isEmpty(Env.get("liveServerWorkerThreadCore")) ? 5000
            : Integer.parseInt(Env.get("liveServerWorkerThreadCore"));
    public static final int LIVE_SERVER_QUEUE_SIZE = StringUtils.isEmpty(Env.get("liveServerQueueSize")) ? Runtime
            .getRuntime().availableProcessors() * 2 : Integer.parseInt(Env.get("liveServerQueueSize"));
    
    // private static final int coolSize =
    // Runtime.getRuntime().availableProcessors();
    public static final ThreadPoolExecutor daemonExecutor = new ThreadPoolExecutor(LIVE_SERVER_WORKER_THREAD_CORE,
            LIVE_SERVER_WORKER_THREAD_MAX, 10L, TimeUnit.SECONDS, new ArrayBlockingQueue(LIVE_SERVER_QUEUE_SIZE));
    public static final ThreadPoolExecutor daemonMemcachedExecutor = new ThreadPoolExecutor(
            LIVE_SERVER_WORKER_THREAD_CORE, LIVE_SERVER_WORKER_THREAD_MAX, 10L, TimeUnit.SECONDS,
            new ArrayBlockingQueue(LIVE_SERVER_QUEUE_SIZE));// 处理memcached操作线程
    public static final ThreadPoolExecutor daemonKafkaExecutor = new ThreadPoolExecutor(LIVE_SERVER_WORKER_THREAD_CORE,
            LIVE_SERVER_WORKER_THREAD_MAX, 10L, TimeUnit.SECONDS, new ArrayBlockingQueue(LIVE_SERVER_QUEUE_SIZE));// 处理kafka操作线程
    private ReceiverExceptionHandler exceptionHandler = new LiveExceptionHandler();
    
    static {
        daemonExecutor.allowCoreThreadTimeOut(false);
        daemonMemcachedExecutor.allowCoreThreadTimeOut(false);
        daemonKafkaExecutor.allowCoreThreadTimeOut(false);
    }
    
    @Override
    protected void execute(HttpServletRequest request, HttpServletResponse response) {
    
        final JsonWapper wapper = new JsonWapper();
        FutureContext<Boolean> context = new FutureContext<Boolean>();
        String flag = Constants.RESULT_SUCCESS;
        final String uri = request.getRequestURI();
        try {
            String body = getParameters(request);
            String[] bodyArr = splitParams(body);// body.split("\n");
            // 循环每一行处理
            for (int i = 0; i < bodyArr.length; i++) {
                final String line = bodyArr[i];
                // if (StringUtil.isEmpty(line)) {
                // continue;
                // }
                Future<Boolean> future = daemonExecutor.submit(new Callable<Boolean>(){
                    public Boolean call() {
                    
                        boolean _flag = true;
                        try {
                            template(line);
                        } catch (Throwable e) {
                            // 执行异常统一操作
                            _flag = false;
                            ReceiverExceptionData exceptionData = new ReceiverExceptionData();
                            exceptionHandler.handleEventException(e, line, exceptionData, uri);
                            JSONObject jsonObject = wapper.syncNextItem();
                            jsonObject.put(line, exceptionData.getCode().value);
                            wapper.syncEndItem(jsonObject);
                        }
                        return _flag;
                    }
                });
                // 每个异步计算的结果存放在context中
                context.addFuture(future);
            }
            List<Future<Boolean>> list = context.getFutureList();
            for (Future<Boolean> future : list) {
                if (!future.get()) {
                    flag = Constants.RESULT_FAILED;
                }
            }
        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
            response.setStatus(500);
            flag = Constants.RESULT_FAILED;
            ReceiverExceptionData exceptionData = new ReceiverExceptionData();
            exceptionHandler.handleEventException(e, "err", exceptionData, uri);
            JSONObject jsonObject = wapper.nextItem();
            jsonObject.put("err", exceptionData.getCode().value);
            wapper.endItem(jsonObject);
        } finally {
            try {
                wapper.writeStore(response, flag);
                context = null;
            } catch (Exception e) {
                LOG.error("writeResponse err: ", e);
            }
        }
    }
    
    /** 业务模板 */
    private void template(String line) throws Exception {
    
        final Map<String, String> paramMap = getParameterMap(line);
        // 统一验证
        boolean _vflag = false;
        _vflag = validateParams(paramMap);
        if (_vflag) {
            handleParams(paramMap);// 参数处理
            Future<Boolean> futureK = daemonKafkaExecutor.submit(new Callable<Boolean>(){
                public Boolean call() throws Exception {
                
                    addStormData(paramMap);
                    return true;
                }
            });
            // 判断日志时间是否为30分钟以内
            String slogtime = paramMap.get("timestamp_bak");
            long time = threadLocal.get().parse(slogtime).getTime();
            Future<Boolean> futureM = null;
            if (ableLiveData(time)) {
                futureM = daemonMemcachedExecutor.submit(new Callable<Boolean>(){
                    public Boolean call() throws Exception {
                    
                        addCacheData(paramMap);
                        return true;
                    }
                });
            }
            if (!futureK.get() || (null != futureM && !futureM.get())) {
                throw new Exception("io err");
            }
        }
    }
    
    private String getParameters(HttpServletRequest request) throws Exception {
    
        String p = "";
        String method = request.getMethod();
        if ("GET".equals(method)) {
            p = URLDecoder.decode(convertToUTF8(request.getQueryString()), "UTF-8");
        } else if ("POST".equals(method)) {
            p = getBodyString(request.getInputStream());
            // p = getBodyString(request.getReader());
        }
        return p;
    }
    
    /**
     * 超过30分钟为无效实时数据 logtime 201411061536 yyyyMMddhhmmss
     *
     * @param slogtime
     * @return
     */
    private boolean ableLiveData(long slogtime) {
    
        long now = System.currentTimeMillis();
        if (now - slogtime < DEFAULT_EXPIRE_TIME) {
            return true;
        }
        LOG.info("非有效数据 {}", slogtime);
        return false;
    }
    
    protected static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
        
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };
    
    public static class FutureContext<T> {
        
        private List<Future<T>> futureList = new ArrayList<Future<T>>();
        
        public void addFuture(Future<T> future) {
        
            this.futureList.add(future);
        }
        
        public List<Future<T>> getFutureList() {
        
            return this.futureList;
        }
    }
}
