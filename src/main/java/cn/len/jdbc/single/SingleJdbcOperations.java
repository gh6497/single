package cn.len.jdbc.single;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 定义单表操作的一系列接口
 * 本接口的子类依赖spring-jdbc的 {@link NamedParameterJdbcTemplate}
 * 为了方便做字段和bean属性的映射,参数bean需要要jpa标准注解标记
 *
 * @author len
 * 2019-02-19
 */
public interface SingleJdbcOperations {

    <T> int batchInsert(Class<T> clazz, List<T> rows);

     int insert(Object row,boolean key);

    <T> int update(Class<T> clazz, T row);

    <T> int delete(Class<T> clazz, T row);

    <T> T select(Class<T> clazz, T row);

   void initMeta();

}
