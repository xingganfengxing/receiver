package com.letv.cdn.receiver.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


/**
 * 
 * @author liufeng1
 *
 */
@Component
public class BaseDao{
    
    //mysql连接
    @Autowired
    public JdbcTemplate jdbcTemplate;

}