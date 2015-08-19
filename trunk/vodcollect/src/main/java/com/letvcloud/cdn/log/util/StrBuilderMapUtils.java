package com.letvcloud.cdn.log.util;

import java.util.Map;

/**
 * StrBuilderMap 通用类
 * @author liufeng1
 * @date 28/5/2015
 */
public class StrBuilderMapUtils {

    /**
     * StrBuilderMap put
     * @param map
     * @param key
     * @param value
     */
    public static void appendValue(Map<String,StringBuilder> map,String key,String value){
        if(map.containsKey(key)){
            map.get(key).append(value);
        }else{
            map.put(key,new StringBuilder(value));
        }
    }
}
