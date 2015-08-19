package com.letvcloud.cdn.log.model;

import lombok.Data;

/**
 * 从一条原始日志切割而来的日志数据
 * Created by liufeng1 on 2015/2/6.
 */
@Data
public class LogData {
    private String ptime;
    private String bandwidth;
    private String maliu;
    private String httpcode;
    private String userip;
    private String serverip;
    private String platid;
    private String splatid;
    private String sign;
    private String playid;
    private String responsetime;
    private String customerName;
    private String geo;
    private String hitMiss;
    private String requestUrl;
    private String refererHeader;
    private String streamId;
    private String mmsid;

    public String toLine() {
        String line = ptime + "\t"
                + bandwidth + "\t"
                + maliu + "\t"
                + httpcode + "\t"
                + userip + "\t"
                + serverip + "\t"
                + platid + "\t"
                + splatid + "\t"
                + sign + "\t"
                + playid + "\t"
                + responsetime + "\t"
                + customerName + "\t"
                + geo + "\t"
                + hitMiss + "\t"
                + requestUrl + "\t"
                + refererHeader + "\t"
                + streamId+ "\t"
                + mmsid;
        return line;
    }
}
