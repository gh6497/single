package cn.len.jdbc.single;

import java.lang.reflect.Field;

/**
 * @author len
 * 2019-02-19
 */
public class DefaultPrimaryKey extends AbstractPrimaryKey {


    public DefaultPrimaryKey(Field field) {
        super(field);
    }
}
