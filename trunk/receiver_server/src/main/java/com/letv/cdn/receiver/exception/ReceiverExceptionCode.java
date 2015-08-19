package com.letv.cdn.receiver.exception;

/**
 * 自定义业务异常状态码
 * 
 * @author kk
 */
public enum ReceiverExceptionCode {
    PARAMETER_MISS("00002"), PARAMETER_ERROR("00003"), MEMCACHED_TIMEDOUT("00004"), KAFKA_TIMEDOUT("00005"), DB_ERROR(
            "00006"), UNKNOWN_EXCEPTION();
    
    public String value = "00001";// UNKNOWN_EXCEPTION
    
    ReceiverExceptionCode() {
    
    }
    
    ReceiverExceptionCode(String value) {
    
        this.value = value;
    }
    
}
