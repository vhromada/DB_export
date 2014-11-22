package cz.vhromada.export.api;

import java.nio.charset.Charset;
import java.sql.Connection;

import cz.vhromada.export.api.entities.Database;
import cz.vhromada.export.api.entities.ExtractData;
import cz.vhromada.export.api.exceptions.ExportException;

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
     * @throws IllegalArgumentException if database description is null
     * @throws ExportException          if getting connection failed
     */
    Connection getConnection(Database database) throws ExportException;

    /**
     * Extract data from database.
     *
     * @param connection connection
     * @return extracted data
     * @throws IllegalArgumentException if database description is null
     *                                  or connection is null
     * @throws ExportException          if extracting data failed
     */
    ExtractData extract(Database database, Connection connection) throws ExportException;

    /**
     * Exports database.
     *
     * @param extractData extracted data from database
     * @param charset     charset
     * @throws IllegalArgumentException if extracted data from database is null
     *                                  or charset is null
     * @throws ExportException          if exporting data failed
     */
    void export(ExtractData extractData, Charset charset) throws ExportException;

}
