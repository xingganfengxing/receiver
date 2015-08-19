package com.letv.cdn.receiver.exception;

/**
 * 验证异常
 * 
 * @author kk
 */
public class ValidateException extends Exception{
    
    private int type;
    
    public ValidateException(String paramString) {
    
        this(paramString, 0);
    }
    
    public ValidateException(String paramString, int type) {
    
        super(paramString);
        this.type = type;
    }
    
    public int getType() {
    
        return this.type;
    }
    
}