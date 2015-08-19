package com.letv.cdn.receiver.dao;

import com.letv.cdn.receiver.model.LogData;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by liufeng1 on 8/4/2015.
 */
@Repository("cloudLivePresureDao")
public class CloudLivePresureDao extends BaseDao {

    private String createTable =
            "CREATE TABLE `cloud_live_presure` (\n" +
                    "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                    "  `logType` int(11) DEFAULT NULL COMMENT '日志类型',\n" +
                    "  `streamIndex` int(11) DEFAULT NULL COMMENT '流在任务中的序号',\n" +
                    "  `taskId` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '任务id',\n" +
                    "  `streamId` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '流ID',\n" +
                    "  `timestamp` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '任务id',\n" +
                    "  `clientIp` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '播放器所在机器ip',\n" +
                    "  `serverIp` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '播放器请求的服务器ip'," +
                    "  `loadingDuration` varchar(16) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '卡顿时长',\n" +
                    "  `originRoute` varchar(2048) DEFAULT '' COMMENT '回源路径',\n" +
                    "  `playbackDuration` varchar(16) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '播放时长', " +
                    "  PRIMARY KEY (`id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    private String batchInsert = " insert into cloud_live_presure" +
            "(logType,streamIndex,taskId,streamId,timestamp,clientIp,serverIp,loadingDuration,originRoute,playbackDuration) values (?,?,?,?,?,?,?,?,?,?) ";

    public void insertBatch(final List<LogData> logList) {

        jdbcTemplate.execute(batchInsert, new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.getConnection().setAutoCommit(false);
                for(LogData logData : logList){
                    ps.setInt(1, logData.getLogType());
                    ps.setInt(2, logData.getStreamIndex());
                    ps.setString(3, logData.getTaskId());
                    ps.setString(4, logData.getStreamId());
                    ps.setString(5, logData.getTimestamp());
                    ps.setString(6, logData.getClientIp());
                    ps.setString(7, logData.getServerIp());
                    ps.setString(8, logData.getLoadingDuration());
                    ps.setString(9, logData.getOriginRoute());
                    ps.setString(10, logData.getPlaybackDuration());
                    ps.addBatch();
                }

                Object object = ps.executeBatch();
                ps.getConnection().commit();
                ps.getConnection().setAutoCommit(true);
                return object;
            }
        });
    }

    public void insertOne(LogData logData) {
        jdbcTemplate.update(batchInsert,
                new Object[]{
                        logData.getLogType(),
                        logData.getStreamIndex(),
                        logData.getTaskId(),
                        logData.getStreamId(),
                        logData.getTimestamp(),
                        logData.getClientIp(),
                        logData.getServerIp(),
                        logData.getLoadingDuration(),
                        logData.getOriginRoute(),
                        logData.getPlaybackDuration()
                });
    }

    public void createCloudLivePresureTable(String day) {
        jdbcTemplate.execute(createTable);
    }
}
