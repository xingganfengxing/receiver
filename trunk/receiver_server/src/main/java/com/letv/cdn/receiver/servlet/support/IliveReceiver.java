package com.letv.cdn.receiver.servlet.support;

/**
 * 直播业务接口
 * 
 * @author kk
 */
public interface IliveReceiver extends IReceiver{
    
    /** 发送缓存操作 */
    void addCacheData(Object param) throws Exception;
    
    /** 发送待计算数据操作 */
    void addStormData(Object param) throws Exception;
    
}
