package com.letvcloud.cdn.log.model;

/**
 * 解析一行日志的返回元素
 * Created by liufeng1 on 2015/2/6.
 */
public class ParseLineResult {
    private boolean isParseSuccess = true;
    private LogData logData;

    public boolean isParseSuccess() {
        return isParseSuccess;
    }

    public void setParseSuccess(boolean isParseSuccess) {
        this.isParseSuccess = isParseSuccess;
    }

    public LogData getLogData() {
        return logData;
    }

    public void setLogData(LogData logData) {
        this.logData = logData;
    }
}
