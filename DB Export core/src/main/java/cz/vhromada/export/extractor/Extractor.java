package cz.vhromada.export.extractor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.vhromada.export.api.entities.ColumnDescription;
import cz.vhromada.export.api.entities.ColumnItem;
import cz.vhromada.export.api.entities.ColumnType;
import cz.vhromada.export.api.entities.ExtractData;
import cz.vhromada.export.api.entities.RowItem;
import cz.vhromada.export.api.exceptions.ExportException;
import cz.vhromada.validators.Validators;

import org.joda.time.DateTime;

/**
 * A class represents extractor of data from database.
 *
 * @author Vladimir Hromada
 */
public abstract class Extractor {

    /**
     * Connection
     */
    private Connection connection;

    /**
     * Creates a new instance of Extractor.
     *
     * @param connection connection
     * @throws IllegalArgumentException if connection is null
     */
    protected Extractor(final Connection connection) {
        Validators.validateArgumentNotNull(connection, "Connection");

        this.connection = connection;
    }

    /**
     * Returns extracted data.
     *
     * @return extracted data
     * @throws ExportException if extracting data failed
     */
    public ExtractData extract() throws ExportException {
        final String databaseTableSQL = getDatabaseTableSQL();
        final String databaseColumnSQL = getDatabaseColumnSQL();
        try {
            final Map<String, List<RowItem>> extractedData = new HashMap<>();
            final List<String> tables = getTables(databaseTableSQL);
            for (final String table : tables) {
                final List<ColumnDescription> columns = getColumns(table, databaseColumnSQL);
                extractedData.put(table, extractData(table, columns));
            }
            return new ExtractData(extractedData);
        } catch (final SQLException ex) {
            throw new ExportException("There was error in working with database.", ex);
        }
    }

    /**
     * Returns database table extraction SQL.
     *
     * @return database table extraction SQL
     */
    protected abstract String getDatabaseTableSQL();

    /**
     * Returns database column extraction SQL.
     *
     * @return database column extraction SQL
     */
    protected abstract String getDatabaseColumnSQL();

    /**
     * Converts column type.
     *
     * @param column column type
     * @return converted column type
     */
    protected abstract ColumnType convertColumn(final String column);

    /**
     * Returns list of table names.
     *
     * @param databaseTableSQL database table extraction SQL
     * @return descriptions of columns in table
     * @throws SQLException if getting descriptions of columns in table failed
     */
    private List<String> getTables(final String databaseTableSQL) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement(databaseTableSQL)) {
            final List<String> result = new ArrayList<>();
            try (final ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(resultSet.getString(1));
                }
            }
            return result;
        }
    }

    /**
     * Returns list of table column names.
     *
     * @param table             table
     * @param databaseColumnSQL database column extraction SQL
     * @return descriptions of columns in table
     * @throws SQLException if getting descriptions of columns in table failed
     */
    private List<ColumnDescription> getColumns(final String table, final String databaseColumnSQL) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement(databaseColumnSQL)) {
            statement.setString(1, table);
            final List<ColumnDescription> result = new ArrayList<>();
            try (final ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    final ColumnDescription columnDescription = new ColumnDescription(resultSet.getString(1), convertColumn(resultSet.getString(2)));
                    result.add(columnDescription);
                }
            }
            return result;
        }
    }

    /**
     * Returns extracted data.
     *
     * @param table              table
     * @param columnDescriptions columns descriptions
     * @return extracted data
     * @throws SQLException if getting data from database failed
     */
    private List<RowItem> extractData(final String table, final Iterable<ColumnDescription> columnDescriptions) throws SQLException {
        final String sql = String.format("SELECT * FROM %s", table);
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            final List<RowItem> result = new ArrayList<>();
            try (final ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    final List<ColumnItem> columns = new ArrayList<>();
                    for (final ColumnDescription column : columnDescriptions) {
                        Object value = null;
                        switch (column.getType()) {
                            case BOOLEAN:
                                value = resultSet.getBoolean(column.getName());
                                break;
                            case DECIMAL:
                                value = resultSet.getBigDecimal(column.getName());
                                break;
                            case STRING:
                                value = resultSet.getString(column.getName());
                                break;
                            case DATE:
                                value = new DateTime(resultSet.getDate(column.getName()));
                                break;
                            case TIME:
                                value = new DateTime(resultSet.getTime(column.getName()));
                                break;
                            case TIMESTAMP:
                                value = new DateTime(resultSet.getTimestamp(column.getName()));
                                break;
                            case OBJECT:
                                value = resultSet.getObject(column.getName());
                                break;
                        }
                        columns.add(new ColumnItem(column, value));
                    }
                    result.add(new RowItem(columns));
                }
            }
            return result;
        }
    }

}
