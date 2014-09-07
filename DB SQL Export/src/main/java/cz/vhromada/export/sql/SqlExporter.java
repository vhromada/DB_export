package cz.vhromada.export.sql;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.vhromada.export.AbstractExport;
import cz.vhromada.export.api.entities.ColumnItem;
import cz.vhromada.export.api.entities.ExtractData;
import cz.vhromada.export.api.entities.RowItem;
import cz.vhromada.export.api.exceptions.ExportException;
import cz.vhromada.validators.Validators;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * A class represents exporter to SQL file.
 *
 * @author Vladimir Hromada
 */
public class SqlExporter extends AbstractExport {

	/** Date format */
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");

	/** Time format */
	private static final DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("HH:mm:ss'");

	/** Timestamp format */
	private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	/** Directory where SQL file will be created */
	private Path directory;

	/** Name of SQL file which will be created */
	private String fileName;

	/**
	 * Creates a new instance of SqlExporter.
	 *
	 * @param directory directory where SQL file will be created
	 * @param fileName  name of SQL file which will be created
	 * @throws IllegalArgumentException if directory is null
	 *                                  or name of file is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if name of file is empty string
	 */
	public SqlExporter(final Path directory, final String fileName) {
		Validators.validateArgumentNotNull(directory, "Directory");
		Validators.validateArgumentNotNull(fileName, "SQL file");
		Validators.validateNotEmptyString(fileName, "SQL file");

		this.directory = directory;
		this.fileName = fileName;
	}

	@Override
	protected void exportData(final ExtractData extractData, final Charset charset) throws ExportException {
		final List<String> rows = new ArrayList<>();
		for (Map.Entry<String, List<RowItem>> entry : extractData.getData().entrySet()) {
			final String table = entry.getKey();
			for (RowItem row : entry.getValue()) {
				rows.add(createSqlStatement(table, row));
			}
		}
		try {
			final Path path = Files.createDirectories(directory).resolve(fileName);
			Files.deleteIfExists(path);
			Files.write(path, rows, charset, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
		} catch (final IOException ex) {
			throw new ExportException("Creating SQL file failed.", ex);
		}
	}

	/**
	 * Creates SQL statement.
	 *
	 * @param table   table
	 * @param rowItem row item
	 * @return created sql statement
	 */
	private String createSqlStatement(final String table, final RowItem rowItem) {
		final List<ColumnItem> columns = rowItem.getColumnItems();
		final StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(table);
		sql.append(" (");
		for (ColumnItem column : columns) {
			sql.append(column.getColumnDescription().getName());
			if (columns.indexOf(column) < columns.size() - 1) {
				sql.append(", ");
			}
		}
		sql.append(") VALUES (");
		for (ColumnItem column : columns) {
			sql.append(getColumnValue(column));
			if (columns.indexOf(column) < columns.size() - 1) {
				sql.append(", ");
			}
		}
		sql.append(");");
		return sql.toString();
	}

	private String getColumnValue(final ColumnItem column) {
		final Object value = column.getValue();
		if (value == null) {
			return null;
		}
		final String stringValue = "'%s'";
		switch (column.getColumnDescription().getType()) {
			case STRING:
				return String.format(stringValue, value.toString().replaceAll("'", "''"));
			case DATE:
				return String.format(stringValue, DATE_FORMAT.print((DateTime) value));
			case TIME:
				return String.format(stringValue, TIME_FORMAT.print((DateTime) value));
			case TIMESTAMP:
				return String.format(stringValue, TIMESTAMP_FORMAT.print((DateTime) value));
			default:
				return value.toString();
		}
	}

}
