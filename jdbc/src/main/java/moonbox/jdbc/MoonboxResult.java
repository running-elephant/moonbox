package moonbox.jdbc;

import moonbox.protocol.protobuf.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class MoonboxResult {
  private MoonboxConnection conn;
  private MoonboxStatement statement;
  private StructTypePB schema;
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
    this.columnCount = schema.getFieldsCount();
    this.columnNameToIndex = new HashMap<>();

    for(int i=0; i < columnCount; i++) {
      columnNameToIndex.put(schema.getFields(i).getName(), i + 1);
    }

    this.currentData = resultPB.getData().getRowsList();

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
    this.currentData = next.getData().getRowsList();
    this.rowIndex = 0;
  }

  @SuppressWarnings("unchecked")
  public <T> T get(int columnIndex) {
    checkColumnIndex(columnIndex);
    return (T) currentRow[columnIndex - 1];
  }

  public int getIndex(String columnName) {
    checkColumnName(columnName);
    return columnNameToIndex.get(columnName);
  }

  private void checkColumnIndex(int columnIndex) {
    if (columnIndex < 1 || columnIndex > columnCount) {
      throw new IllegalArgumentException("ColumnIndex out of range 1 to " + columnCount);
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
    return JDBCType.valueOf(convertTypeToSQLType(schema.getFields(columnIndex - 1).getDataType())).getName();
  }

  public String getColumnName(int columnIndex) {
    checkColumnIndex(columnIndex);
    return schema.getFields(columnIndex - 1).getName();
  }

  public int getColumnType(int columnIndex) {
    checkColumnIndex(columnIndex);
    return convertTypeToSQLType(schema.getFields(columnIndex - 1).getDataType());
  }

  public int getColumnCount() {
    return columnCount;
  }

  public int getPrecision(int column) {
    checkColumnIndex(column);
    DataTypePB dataType = schema.getFields(column - 1).getDataType();
    if (dataType.hasDecimalType()) {
      return dataType.getDecimalType().getPrecision();
    } else {
      return 0;
    }
  }

  public int getScale(int column) {
    checkColumnIndex(column);
    DataTypePB dataType = schema.getFields(column - 1).getDataType();
    if (dataType.hasDecimalType()) {
      return dataType.getDecimalType().getScale();
    } else {
      return 0;
    }
  }

  public static Object[] convertRow(RowPB row, StructTypePB schema) {
    Object[] objects = new Object[row.getFieldsCount()];
    for (int i = 0; i < row.getFieldsCount(); i++) {
      objects[i] = convertValue(row.getFields(i), schema.getFields(i).getDataType());
    }
    return objects;
  }

  public static Object convertValue(Value value, DataTypePB dt) {
    if (dt.hasByteType()) {
      return (byte) value.getIntValue();
    } else if (dt.hasShortType()) {
      return (short) value.getIntValue();
    } else if (dt.hasIntegerType()) {
      return value.getIntValue();
    } else if (dt.hasLongType()) {
      return value.getLongValue();
    } else if (dt.hasFloatType()) {
      return value.getFloatValue();
    } else if (dt.hasDoubleType()) {
      return value.getDoubleValue();
    } else if (dt.hasDecimalType()) {
      BigDecimalPB bigDecimal = value.getBigDecimalValue();
      int scale = dt.getDecimalType().getScale();
      return new BigDecimal(new BigInteger(bigDecimal.getBigInteger().getIntVals().toByteArray()), scale);
    } else if (dt.hasBooleanType()) {
      return value.getBooleanValue();
    } else if (dt.hasCharType()) {
      return (char) value.getIntValue();
    } else if (dt.hasVarcharType()) {
      return value.getStringValue();
    } else if (dt.hasStringType()) {
      return value.getStringValue();
    } else if (dt.hasDateType()) {
      long date = value.getLongValue();
      return new Date(date);
    } else if (dt.hasTimestampType()) {
      long time = value.getLongValue();
      return new Timestamp(time);
    } else if (dt.hasBinaryType()) {
      return value.getBytesValue().toByteArray();
    } else if (dt.hasArrayType()) { // TODO
      DataTypePB elementType = dt.getArrayType().getElementType();
      ArrayPB arrayValue = value.getArrayValue();
      int valuesCount = arrayValue.getValuesCount();
      Object[] objects = new Object[valuesCount];
      for (int i = 0; i < valuesCount; i++) {
        objects[i] = convertValue(arrayValue.getValues(i), elementType);
      }
      return objects;
    } else if (dt.hasMapType()) {
      Map<String, Value> valuesMap = value.getMapValue().getValuesMap();
      DataTypePB keyType = dt.getMapType().getKeyType();
      DataTypePB valueType = dt.getMapType().getValueType();
      Map<String, Object> objects = new HashMap<>();
      for (Map.Entry<String, Value> entry: valuesMap.entrySet()) {
        objects.put(entry.getKey(), convertValue(entry.getValue(), valueType));
      }
      return objects;
    } else if (dt.hasStrucType()) {
      StructTypePB strucType = dt.getStrucType();
      int fieldsCount = strucType.getFieldsCount();
      StructPB structValue = value.getStructValue();

      Object[] objects = new Object[fieldsCount];
      for (int i = 0; i < fieldsCount; i++) {
        objects[i] = convertValue(structValue.getRow().getFields(i), strucType.getFields(i).getDataType());
      }
      return objects;
    } else if (dt.hasObjectType()) { // TODO
      return value.getBytesValue().toByteArray();
    } else if (dt.hasNullType()) {
      return null;
    } else {
      throw new IllegalArgumentException("Unknown dataType: " + dt.getTypeName());
    }
  }

  public static int convertTypeToSQLType(DataTypePB dt) {
    if (dt.hasByteType()) {
      return Types.CHAR;
    } else if (dt.hasShortType()) {
      return Types.SMALLINT;
    } else if (dt.hasIntegerType()) {
      return Types.INTEGER;
    } else if (dt.hasLongType()) {
      return Types.BIGINT;
    } else if (dt.hasFloatType()) {
      return Types.FLOAT;
    } else if (dt.hasDoubleType()) {
      return Types.DOUBLE;
    } else if (dt.hasDecimalType()) {
      return Types.DECIMAL;
    } else if (dt.hasBooleanType()) {
      return Types.BOOLEAN;
    } else if (dt.hasCharType()) {
      return Types.CHAR;
    } else if (dt.hasVarcharType()) {
      return Types.VARCHAR;
    } else if (dt.hasStringType()) {
      return Types.VARCHAR;
    } else if (dt.hasDateType()) {
      return Types.DATE;
    } else if (dt.hasTimestampType()) {
      return Types.TIMESTAMP;
    } else if (dt.hasBinaryType()) {
      return Types.BINARY;
    } else if (dt.hasArrayType()) {
      return Types.ARRAY;
    } else if (dt.hasMapType()) {
      return Types.JAVA_OBJECT;
    } else if (dt.hasStrucType()) {
      return Types.STRUCT;
    } else if (dt.hasObjectType()) {
      return Types.JAVA_OBJECT;
    } else if (dt.hasNullType()) {
      return Types.NULL;
    } else {
      throw new IllegalArgumentException("Unknown dataType: " + dt.getTypeName());
    }
  }

}
