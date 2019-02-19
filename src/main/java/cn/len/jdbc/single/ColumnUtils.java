package cn.len.jdbc.single;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import java.lang.reflect.Field;

/**
 * @author len
 * 2019-02-19
 */
public class ColumnUtils {

    public static String getColumnName(Field field) {
        String name = field.getName();
        final Column column = field.getAnnotation(Column.class);
        if (column != null && StringUtils.hasText(column.name())) {
            name = column.name();
        }
        return name;
    }
}
