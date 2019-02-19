package cn.len.jdbc.single;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import java.lang.reflect.Field;

/**
 * @author len
 * 2019-02-19
 */
public abstract class AbstractColumn implements ColumnMeta {

    private String columnName;

    private String fieldName;
    @Override
    public String getName() {
        return columnName;
    }

    @Override
    public String getJavaName() {
        return fieldName;
    }

    public AbstractColumn(Field field) {
        fieldName = field.getName();
        columnName = ColumnUtils.getColumnName(field);
    }

    protected void parseField(Field field) {

    }

    protected void setName(String columnName) {
        this.columnName = columnName;
    }
}
