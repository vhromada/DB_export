package cz.vhromada.export.extractor.impl;

import java.sql.Connection;

import cz.vhromada.export.api.entities.ColumnType;
import cz.vhromada.export.extractor.Extractor;

/**
 * A class represents extractor for H2 DB.
 *
 * @author Vladimir Hromada
 */
public class H2Extractor extends Extractor {

    /**
     * Creates a new instance of H2Extractor.
     *
     * @param connection connection
     * @throws IllegalArgumentException if connection is null
     */
    public H2Extractor(final Connection connection) {
        super(connection);
    }

    @Override
    protected String getDatabaseTableSQL() {
        return "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA LIKE 'PUBLIC%'";
    }

    @Override
    protected String getDatabaseColumnSQL() {
        return "SELECT COLUMN_NAME, TYPE_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?";
    }

    @Override
    protected ColumnType convertColumn(final String column) {
        switch (column.toUpperCase()) {
            case "BOOLEAN":
                return ColumnType.BOOLEAN;
            case "TINYINT":
            case "SMALLINT":
            case "INTEGER":
            case "BIGINT":
            case "REAL":
            case "DOUBLE":
            case "DECIMAL":
                return ColumnType.DECIMAL;
            case "VARCHAR":
            case "VARCHAR_IGNORECASE":
            case "CHAR":
                return ColumnType.STRING;
            case "DATE":
                return ColumnType.DATE;
            case "TIME":
                return ColumnType.TIME;
            case "TIMESTAMP":
                return ColumnType.TIMESTAMP;
            default:
                return ColumnType.OBJECT;
        }
    }

}
