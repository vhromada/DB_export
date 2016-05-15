package cz.vhromada.export.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import cz.vhromada.export.api.entities.Database;
import cz.vhromada.export.api.entities.DatabaseType;
import cz.vhromada.export.api.entities.ExtractData;
import cz.vhromada.export.test.TestUtils;
import cz.vhromada.validators.exceptions.ValidationException;

import org.h2.jdbc.JdbcConnection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents for class {@link XmlExporter}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:h2Context.xml")
public class XmlExporterTest {

    /**
     * Directory
     */
    private static final Path DIRECTORY = Paths.get("").resolve("target");

    /**
     * File name
     */
    private static final String FILE = "temp.xml";

    /**
     * Data source
     */
    @Autowired
    private DataSource dataSource;

    /**
     * Instance of {@link XmlExporter}
     */
    private XmlExporter xmlExporter;

    /**
     * Instance of {@link Database}
     */
    private Database database;

    /**
     * Initializes xmlExporter and database.
     */
    @Before
    public void setUp() {
        xmlExporter = new XmlExporter(DIRECTORY, FILE);
        database = new Database();
        database.setDataSource(dataSource);
        database.setType(DatabaseType.H2);
    }

    /**
     * Test method for {@link XmlExporter#XmlExporter(Path, String)} with null directory.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullDirectory() {
        new XmlExporter(null, FILE);
    }

    /**
     * Test method for {@link XmlExporter#XmlExporter(Path, String)} with null file name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullFileName() {
        new XmlExporter(DIRECTORY, null);
    }

    /**
     * Test method for {@link XmlExporter#XmlExporter(Path, String)} with empty string as file name.
     */
    @Test(expected = ValidationException.class)
    public void testConstructorWithEmptyFileName() {
        new XmlExporter(DIRECTORY, "");
    }

    /**
     * Test method for {@link XmlExporter#getConnection(Database)}.
     *
     * @throws SQLException if closing connection failed
     */
    @Test
    public void testGetConnection() throws SQLException {
        try (final Connection connection = xmlExporter.getConnection(database)) {
            assertNotNull(connection);
            assertTrue(connection instanceof JdbcConnection);
        }
    }

    /**
     * Test method for {@link XmlExporter#extract(Database, Connection)}.
     *
     * @throws SQLException if closing connection failed
     */
    @Test
    public void testExtractData() throws SQLException {
        try (final Connection connection = xmlExporter.getConnection(database)) {
            final ExtractData expectedExtractData = TestUtils.getExtractDataForExtract();

            final ExtractData actualExtractData = xmlExporter.extract(database, connection);

            TestUtils.assertExtractDataDeepEquals(expectedExtractData, actualExtractData);
        }
    }

    /**
     * Test method for {@link XmlExporter#export(ExtractData, Charset)}.
     *
     * @throws IOException if reading stored file failed
     */
    @Test
    public void testExportData() throws IOException {
        final Charset charset = Charset.forName("UTF-8");

        xmlExporter.export(TestUtils.getExtractDataForExport(), charset);

        final Path path = DIRECTORY.resolve(FILE);
        final List<String> lines = Files.readAllLines(path, charset);
        assertEquals(2, lines.size());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", lines.get(0));
        assertEquals(TestUtils.getXml(), lines.get(1));
        Files.deleteIfExists(path);
    }

}
