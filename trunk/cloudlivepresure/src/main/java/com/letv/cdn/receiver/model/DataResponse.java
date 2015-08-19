package com.letv.cdn.receiver.model;

public class DataResponse {
    
    protected String result;
    protected String message;
    
    public DataResponse() {
    
    }
    
    public String getResult() {
    
        return result;
    }
    
    public void setResult(String result) {
    
        this.result = result;
    }
    
    public String getMessage() {
    
        return message;
    }
    
    public void setMessage(String message) {
    
        this.message = message;
    }
    
    public String toString() {
    
        return "{\"result\":\"" + result + "\"," + "\"message\":" + "\"" + message + "\"}";
    }
}
