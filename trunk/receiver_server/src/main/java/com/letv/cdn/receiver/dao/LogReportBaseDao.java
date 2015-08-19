package com.letv.cdn.receiver.dao;

import com.letv.cdn.receiver.model.ILogData;
import com.letv.cdn.receiver.util.JdbcUtils;
import com.letv.cdn.receiver.util.LogReportConstants;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author liufeng1
 * @date 6/5/2015
 */
public class LogReportBaseDao {
    private QueryRunner runner = new QueryRunner(JdbcUtils.getRtmpDfnDataSource());

    public void insertBatch(String sql,List<ILogData> list) throws SQLException {
        runner.getDataSource().getConnection().setAutoCommit(false);
        runner.getDataSource().getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        Object[][] params = new Object[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            params[i] = list.get(i).toLine().split(LogReportConstants.TAB);
        }

        runner.batch(sql, params);
        runner.getDataSource().getConnection().commit();
    }
}
