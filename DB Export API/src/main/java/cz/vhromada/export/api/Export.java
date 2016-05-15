package cz.vhromada.export.api;

import java.nio.charset.Charset;
import java.sql.Connection;

import cz.vhromada.export.api.entities.Database;
import cz.vhromada.export.api.entities.ExtractData;

/**
 * An interface represents database export.
 *
 * @author Vladimir Hromada
 */
public interface Export {

    /**
     * Extract data from database.
     *
     * @param database database description
     * @return extracted data
     * @throws IllegalArgumentException                          if database description is null
     * @throws cz.vhromada.export.api.exceptions.ExportException if getting connection failed
     */
    Connection getConnection(Database database);

    /**
     * Extract data from database.
     *
     * @param database database description
     * @param connection connection
     * @return extracted data
     * @throws IllegalArgumentException                          if database description is null
     *                                                           or connection is null
     * @throws cz.vhromada.export.api.exceptions.ExportException if extracting data failed
     */
    ExtractData extract(Database database, Connection connection);

    /**
     * Exports database.
     *
     * @param extractData extracted data from database
     * @param charset     charset
     * @throws IllegalArgumentException                          if extracted data from database is null
     *                                                           or charset is null
     * @throws cz.vhromada.export.api.exceptions.ExportException if exporting data failed
     */
    void export(ExtractData extractData, Charset charset);

}
