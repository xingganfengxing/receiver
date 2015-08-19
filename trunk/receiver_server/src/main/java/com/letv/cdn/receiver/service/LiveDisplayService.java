package com.letv.cdn.receiver.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.letv.cdn.receiver.model.LiveData;

/**
 * 从数据库查询展示数据
 * 
 * @author wangzhenzhen
 *
 */
public class LiveDisplayService{
    public JdbcTemplate jdbcTemplate;
    String querySql = "select ptime,sum(online),sum(bandwidth) from cloud_live_{0} where ptime>=? and ptime < ? and stream = ? and province<>999  group by ptime";
    
    String queryClientSql = "select ptime,sum(online),sum(buffer) from client_live_{0} where ptime>=? and ptime < ? and stream = ? and province<>999  group by ptime";
    public String query(String startTime, String endTime, String streamid) {
        
        String day1 = startTime.substring(0, 8);
        String day2 = endTime.substring(0, 8);
        List<LiveData> list = new ArrayList<LiveData>();
        if (day1.compareTo(day2) == 0) {
            list.addAll(queryData(day1, startTime, endTime, streamid));
        } else {
            String endTimeDay1 = day2 + "0000";
            String startTimeDay2 = day2 + "0000";
            list.addAll(queryData(day2, startTimeDay2, endTime, streamid));
            list.addAll(queryData(day1, startTime, endTimeDay1, streamid));
        }
        
        Map<String, LiveData> maps = new TreeMap<String, LiveData>(new Comparator<String>(){
            public int compare(String obj1, String obj2) {
                return obj2.compareTo(obj1); // 降序排序
            }
        });
        
        for(LiveData data : list){
            maps.put(data.getPtime(), data);
        }

        StringBuilder sb = new StringBuilder();
        for ( LiveData data : maps.values()) {
            sb.append(data.toString()).append("</br>");
        }
        return sb.toString();
    }
    
    
 public String queryClient(String startTime, String endTime, String streamid) {
        
        String day1 = startTime.substring(0, 8);
        String day2 = endTime.substring(0, 8);
        List<LiveData> list = new ArrayList<LiveData>();
        if (day1.compareTo(day2) == 0) {
            list.addAll(queryClientData(day1, startTime, endTime, streamid));
        } else {
            String endTimeDay1 = day2 + "0000";
            String startTimeDay2 = day2 + "0000";
            list.addAll(queryClientData(day2, startTimeDay2, endTime, streamid));
            list.addAll(queryClientData(day1, startTime, endTimeDay1, streamid));
        }
        
        Map<String, LiveData> maps = new TreeMap<String, LiveData>(new Comparator<String>(){
            public int compare(String obj1, String obj2) {
                return obj2.compareTo(obj1); // 降序排序
            }
        });
        
        for(LiveData data : list){
            maps.put(data.getPtime(), data);
        }

        StringBuilder sb = new StringBuilder();
        for ( LiveData data : maps.values()) {
            sb.append(data.toString()).append("</br>");
        }
        return sb.toString();
    }
 
 
    private  List<LiveData> queryData(String day, String startTime, String endTime, String streamid) {
        String sql = MessageFormat.format(querySql, day);
        List<LiveData> list = jdbcTemplate.query(sql, new String[] { startTime, endTime, streamid }, new RowMapper(){
            @Override
            public LiveData mapRow(ResultSet rs, int arg1) throws SQLException {
                String ptime = rs.getObject(1).toString();
                Long visit = Long.valueOf(rs.getObject(2).toString());
                Long bw =Long.valueOf(rs.getObject(3).toString());
                LiveData data = new LiveData(ptime, visit, bw);
                return data;
            }
        });
        return list;
    }
    
    
    private  List<LiveData> queryClientData(String day, String startTime, String endTime, String streamid) {
        String sql = MessageFormat.format(queryClientSql, day);
        List<LiveData> list = jdbcTemplate.query(sql, new String[] { startTime, endTime, streamid }, new RowMapper(){
            @Override
            public LiveData mapRow(ResultSet rs, int arg1) throws SQLException {
                String ptime = rs.getObject(1).toString();
                Long visit = Long.valueOf(rs.getObject(2).toString());
                Long bw =Long.valueOf(rs.getObject(3).toString());
                LiveData data = new LiveData(ptime, visit, bw);
                return data;
            }
        });
        return list;
    }
    
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
