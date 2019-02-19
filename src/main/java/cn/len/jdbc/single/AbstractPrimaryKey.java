package cn.len.jdbc.single;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import java.lang.reflect.Field;

/**
 * @author len
 * 2019-02-19
 */
public abstract class AbstractPrimaryKey implements PrimaryKeyMeta {
    private boolean flag;
    private final String name;
    private final String javaName;

    public AbstractPrimaryKey(Field field) {
        final GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
        flag = generatedValue != null;
        name = ColumnUtils.getColumnName(field);
        javaName = field.getName();
    }

    @Override
    public boolean auto() {
        return flag;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getJavaName() {
        return javaName;
    }
}
