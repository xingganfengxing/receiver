package com.letv.cdn.receiver.dao;

import com.letv.cdn.receiver.model.ILogData;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author liufeng1
 * @date 6/5/2015
 */
public class RtmpdfnDaoLogReport extends LogReportBaseDao {

    private static final LinkedBlockingQueue<ILogData> QUEUE = new LinkedBlockingQueue<ILogData>(10000);

    public static LinkedBlockingQueue<ILogData> getQueue() {
        return QUEUE;
    }

    public void insertBatch(List<ILogData> list) throws SQLException {
        String sql = "insert into cloud_live_rtmpdfn(timestamp,streamid,sp,bw,cip,sip,dfn,tmd) value(?,?,?,?,?,?,?,?)";
        insertBatch(sql,list);
    }
}
