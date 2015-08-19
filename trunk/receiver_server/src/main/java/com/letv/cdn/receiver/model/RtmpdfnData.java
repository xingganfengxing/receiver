package com.letv.cdn.receiver.model;

import com.letv.cdn.receiver.util.LogReportConstants;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * rtmp播放中丢帧统计日志格式
 *
 * @author liufeng1
 * @date 6/5/2015
 */
public class RtmpdfnData implements ILogData {
    private int id;                 //自增id
    private String timestamp;       //时间戳
    private String streamId;        //流id
    private String sp;              //码率
    private String bw;              //流量
    private String cip;             //目标ip
    private String sip;             //服务端ip
    private String dfn;             //丢掉的帧数
    private String tmd;             //丢帧的时间间隔
    private String is;              //代表inner server，0代表是用户请求，1代表内部服务器之间

    public RtmpdfnData() {
    }

    public RtmpdfnData(Map map) {
        mapToILogData(map);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    public String getBw() {
        return bw;
    }

    public void setBw(String bw) {
        this.bw = bw;
    }

    public String getCip() {
        return cip;
    }

    public void setCip(String cip) {
        this.cip = cip;
    }

    public String getSip() {
        return sip;
    }

    public void setSip(String sip) {
        this.sip = sip;
    }

    public String getDfn() {
        return dfn;
    }

    public void setDfn(String dfn) {
        this.dfn = dfn;
    }

    public String getTmd() {
        return tmd;
    }

    public void setTmd(String tmd) {
        this.tmd = tmd;
    }

    public String getIs() {
        return is;
    }

    public void setIs(String is) {
        this.is = is;
    }

    public String toLine() {
        return StringUtils.join(
                new String[]{timestamp, streamId, sp, bw, cip, sip, dfn, tmd, String.valueOf(is)},
                LogReportConstants.TAB);
    }

    public boolean mapToILogData(Map map) {
        if (isValid(map)) {
            timestamp = map.get("timestamp") == null ? "" : map.get("timestamp").toString();
            streamId = map.get("streamid") == null ? "" : map.get("streamid").toString();
            sp = map.get("sp") == null ? "" : map.get("sp").toString();
            bw = map.get("bw") == null ? "" : map.get("bw").toString();
            cip = map.get("cip") == null ? "" : map.get("cip").toString();
            sip = map.get("sip") == null ? "" : map.get("sip").toString();
            dfn = map.get("dfn") == null ? "" : map.get("dfn").toString();
            tmd = map.get("tmd") == null ? "" : map.get("tmd").toString();
            is = map.get("tmd") == null ? "0" : map.get("is").toString();

            return true;
        }
        return false;
    }

    /**
     * 校验必备的参数是否齐全
     *
     * @param map
     * @return
     */
    private boolean isValid(Map map) {
        String[] keys = new String[]{"timestamp", "streamid", "dfn", "tmd", "is"};
        for (String key : keys) {
            if (!map.containsKey(key)) {
                return false;
            }
        }
        return true;
    }
}
