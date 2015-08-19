package com.letv.cdn.receiver.model;

import com.letv.cdn.receiver.util.Constants;

/**
 * @author liufeng1
 * @date 9/4/2015
 */
public class LogData {
    private int id;                     //id
    private int logType;                //日志类型
    private int streamIndex;                  //流在任务中的序号
    private String taskId;              //任务id
    private String streamId;            //流id
    private String timestamp;           //时间撮
    private String clientIp;            //播放器所在ip
    private String serverIp;            //播放器请求服务器ip
    private String loadingDuration;     //卡顿时长
    private String originRoute;         //回源路径
    private String playbackDuration;    //播放时长

    public String toLine() {
        return logType + Constants.TAB +
                streamIndex + Constants.TAB +
                taskId + Constants.TAB +
                streamId + Constants.TAB +
                timestamp + Constants.TAB +
                clientIp + Constants.TAB +
                serverIp + Constants.TAB +
                loadingDuration + Constants.TAB +
                originRoute + Constants.TAB +
                playbackDuration + Constants.TAB;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public int getStreamIndex() {
        return streamIndex;
    }

    public void setStreamIndex(int streamIndex) {
        this.streamIndex = streamIndex;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getLoadingDuration() {
        return loadingDuration;
    }

    public void setLoadingDuration(String loadingDuration) {
        this.loadingDuration = loadingDuration;
    }

    public String getOriginRoute() {
        return originRoute;
    }

    public void setOriginRoute(String originRoute) {
        this.originRoute = originRoute;
    }

    public String getPlaybackDuration() {
        return playbackDuration;
    }

    public void setPlaybackDuration(String playbackDuration) {
        this.playbackDuration = playbackDuration;
    }
}
