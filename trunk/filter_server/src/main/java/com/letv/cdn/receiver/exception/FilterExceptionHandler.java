package com.letv.cdn.receiver.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.ExceptionHandler;

public final class FilterExceptionHandler implements ExceptionHandler{
    
    private static final Logger log = LoggerFactory.getLogger(FilterExceptionHandler.class);
    
    @Override
    public void handleEventException(final Throwable ex, final long sequence, final Object event) {
    
        log.error("Exception processing: " + event, ex);
    }
    
    @Override
    public void handleOnStartException(final Throwable ex) {
    
        log.error("Exception during onStart()", ex);
    }
    
    @Override
    public void handleOnShutdownException(final Throwable ex) {
    
        log.error("Exception during onShutdown()", ex);
    }
}
