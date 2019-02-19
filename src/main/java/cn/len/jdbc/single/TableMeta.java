package cn.len.jdbc.single;

import java.util.List;

/**
 * @author len
 * 2019-02-19
 */
public interface TableMeta<T> extends Meta {

    List<PrimaryKeyMeta> getPrimaryKeyMetas();


    ColumnMeta getColumn(String fieldName);

    List<ColumnMeta> getColumns();

}
