package cz.vhromada.export.sql;

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
 * A class represents for class {@link SqlExporter}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:h2Context.xml")
public class SqlExporterTest {

    /**
     * Directory
     */
    private static final Path DIRECTORY = Paths.get("").resolve("target");

    /**
     * File name
     */
    private static final String FILE = "temp.sql";

    /**
     * Data source
     */
    @Autowired
    private DataSource dataSource;

    /**
     * Instance of {@link SqlExporter}
     */
    private SqlExporter sqlExporter;

    /**
     * Instance of {@link Database}
     */
    private Database database;

    /**
     * Initializes sqlExporter and database.
     */
    @Before
    public void setUp() {
        sqlExporter = new SqlExporter(DIRECTORY, FILE);
        database = new Database();
        database.setDataSource(dataSource);
        database.setType(DatabaseType.H2);
    }

    /**
     * Test method for {@link SqlExporter#SqlExporter(Path, String)} with null directory.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullDirectory() {
        new SqlExporter(null, FILE);
    }

    /**
     * Test method for {@link SqlExporter#SqlExporter(Path, String)} with null file name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullFileName() {
        new SqlExporter(DIRECTORY, null);
    }

    /**
     * Test method for {@link SqlExporter#SqlExporter(Path, String)} with empty string as file name.
     */
    @Test(expected = ValidationException.class)
    public void testConstructorWithEmptyFileName() {
        new SqlExporter(DIRECTORY, "");
    }

    /**
     * Test method for {@link SqlExporter#getConnection(Database)}.
     *
     * @throws SQLException if closing connection failed
     */
    @Test
    public void testGetConnection() throws SQLException {
        try (final Connection connection = sqlExporter.getConnection(database)) {
            assertNotNull(connection);
            assertTrue(connection instanceof JdbcConnection);
        }
    }

    /**
     * Test method for {@link SqlExporter#extract(Database, Connection)}.
     *
     * @throws SQLException if closing connection failed
     */
    @Test
    public void testExtractData() throws SQLException {
        try (final Connection connection = sqlExporter.getConnection(database)) {
            final ExtractData expectedExtractData = TestUtils.getExtractDataForExtract();

            final ExtractData actualExtractData = sqlExporter.extract(database, connection);

            TestUtils.assertExtractDataDeepEquals(expectedExtractData, actualExtractData);
        }
    }

    /**
     * Test method for {@link SqlExporter#export(ExtractData, Charset)}.
     *
     * @throws IOException if reading stored file failed
     */
    @Test
    public void testExportData() throws IOException {
        final Charset charset = Charset.forName("UTF-8");

        sqlExporter.export(TestUtils.getExtractDataForExport(), charset);

        final Path path = DIRECTORY.resolve(FILE);
        final List<String> lines = Files.readAllLines(path, charset);
        final int count = 5;
        assertEquals(count, lines.size());
        for (int i = 0; i < count; i++) {
            assertEquals(TestUtils.getSqlCommand(i + 1), lines.get(i));
        }
        Files.deleteIfExists(path);
    }

}
