package cn.len.jdbc.single;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author len
 * 2019-02-19
 */
public class SingleJdbcTemplate implements SingleJdbcOperations {

    private final NamedParameterJdbcTemplate template;

    public SingleJdbcTemplate(DataSource dataSource) {
        this(new JdbcTemplate(dataSource));
    }

    public SingleJdbcTemplate(NamedParameterJdbcTemplate template) {

        this.template = template;
    }

    public SingleJdbcTemplate(JdbcTemplate JdbcTemplate) {
        this(new NamedParameterJdbcTemplate(JdbcTemplate));
    }

    private Map<String, TableMeta> tables = new ConcurrentHashMap<>(32);

    @Override
    public <T> int batchInsert(Class<T> clazz, List<T> rows) {

        return 0;
    }

    @Override
    public int insert(Object row, boolean key) {
        int affected = 0;
        final NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(new SimpleDriverDataSource());
        final Class<?> aClass = row.getClass();
        final String name = aClass.getName();
        TableMeta<?> tableMeta = tables.get(name);
        if (tableMeta == null) {
            TableMeta<?> defaultTable = new DefaultTable<>(row.getClass());
            tables.put(name, defaultTable);
        }
        tableMeta = tables.get(name);
        if (key) {
            final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            final String sql = generateInsertSql(tableMeta);
            final List<PrimaryKeyMeta> primaryKeyMetas = tableMeta.getPrimaryKeyMetas();
            final List<String> columns = primaryKeyMetas.stream().map(Meta::getName).collect(Collectors.toList());
            final List<String> fields = primaryKeyMetas.stream().map(Meta::getJavaName).collect(Collectors.toList());
            if (primaryKeyMetas.isEmpty()) {
                template.update(generateInsertSql(tableMeta), new BeanPropertySqlParameterSource(row));
            }
            if (primaryKeyMetas.size() == 1) {
                affected = template.update(sql, new BeanPropertySqlParameterSource(row), generatedKeyHolder);
                final String idFiled = columns.get(0);
            } else {
                final String[] strings = columns.toArray(new String[]{});
                affected = template.update(sql, new BeanPropertySqlParameterSource(row), generatedKeyHolder, strings);
            }
            setPrimary(row, aClass, generatedKeyHolder, columns, fields);
        } else {
            affected = template.update(generateInsertSql(tableMeta), new BeanPropertySqlParameterSource(row));
        }
        return affected;
    }

    private void setPrimary(Object row, Class<?> aClass, GeneratedKeyHolder generatedKeyHolder, List<String> columns, List<String> fields) {
        Map<String, Object> keys = generatedKeyHolder.getKeys();
        if (keys == null) {
            return;
        }
        for (int i = 0; i < columns.size(); i++) {
            String idColumn = columns.get(i);
            String idFiled = fields.get(i);
            Number number = (Number) keys.get(idColumn);
            if (number == null) {
                return;
            }
            final Field field = ReflectionUtils.findField(aClass, idFiled);
            if (field == null) {
                return;
            }
            final Class<?> type = field.getType();
            final Method method = ReflectionUtils.findMethod(aClass, "set" + idFiled, type);
            if (method == null) {
                return;
            }
            try {
                if (type == Integer.class) {
                    method.invoke(row, number.intValue());
                } else if (type == Long.class) {
                    method.invoke(row, number.longValue());
                } else if (type == Short.class) {
                    method.invoke(row, number.shortValue());
                } else if (type == Byte.class) {
                    method.invoke(row, number.byteValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public <T> int update(Class<T> clazz, T row) {
        return 0;
    }

    @Override
    public <T> int delete(Class<T> clazz, T row) {
        return 0;
    }

    @Override
    public <T> T select(Class<T> clazz, T row) {
        return null;
    }

    @Override
    public void initMeta() {

    }

    private String generateInsertSql(TableMeta<?> tableMeta) {
        StringBuilder insertBuilder = new StringBuilder();
        StringBuilder valueBuilder = new StringBuilder();
        insertBuilder.append("insert into(");
        valueBuilder.append("values(");
        final List<PrimaryKeyMeta> primaryKeyMetas = tableMeta.getPrimaryKeyMetas();
        final List<ColumnMeta> columns = tableMeta.getColumns();

        if (!primaryKeyMetas.isEmpty()) {
            for (PrimaryKeyMeta pkm : primaryKeyMetas) {
                if (!pkm.auto()) {
                    insertBuilder.append(pkm.getName()).append(",");
                    valueBuilder.append(":").append(pkm.getJavaName()).append(",");
                }
            }
        }

        if (!columns.isEmpty()) {
            for (int i = 0; i < columns.size(); i++) {
                ColumnMeta cm = columns.get(i);
                insertBuilder.append(cm.getName());
                valueBuilder.append(":").append(cm.getJavaName());
                if (i != columns.size() - 1) {
                    insertBuilder.append(",");
                    valueBuilder.append(",");
                }
            }
        } else {
            insertBuilder = new StringBuilder(insertBuilder.substring(0, insertBuilder.length() - 1));
            valueBuilder = new StringBuilder(valueBuilder.substring(0, valueBuilder.length() - 1));
        }
        insertBuilder.append(")").append(" ");
        valueBuilder.append(")");
        return insertBuilder.toString() + valueBuilder.toString();
    }
}
