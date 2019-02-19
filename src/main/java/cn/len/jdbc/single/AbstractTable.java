package cn.len.jdbc.single;


import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author len
 * 2019-02-19
 */
public abstract class AbstractTable<T> implements TableMeta<T> {
    protected final Class<T> clazz;
    private String tableName;


    private final List<PrimaryKeyMeta> primaryKeyMetas = new ArrayList<>(16);
    private final List<ColumnMeta> columnMetas = new ArrayList<>(2);


    public AbstractTable(Class<T> clazz) {
        this.clazz = clazz;
        init();
    }

    private Map<String, String> columns = new HashMap<>(16);

    @Override
    public ColumnMeta getColumn(String fieldName) {
        return columnMetas.stream().filter(cm ->
                cm.getJavaName().equals(fieldName)
        ).findFirst().orElseThrow(() -> new RuntimeException("未知异常"));
    }

    @Override
    public String getName() {
        return tableName;
    }

    @Override
    public String getJavaName() {
        return clazz.getName();
    }

    private void init() {
        initTableName();
        buildColumn(clazz);
    }

    private void initPrimaryKey(Class<T> clazz) {

    }

    /**
     * 初始化列元数据,由子类实现
     */
    protected void buildColumn(Class<T> clazz) {

        final Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (isIdColumn(field)) {
                primaryKeyMetas.add(getPrimaryKey(field));
                continue;
            }
            columnMetas.add(getColumn(field));
        }
    }

    protected void initTableName() {
        final Table table = AnnotationUtils.findAnnotation(clazz, Table.class);
        final String simpleName = clazz.getSimpleName();
        final StringBuilder fullTableName = new StringBuilder(simpleName);
        Optional.ofNullable(table).ifPresent(t -> {
            fullTableName.setLength(0);
            final String schema = t.schema();
            if (StringUtils.hasText(schema)) {
                fullTableName.append(schema).append(".");
            }
            final String name = t.name();
            if (StringUtils.hasText(name)) {
                fullTableName.append(name);
            }
        });
        tableName = fullTableName.toString();
    }


    protected void setName(String name) {
        tableName = name;
    }


    @Override
    public List<ColumnMeta> getColumns() {
        return columnMetas;
    }

    @Override
    public List<PrimaryKeyMeta> getPrimaryKeyMetas() {
        return primaryKeyMetas;
    }

    abstract ColumnMeta getColumn(Field field);

    abstract PrimaryKeyMeta getPrimaryKey(Field field);

    private boolean isIdColumn(Field field) {
        return field.getAnnotation(Id.class) != null;
    }

}
