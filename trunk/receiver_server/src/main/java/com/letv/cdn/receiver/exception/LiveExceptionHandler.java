package com.letv.cdn.receiver.exception;

import java.sql.SQLException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.receiver.model.ReceiverExceptionData;
import com.letv.cdn.receiver.util.ExceptionMessageUtil;

/**
 * 直播异常处理
 * 
 * @author kk
 */
public final class LiveExceptionHandler implements ReceiverExceptionHandler{
    
    private static final Logger LOG = LoggerFactory.getLogger(LiveExceptionHandler.class);
    
    @Override
    public ReceiverExceptionData handleEventException(Throwable ex, String line, ReceiverExceptionData exceptionData,
            String type) {
    
        if (ex instanceof ValidateException) {
            exceptionData.setCode(ReceiverExceptionCode.PARAMETER_MISS);
        } else if (ex instanceof ParseException) {
            exceptionData.setCode(ReceiverExceptionCode.PARAMETER_ERROR);
        } else if (ex instanceof net.spy.memcached.OperationTimeoutException) {
            exceptionData.setCode(ReceiverExceptionCode.MEMCACHED_TIMEDOUT);
        } else if (ex instanceof net.rubyeye.xmemcached.exception.MemcachedException
                || ex instanceof java.util.concurrent.TimeoutException
                || ex.getCause() instanceof net.rubyeye.xmemcached.exception.MemcachedException) {
            exceptionData.setCode(ReceiverExceptionCode.MEMCACHED_TIMEDOUT);
        } else if (ex instanceof kafka.common.FailedToSendMessageException
                || ex.getCause() instanceof kafka.common.FailedToSendMessageException) {
            exceptionData.setCode(ReceiverExceptionCode.KAFKA_TIMEDOUT);
        } else if (ex instanceof SQLException) {
            exceptionData.setCode(ReceiverExceptionCode.DB_ERROR);
        } else {
            exceptionData.setCode(ReceiverExceptionCode.UNKNOWN_EXCEPTION);
        }
        exceptionData.setMessage(ex.getMessage());
        exceptionData.setObject(line);
        exceptionData.setType(type);
        LOG.error("handleEventException:", ex);
        if (!ExceptionMessageUtil.addqueueException(exceptionData)) {
            LOG.error("add exceptionMessage err!");
        }
        return exceptionData;
        
    }
    
    @Override
    public void handleEventException(Exception ex, String LOG, String type) throws Exception {
    
        handleEventException(ex, LOG, new ReceiverExceptionData(), type);
        
    }
    
}
