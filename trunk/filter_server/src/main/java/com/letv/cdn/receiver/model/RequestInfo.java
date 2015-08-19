package com.letv.cdn.receiver.model;

/**
 * 请求信息Bean
 * 
 * @author kk
 */
public class RequestInfo extends BaseModel{
    
    private String body;
    
    public String getBody() {

        return body;
    }
    
    public void setBody(String body) {

        this.body = body;
    }
    
    @Override
    public String toString() {

        return "body:" + this.body;
    }
}
