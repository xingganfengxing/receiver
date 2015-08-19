package com.letv.cdn.receiver.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.common.Env;

public final class JdbcUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcUtils.class);
    private static DataSource myDataSource = null;
    private static DataSource rtmpDfnDataSource = null;

    static {
        InputStream is = null;
        try {
            Properties prop = new Properties();
            prop.setProperty("driverClassName", Env.get("mysql.jdbc.driver"));
            prop.setProperty("url", Env.get("mysql.jdbc.url"));
            prop.setProperty("username", Env.get("mysql.jdbc.username"));
            prop.setProperty("password", Env.get("mysql.jdbc.password"));

            is = JdbcUtils.class.getClassLoader().getResourceAsStream("dbcpconfig.properties");
            prop.load(is);

            myDataSource = BasicDataSourceFactory.createDataSource(prop);

            prop.setProperty("url", Env.get("rtmpdfn.mysql.jdbc.url"));
            prop.setProperty("username", Env.get("rtmpdfn.mysql.jdbc.username"));
            prop.setProperty("password", Env.get("rtmpdfn.mysql.jdbc.password"));

            rtmpDfnDataSource = BasicDataSourceFactory.createDataSource(prop);

        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    private JdbcUtils() {

    }

    public static DataSource getDataSource() {
        return myDataSource;
    }

    public static DataSource getRtmpDfnDataSource() {
        return rtmpDfnDataSource;
    }

    public static Connection getConnection() throws SQLException {

        return myDataSource.getConnection();
    }

    public static void free(ResultSet rs, Statement st, Connection conn) {

        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                    }
                }

            }
        }
    }

}
