package cn.len.jdbc.single;

import java.lang.reflect.Field;

/**
 * @author len
 * 2019-02-19
 */
public class DefaultTable<T> extends AbstractTable<T> {
    public DefaultTable(Class<T> clazz) {
        super(clazz);
    }

    @Override
    ColumnMeta getColumn(Field field) {
        return new DefaultColumn(field);
    }

    @Override
    PrimaryKeyMeta getPrimaryKey(Field field) {
        return new DefaultPrimaryKey(field);
    }


}
