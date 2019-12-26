package com.study.tx.dispatch.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DispatchService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void dispatch(String orderId) throws SQLException {
        String sql = "insert into t_dispatch values(null,?,?)";
        int state = jdbcTemplate.update(sql, orderId, "派送此订单");
        if (state != 1) {
            throw new SQLException("调度数据插入失败，[数据库原因]");
        }
    }
}
