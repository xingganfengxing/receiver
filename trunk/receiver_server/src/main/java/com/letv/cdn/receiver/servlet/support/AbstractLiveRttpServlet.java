package com.letv.cdn.receiver.servlet.support;

import java.net.URLDecoder;

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
import com.letv.cdn.receiver.util.SequenceUtil;

/**
 * 直播rttp业务流程控制
 * 
 * @author kk
 */
public abstract class AbstractLiveRttpServlet extends BaseServlet implements IliveRttpReceiver{
    
    private static final Logger LOG = LoggerFactory.getLogger(AbstractLiveRttpServlet.class);
    public static final int KAFKA_POOL_SIZE_FOR_LIVE_RTTP = StringUtils.isEmpty(Env.get("kafkaPoolSizeForLiveRttp")) ? 1
            : Integer.parseInt(Env.get("kafkaPoolSizeForLiveRttp"));
    protected static final SequenceUtil sequence = new SequenceUtil();
    private ReceiverExceptionHandler exceptionHandler = new LiveExceptionHandler();// 指定异常处理机制
    
    @Override
    protected void execute(HttpServletRequest request, HttpServletResponse response) {
    
        final JsonWapper wapper = new JsonWapper();
        String flag = Constants.RESULT_SUCCESS;
        String body = "";
        final String uri = request.getRequestURI();
        try {
            body = getParameters(request);
            addStormData(body);
        } catch (Throwable e) {
            flag = Constants.RESULT_FAILED;
            ReceiverExceptionData exceptionData = new ReceiverExceptionData();
            exceptionHandler.handleEventException(e, body, exceptionData, uri);
            JSONObject jsonObject = wapper.nextItem();
            jsonObject.put(body, exceptionData.getCode().value);
            wapper.endItem(jsonObject);
        } finally {
            try {
                wapper.writeStore(response, flag);
            } catch (Exception e) {
                LOG.error("writeResponse err: ", e);
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
        }
        return p;
    }
    
    @Override
    public void addCacheData(Object param) throws Exception {
    
        // 暂时无操作
    }
    
}
