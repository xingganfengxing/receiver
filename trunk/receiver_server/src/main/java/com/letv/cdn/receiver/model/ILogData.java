package com.letv.cdn.receiver.model;

import java.util.Map;

/**
 * 实体统一接口
 * @author liufeng1
 * @date 6/5/2015
 */
public interface ILogData {

    /**
     * 实体转字符串line
     * @return
     */
    String toLine();

    /**
     * 转实体
     * @param map
     * @return 参数是否齐全
     */
    boolean mapToILogData(Map map);
}
