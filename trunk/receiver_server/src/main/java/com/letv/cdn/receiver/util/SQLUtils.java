package com.letv.cdn.receiver.util;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SQLUtils{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLUtils.class);
    
    // sql
    // private static final String FLV_SERVER_SQL =
    // "insert into flv_server (ptime,stream,province,online,bandwidth) values (?,?,?,?,?) ON DUPLICATE KEY UPDATE online = online+VALUES(online),bandwidth = bandwidth+VALUES(bandwidth) ";
    private static final String INSERT_VOD_BANDWIDTH_SERVER_SQL = " insert into vod_bandwidth_server_{0} (ptime,splatid,platid,sip,host,bandwidth) values (?,?,?,?,?,?) ";
    
    // sql type
    public static final int VOD_BANDWIDTH_SERVER = 0X00;
    
    private static final int TABLE_DAILY = 8;
    
    private SQLUtils() {
    
    }
    
    public static void insert(String log, int sqlType) throws SQLException {
    
        switch (sqlType) {
            case VOD_BANDWIDTH_SERVER:
                istVodBandwidthTable(log, INSERT_VOD_BANDWIDTH_SERVER_SQL);
                break;
        }
    }
    
    private static void istVodBandwidthTable(String log, String sql) throws SQLException {
    
        QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
        String[] params = log.split(",");
        String timePrefix = params[0].substring(0, TABLE_DAILY);
        runner.update(sql.replace("{0}", timePrefix), params);
    }
    
}
