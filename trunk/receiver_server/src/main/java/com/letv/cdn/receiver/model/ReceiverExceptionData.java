package com.letv.cdn.receiver.model;

import com.letv.cdn.receiver.exception.ReceiverExceptionCode;

/**
 * 异常信息模型
 * 
 * @author kk
 */
public class ReceiverExceptionData{
    
    private String message;
    private ReceiverExceptionCode code;
    private String object;
    private String type;
    
    public ReceiverExceptionData() {
    
    }
    
    public ReceiverExceptionData(String msg, ReceiverExceptionCode code, String obj, String type) {
    
        this.message = msg;
        this.code = code;
        this.object = obj;
        this.type = type;
    }
    
    public String getType() {
    
        return type;
    }
    
    public void setType(String type) {
    
        this.type = type;
    }
    
    public String getMessage() {
    
        return message;
    }
    
    public ReceiverExceptionCode getCode() {
    
        return code;
    }
    
    public String getObject() {
    
        return object;
    }
    
    public void setMessage(String message) {
    
        this.message = message;
    }
    
    public void setCode(ReceiverExceptionCode code) {
    
        this.code = code;
    }
    
    public void setObject(String object) {
    
        this.object = object;
    }
    
    public String toString() {
    
        return code.value + "," + code.name() + "," + message + "," + object + "," + type;
    }
}
