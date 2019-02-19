package cn.len.jdbc.simple.insert;

import cn.len.jdbc.single.SingleJdbcTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

/**
 * @author len
 * 2019-02-19
 */
@DisplayName("插入测试")
 class InsertTest  {
    @Test
    void testInsertNoAuto() throws Exception {
        final User user = new User();
        user.setPassword("123");
        user.setUserId("123");
        user.setUsername("234");
        final String url = "jdbc:mysql:///jdbc_test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8";
        final String username = "root";
        final String password = "123456";
        Class.forName("com.mysql.cj.jdbc.Driver");
        final Enumeration<Driver> drivers = DriverManager.getDrivers();
        final java.sql.Driver driver = drivers.nextElement();
        SimpleDriverDataSource driverManagerDataSource = new SimpleDriverDataSource(driver, url, username, password);
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(driverManagerDataSource);

        final SingleJdbcTemplate singleJdbcTemplate = new SingleJdbcTemplate(jdbcTemplate);
        singleJdbcTemplate.insert(url, false);

    }
}
