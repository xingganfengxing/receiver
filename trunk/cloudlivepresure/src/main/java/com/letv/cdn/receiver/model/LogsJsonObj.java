package com.letv.cdn.receiver.model;

import java.util.List;

/**
 * logs josn
 * @author liufeng1
 * @date 14/4/2015
 */
public class LogsJsonObj {
    private List<LogData> logs;

    public List<LogData> getLogs() {
        return logs;
    }

    public void setLogs(List<LogData> logs) {
        this.logs = logs;
    }
}
