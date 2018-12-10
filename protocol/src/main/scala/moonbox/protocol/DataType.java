package moonbox.protocol;

import java.util.Locale;

public enum DataType {

    /**
     *   BOOLEAN <=> java.lang.Boolean
     *   BYTE <=> java.lang.Byte
     *   SHORT <=> java.lang.Short
     *   INTEGER <=> java.lang.Integer
     *   FLOAT <=> java.lang.Float
     *   DOUBLE <=> java.lang.Double
     *   STRING <=> String
     *   DECIMAL <=> java.math.BigDecimal
     *
     *   DATE <=> java.sql.Date
     *   TIMESTAMP <=> java.sql.Timestamp
     *
     *   BINARY <=> byte array
     *   ARRAY <=> scala.collection.Seq (use getList for java.util.List)
     *   MAP <=> scala.collection.Map (use getJavaMap for java.util.Map)
     *   STRUCT <=> Row
     */

    BINARY("binary"),
    BOOLEAN("boolean"),
    DATE("date"),
    CHAR("char"),
    VARCHAR("varchar"),
    DOUBLE("double"),
    FLOAT("float"),
    BYTE("byte"),
    INTEGER("integer"),
    LONG("long"),
    SHORT("short"),
    STRING("string"),
    TIMESTAMP("timestamp"),
    NULL("null"),
    /* decimal(precision,scale) */
    DECIMAL("decimal"),
    OBJECT("object"),
    STRUCT("struct"),
    /* map(keyType,valueType,valueContainsNull) */
    MAP("map"),
    /* array(elementType,containsNull) */
    ARRAY("array");

    private final String name;

    public String getName() {
        return name;
    }

    DataType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DataType(" + name + ")";
    }

    public static DataType getDataType(String name) {
        String lowerCaseName = name.toLowerCase(Locale.ROOT);
        if (lowerCaseName.startsWith("decimal")) {
            return DECIMAL;
        } else if (lowerCaseName.startsWith("map")) {
            return MAP;
        } else if (lowerCaseName.startsWith("array")) {
            return ARRAY;
        } else if (lowerCaseName.startsWith("struct")) {
            return STRUCT;
        }
        switch (lowerCaseName) {
            case "binary":
                return BINARY;
            case "boolean":
                return BOOLEAN;
            case "date":
                return DATE;
            case "char":
                return CHAR;
            case "varchar":
                return VARCHAR;
            case "double":
                return DOUBLE;
            case "float":
                return FLOAT;
            case "byte":
                return BYTE;
            case "integer":
                return INTEGER;
            case "long":
                return LONG;
            case "short":
                return SHORT;
            case "string":
                return STRING;
            case "timestamp":
                return TIMESTAMP;
            case "null":
                return NULL;
            case "object":
                return OBJECT;
            default:
                throw new IllegalArgumentException("Type name mismatch.");
        }
    }
}
