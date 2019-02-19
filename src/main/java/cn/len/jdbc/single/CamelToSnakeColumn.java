package cn.len.jdbc.single;

import java.lang.reflect.Field;

/**
 * @author len
 * 2019-02-19
 */
public class CamelToSnakeColumn extends DefaultColumn {

    public CamelToSnakeColumn(Field field) {
        super(field);
    }
}
