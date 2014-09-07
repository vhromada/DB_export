package cz.vhromada.export;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;

import cz.vhromada.export.api.Export;
import cz.vhromada.export.api.entities.Database;
import cz.vhromada.export.api.entities.ExtractData;
import cz.vhromada.export.api.exceptions.ExportException;
import cz.vhromada.export.extractor.Extractor;
import cz.vhromada.export.extractor.impl.DerbyExtractor;
import cz.vhromada.export.extractor.impl.H2Extractor;
import cz.vhromada.export.extractor.impl.HsqlExtractor;
import cz.vhromada.validators.Validators;

/**
 * An abstract class represents abstract implementation of database export.
 *
 * @author Vladimir Hromada
 */
public abstract class AbstractExport implements Export {

	@Override
	public Connection getConnection(final Database database) throws ExportException {
		Validators.validateArgumentNotNull(database, "Database description");

		try {
			return database.getDataSource().getConnection();
		} catch (final SQLException ex) {
			throw new ExportException("Getting connection failed.", ex);
		}
	}

	@Override
	public ExtractData extract(final Database database, final Connection connection) throws ExportException {
		Validators.validateArgumentNotNull(database, "Database description");
		Validators.validateArgumentNotNull(connection, "Connection");

		Extractor extractor;
		switch (database.getType()) {
			case H2:
				extractor = new H2Extractor(connection);
				break;
			case HSQL:
				extractor = new HsqlExtractor(connection);
				break;
			default:
				extractor = new DerbyExtractor(connection);
				break;
		}
		return extractor.extract();
	}

	@Override
	public void export(final ExtractData extractData, final Charset charset) throws ExportException {
		Validators.validateArgumentNotNull(extractData, "Extract data from database");
		Validators.validateArgumentNotNull(charset, "Charset");

		exportData(extractData, charset);
	}

	/**
	 * Exports data from database.
	 *
	 * @param extractData extracted data from database
	 * @param charset     charset
	 * @throws ExportException if exporting data failed
	 */
	protected abstract void exportData(final ExtractData extractData, final Charset charset) throws ExportException;

}
