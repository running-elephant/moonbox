package moonbox.jdbc;

import moonbox.protocol.protobuf.*;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class MoonboxResult {
  private MoonboxConnection conn;
  private MoonboxStatement statement;
  private SchemaPB schema;
  private Map<String, Integer> columnNameToIndex;
  private boolean hasMoreData;
  private List<RowPB> currentData;
  private Object[] currentRow;
  private int columnCount;
  private int rowIndex;

  MoonboxResult(MoonboxConnection conn, MoonboxStatement statement, ExecutionResultPB resultPB) {
    this.conn = conn;
    this.statement = statement;
    this.hasMoreData = resultPB.getHasMore();
    this.schema = resultPB.getData().getSchema();
    this.columnNameToIndex = new HashMap<>();

    for(int i=0; i < schema.getColumnsCount(); i++) {
      columnNameToIndex.put(schema.getColumns(i).getName(), i);
    }

    this.currentData = resultPB.getData().getRowList();
    this.columnCount = schema.getColumnsCount();
  }

  public boolean next() throws Exception {
    if (rowIndex < currentData.size()) {
      currentRow = convertRow(currentData.get(rowIndex), schema);
      rowIndex ++;
      return true;
    } else if (hasMoreData) {
      fetchMoreData();
      return next();
    } else {
      return false;
    }
  }

  private void fetchMoreData() throws Exception {
    checkClosed();
    ExecutionResultPB next = conn.getClient().next(statement.getQueryTimeout());
    this.hasMoreData = next.getHasMore();
    this.currentData = next.getData().getRowList();
    this.rowIndex = 0;
  }

  @SuppressWarnings("unchecked")
  public <T> T get(int columnIndex) {
    checkColumnIndex(columnIndex);
    return (T) currentRow[columnIndex];
  }

  public int getIndex(String columnName) {
    checkColumnName(columnName);
    return columnNameToIndex.get(columnName);
  }

  public SchemaPB getSchema() {
    return this.schema;
  }


  private void checkColumnIndex(int columnIndex) {
    if (columnIndex < 1 || columnIndex > columnCount) {
      throw new IllegalArgumentException("ColumnIndex out of range 0 to " + columnCount);
    }
  }

  private void checkColumnName(String columnName) {
    if (!columnNameToIndex.containsKey(columnName)) {
      throw new NoSuchElementException(columnName);
    }
  }

  void checkClosed() throws Exception {
    if (conn != null) {
      conn.checkClosed();
    }
  }

  public String getColumnTypeName(int columnIndex) {
    checkColumnIndex(columnIndex);
    return JDBCType.valueOf(convertTypeToSQLType(schema.getColumns(columnIndex).getDataType())).getName();
  }

  public String getColumnName(int columnIndex) {
    checkColumnIndex(columnIndex);
    return schema.getColumns(columnIndex).getName();
  }

  public int getColumnType(int columnIndex) {
    checkColumnIndex(columnIndex);
    return convertTypeToSQLType(schema.getColumns(columnIndex).getDataType());
  }

  public static Object[] convertRow(RowPB row, SchemaPB schema) {
    Object[] objects = new Object[row.getFieldCount()];
    for (int i = 0; i < row.getFieldCount(); i++) {
      objects[i] = convertValue(row.getField(i), schema.getColumns(i));
    }
    return objects;
  }

  public static Object convertValue(Value value, ColumnSchemaPB columnSchemaPB) {
    switch (columnSchemaPB.getDataType()) {
      case BYTE:
        return (byte) value.getIntValue();
      case BOOLEAN:
        return value.getBooleanValue();
      case INT:
        return value.getIntValue();
      case SHORT:
        return (short) value.getIntValue();
      case LONG:
        return value.getLongValue();
      case FLOAT:
        return value.getFloatValue();
      case DOUBLE:
        return value.getDoubleValue();
      case DECIMAL:
        Decimal bigDecimal = value.getBigDecimal();
        long longValue = bigDecimal.getValue();
        int scale = columnSchemaPB.getAttribute().getScale();
        return BigDecimal.valueOf(longValue, scale);
      case CHAR:
        return (char) value.getIntValue();
      case VARCHAR:
        return value.getStringValue();
      case STRING:
        return value.getStringValue();
      case BINARY:
        return value.getByteArray().toByteArray();
      case DATE:
        long date = value.getLongValue();
        return new Date(date);
      case TIMESTAMP:
        long time = value.getLongValue();
        return new Timestamp(time);
      case NULL:
        return null;
      case ARRAY:
        ListValue listValue = value.getListValue();
        columnSchemaPB.
        return ;
      case STRUCT:
        return Types.STRUCT;
      case MAP:
        return Types.JAVA_OBJECT;
      case OBJECT:
        return Types.JAVA_OBJECT;
      case UNKNOWN_DATA:
      default:
        return null;
    }
  }

  public static int convertTypeToSQLType(DataType dataType) {
    switch (dataType) {
      case BYTE:
        return Types.CHAR;
      case BOOLEAN:
        return Types.BOOLEAN;
      case INT:
        return Types.INTEGER;
      case SHORT:
        return Types.SMALLINT;
      case LONG:
        return Types.BIGINT;
      case FLOAT:
        return Types.FLOAT;
      case DOUBLE:
        return Types.DOUBLE;
      case DECIMAL:
        return Types.DECIMAL;
      case CHAR:
        return Types.CHAR;
      case VARCHAR:
        return Types.VARCHAR;
      case STRING:
        return Types.VARCHAR;
      case BINARY:
        return Types.BINARY;
      case DATE:
        return Types.DATE;
      case TIMESTAMP:
        return Types.TIMESTAMP;
      case NULL:
        return Types.NULL;
      case ARRAY:
        return Types.ARRAY;
      case STRUCT:
        return Types.STRUCT;
      case MAP:
        return Types.JAVA_OBJECT;
      case OBJECT:
        return Types.JAVA_OBJECT;
      case UNKNOWN_DATA:
      default:
        return Types.OTHER;
    }
  }

}
