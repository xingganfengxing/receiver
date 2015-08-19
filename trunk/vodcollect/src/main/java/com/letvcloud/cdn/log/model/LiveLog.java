package com.letvcloud.cdn.log.model;

import lombok.Data;

/**
 * flv,hls日志实体
 * @author liufeng1
 * @date 3/6/2015
 */
@Data
public class LiveLog {
    private String ptime;       //时间
    private String protocol;    //协议
    private String domain;      //域名
}
