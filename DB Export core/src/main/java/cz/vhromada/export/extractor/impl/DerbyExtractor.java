package cz.vhromada.export.extractor.impl;

import java.sql.Connection;

import cz.vhromada.export.api.entities.ColumnType;
import cz.vhromada.export.extractor.Extractor;

/**
 * A class represents extractor for Derby DB.
 *
 * @author Vladimir Hromada
 */
public class DerbyExtractor extends Extractor {

	/**
	 * Creates a new instance of DerbyExtractor.
	 *
	 * @param connection connection
	 * @throws IllegalArgumentException if connection is null
	 */
	public DerbyExtractor(final Connection connection) {
		super(connection);
	}

	@Override
	protected String getDatabaseTableSQL() {
		return "SELECT TABLENAME FROM SYS.SYSTABLES WHERE TABLETYPE = 'T'";
	}

	@Override
	protected String getDatabaseColumnSQL() {
		return "SELECT col.COLUMNNAME, col.COLUMNDATATYPE FROM SYS.SYSCOLUMNS col, SYS.SYSTABLES tab WHERE col.REFERENCEID = tab.TABLEID AND tab.TABLENAME = ?";
	}

	/**
	 * Converts column type.
	 *
	 * @param column column type
	 * @return converted column type
	 */
	@Override
	protected ColumnType convertColumn(final String column) {
		final String columnName = column.split("\\(")[0];
		switch (columnName.toUpperCase()) {
			case "BOOLEAN":
				return ColumnType.BOOLEAN;
			case "SMALLINT":
			case "INTEGER":
			case "BIGINT":
			case "REAL":
			case "DOUBLE":
			case "NUMERIC":
			case "DECIMAL":
				return ColumnType.DECIMAL;
			case "VARCHAR":
			case "CHAR":
			case "LONG VARCHAR":
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
