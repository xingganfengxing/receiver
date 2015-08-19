package com.letv.cdn.receiver.exception;

import com.letv.cdn.receiver.model.ReceiverExceptionData;

/**
 * 异常处理接口
 * 
 * @author kk
 */
public interface ReceiverExceptionHandler{
    
    void handleEventException(Exception ex, String log, String action) throws Exception;
    
    ReceiverExceptionData handleEventException(Throwable ex, String log, ReceiverExceptionData exceptionData,
            String action);
}
