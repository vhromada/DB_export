package cz.vhromada.export;

import static org.mockito.Mockito.mock;

import java.nio.charset.Charset;
import java.sql.Connection;

import cz.vhromada.export.api.entities.Database;
import cz.vhromada.export.api.entities.ExtractData;

import org.junit.Test;

/**
 * A class represents for class {@link AbstractExport}.
 *
 * @author Vladimir Hromada
 */
public class AbstractExportTest {

    /**
     * Test method for {@link AbstractExport#getConnection(Database)} with null database description.
     */
    @SuppressWarnings("resource")
    @Test(expected = IllegalArgumentException.class)
    public void testGetConnectionWithNullDatabase() {
        new AbstractExportImpl().getConnection(null);
    }

    /**
     * Test method for {@link AbstractExport#extract(Database, Connection)} with null database description.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExtractWithNullDatabase() {
        new AbstractExportImpl().extract(null, mock(Connection.class));
    }

    /**
     * Test method for {@link AbstractExport#extract(Database, Connection)} with null connection.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExtractWithNullConnection() {
        new AbstractExportImpl().extract(mock(Database.class), null);
    }

    /**
     * Test method for {@link AbstractExport#export(ExtractData, Charset)} with null extracted data from database.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExportWithNullExtractData() {
        new AbstractExportImpl().export(null, Charset.defaultCharset());
    }

    /**
     * Test method for {@link AbstractExport#export(ExtractData, Charset)} with null charset.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExportWithNullCharset() {
        new AbstractExportImpl().export(mock(ExtractData.class), null);
    }

    /**
     * A class represents mock implementation of {@link AbstractExport}.
     */
    private static final class AbstractExportImpl extends AbstractExport {

        @Override
        protected void exportData(final ExtractData extractData, final Charset charset) {
        }

    }

}
