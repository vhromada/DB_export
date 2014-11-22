package cz.vhromada.export.extractor.impl;

import java.sql.Connection;

import cz.vhromada.export.api.entities.ColumnType;
import cz.vhromada.export.extractor.Extractor;

/**
 * A class represents extractor for HSQL DB.
 *
 * @author Vladimir Hromada
 */
public class HsqlExtractor extends Extractor {

    /**
     * Creates a new instance of HsqlExtractor.
     *
     * @param connection connection
     * @throws IllegalArgumentException if connection is null
     */
    public HsqlExtractor(final Connection connection) {
        super(connection);
    }

    /**
     * Returns database table extraction SQL.
     *
     * @return database table extraction SQL
     */
    @Override
    protected String getDatabaseTableSQL() {
        return "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA LIKE 'PUBLIC%'";
    }

    @Override
    protected String getDatabaseColumnSQL() {
        return "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?";
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
            case "DOUBLE PRECISION":
            case "NUMERIC":
            case "DECIMAL":
                return ColumnType.DECIMAL;
            case "CHARACTER":
            case "CHARACTER VARYING":
                return ColumnType.STRING;
            case "DATE":
                return ColumnType.DATE;
            case "TIME":
            case "TIME WITH TIME ZONE":
                return ColumnType.TIME;
            case "TIMESTAMP":
            case "TIMESTAMP WITH TIME ZONE":
                return ColumnType.TIMESTAMP;
            default:
                return ColumnType.OBJECT;
        }
    }

}
