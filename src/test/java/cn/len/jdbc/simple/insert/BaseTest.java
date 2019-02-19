package cn.len.jdbc.simple.insert;

import cn.len.jdbc.single.SingleJdbcTemplate;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * @author len
 * 2019-02-19
 */
public class BaseTest {
    protected static SingleJdbcTemplate template;
    @BeforeAll
    static void setUp() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
       final String url = "jdbc:mysql:///jdbc_test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8";
        final String username = "root";
        final String password = "123456";
        Class.forName("com.mysql.cj.jdbc.Driver");
        final Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
        final java.sql.Driver driver = drivers.nextElement();
        final Connection connection = DriverManager.getConnection(url, username, password);
        SimpleDriverDataSource driverManagerDataSource = new SimpleDriverDataSource(driver, url, username, password);
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(driverManagerDataSource);

        template = new SingleJdbcTemplate(jdbcTemplate);
    }
}
