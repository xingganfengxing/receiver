package com.letv.cdn.receiver.servlet.support;

import java.util.Map;

import com.letv.cdn.receiver.exception.ValidateException;

/**
 * 直播Rtmp业务接口
 * 
 * @author kk
 */
public interface IliveRtmpReceiver extends IliveReceiver{
    
    /** 参数验证 */
    boolean validateParams(Map<String, String> param) throws ValidateException;
    
    /** 数据拆分 */
    String[] splitParams(String str) throws Exception;
    
    /** 参数业务封装 */
    void handleParams(Map<String, String> param) throws Exception;
    
}
