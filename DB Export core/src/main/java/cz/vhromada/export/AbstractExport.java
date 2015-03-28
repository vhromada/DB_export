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

    /**
     * @throws IllegalArgumentException if database description is null
     * @throws ExportException          if getting connection failed
     */
    @Override
    public Connection getConnection(final Database database) {
        Validators.validateArgumentNotNull(database, "Database description");

        try {
            return database.getDataSource().getConnection();
        } catch (final SQLException ex) {
            throw new ExportException("Getting connection failed.", ex);
        }
    }

    /**
     * @throws IllegalArgumentException if database description is null
     *                                  or connection is null
     * @throws ExportException          if getting connection failed
     */
    @Override
    public ExtractData extract(final Database database, final Connection connection) {
        Validators.validateArgumentNotNull(database, "Database description");
        Validators.validateArgumentNotNull(connection, "Connection");

        final Extractor extractor;
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

    /**
     * @throws IllegalArgumentException if extracted data from database is null
     *                                  or charset is null
     * @throws ExportException          if getting connection failed
     */
    @Override
    public void export(final ExtractData extractData, final Charset charset) {
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
    protected abstract void exportData(final ExtractData extractData, final Charset charset);

}
