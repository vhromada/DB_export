package cz.vhromada.export.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import cz.vhromada.export.api.entities.ColumnDescription;
import cz.vhromada.export.api.entities.ColumnItem;
import cz.vhromada.export.api.entities.ColumnType;
import cz.vhromada.export.api.entities.Database;
import cz.vhromada.export.api.entities.DatabaseType;
import cz.vhromada.export.api.entities.ExtractData;
import cz.vhromada.export.api.entities.RowItem;
import cz.vhromada.export.api.exceptions.ExportException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.h2.jdbc.JdbcConnection;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
@ContextConfiguration(locations = "classpath:dataSourceContext.xml")
public class SqlExporterTest {

    /** Date format */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");

    /** Time format */
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("HH:mm:ss");

    /** Timestamp format */
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    /** Directory */
    private static final Path DIRECTORY = Paths.get("").resolve("target");

    /** File name */
    private static final String FILE = "temp.sql";

    /** Data source */
    @Autowired
    private DataSource dataSource;

    /** Instance of {@link SqlExporter} */
    private SqlExporter sqlExporter;

    /** Instance of {@link Database} */
    private Database database;

    /** Initializes sqlExporter and database. */
    @Before
    public void setUp() {
        sqlExporter = new SqlExporter(DIRECTORY, FILE);
        database = new Database(dataSource, DatabaseType.H2);
    }

    /** Test method for {@link SqlExporter#SqlExporter(Path, String)} with null directory. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullDirectory() {
        new SqlExporter(null, FILE);
    }

    /** Test method for {@link SqlExporter#SqlExporter(Path, String)} with null file name. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullFileName() {
        new SqlExporter(DIRECTORY, null);
    }

    /** Test method for {@link SqlExporter#SqlExporter(Path, String)} with empty string as file name. */
    @Test(expected = ValidationException.class)
    public void testConstructorWithEmptyFileName() {
        new SqlExporter(DIRECTORY, "");
    }

    /**
     * Test method for {@link SqlExporter#getConnection(Database)}.
     *
     * @throws ExportException if getting connection failed
     * @throws SQLException    if closing connection failed
     */
    @Test
    public void testGetConnection() throws ExportException, SQLException {
        try (final Connection connection = sqlExporter.getConnection(database)) {
            assertNotNull(connection);
            assertTrue(connection instanceof JdbcConnection);
        }
    }

    /**
     * Test method for {@link SqlExporter#extract(Database, Connection)}.
     *
     * @throws ExportException if extracting data failed
     * @throws SQLException    if closing connection failed
     */
    @Test
    public void testExtractData() throws ExportException, SQLException {
        try (final Connection connection = sqlExporter.getConnection(database)) {
            final ExtractData actualExtractData = sqlExporter.extract(database, connection);
            assertNotNull(actualExtractData);
            final ExtractData expectedExtractData = getExtractData();
            assertExtractDataDeepEquals(actualExtractData, expectedExtractData);
        }
    }

    /**
     * Test method for {@link SqlExporter#export(ExtractData, Charset)}.
     *
     * @throws ExportException if exporting data failed
     * @throws IOException     if reading stored file failed
     */
    @Test
    public void testExportData() throws ExportException, IOException {
        final Charset charset = Charset.forName("UTF-8");
        sqlExporter.export(getExtractData(), charset);
        final Path path = DIRECTORY.resolve(FILE);
        final List<String> lines = Files.readAllLines(path, charset);
        final int count = 5;
        assertEquals(count, lines.size());
        for (int i = 0; i < count; i++) {
            assertEquals(getSqlCommand(i + 1), lines.get(i));
        }
        Files.deleteIfExists(path);
    }

    /**
     * Returns extracted data.
     *
     * @return extracted data
     */
    private static ExtractData getExtractData() {
        final List<RowItem> rowItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final List<ColumnItem> columnItems = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                columnItems.add(getColumnItem(j, i + 1));
            }
            rowItems.add(new RowItem(columnItems));
        }
        final Map<String, List<RowItem>> data = new HashMap<>();
        data.put("TAB", rowItems);
        return new ExtractData(data);
    }

    /**
     * Returns column item.
     *
     * @param index column index
     * @param row   row
     * @return column item
     */
    private static ColumnItem getColumnItem(final int index, final int row) {
        final String name;
        final ColumnType type;
        final Object value;
        switch (index) {
            case 0:
                name = "A";
                type = ColumnType.BOOLEAN;
                value = true;
                break;
            case 1:
                name = "B";
                type = ColumnType.DECIMAL;
                value = row;
                break;
            case 2:
                name = "C";
                type = ColumnType.STRING;
                value = "Hello";
                break;
            case 3:
                name = "D";
                type = ColumnType.DECIMAL;
                value = new BigDecimal("-7.88");
                break;
            case 4:
                name = "E";
                type = ColumnType.STRING;
                value = "Bye";
                break;
            case 5:
                name = "F";
                type = ColumnType.DATE;
                value = DATE_FORMAT.parseDateTime("2000-12-20");
                break;
            case 6:
                name = "G";
                type = ColumnType.TIME;
                value = TIME_FORMAT.parseDateTime("12:20:50");
                break;
            case 7:
                name = "H";
                type = ColumnType.TIMESTAMP;
                value = TIMESTAMP_FORMAT.parseDateTime("2000-12-20 12:20:50");
                break;
            default:
                throw new IllegalArgumentException("Bad index");
        }
        return new ColumnItem(new ColumnDescription(name, type), value);
    }

    /**
     * Asserts deep extract data for equality.
     *
     * @param expected expected extract data
     * @param actual   actual extract data
     */
    private static void assertExtractDataDeepEquals(final ExtractData expected, final ExtractData actual) {
        assertNotNull(expected);
        assertNotNull(actual);

        final Map<String, List<RowItem>> expectedExtractData = expected.getData();
        final Map<String, List<RowItem>> actualExtractData = expected.getData();
        assertNotNull(expectedExtractData);
        assertNotNull(actualExtractData);
        assertEquals(expectedExtractData.size(), actualExtractData.size());
        assertEquals(expectedExtractData.keySet(), actualExtractData.keySet());
        for (final Map.Entry<String, List<RowItem>> entry : expectedExtractData.entrySet()) {
            assertTrue(actualExtractData.containsKey(entry.getKey()));
            final List<RowItem> expectedRows = entry.getValue();
            final List<RowItem> actualRows = actualExtractData.get(entry.getKey());
            assertNotNull(expectedRows);
            assertNotNull(actualRows);
            assertEquals(expectedRows.size(), actualRows.size());
            for (int i = 0; i < expectedRows.size(); i++) {
                assertRowItemDeepEquals(expectedRows.get(i), actualRows.get(i));
            }
        }
    }

    /**
     * Asserts deep row items for equality.
     *
     * @param expected expected row item
     * @param actual   actual row item
     */
    private static void assertRowItemDeepEquals(final RowItem expected, final RowItem actual) {
        assertNotNull(expected);
        assertNotNull(actual);

        final List<ColumnItem> expectedColumns = expected.getColumnItems();
        final List<ColumnItem> actualColumns = actual.getColumnItems();
        assertNotNull(expectedColumns);
        assertNotNull(actualColumns);
        assertEquals(expectedColumns.size(), actualColumns.size());
        for (int i = 0; i < expectedColumns.size(); i++) {
            assertColumnItemDeepEquals(expectedColumns.get(i), actualColumns.get(i));
        }
    }

    /**
     * Asserts deep column items for equality.
     *
     * @param expected expected column item
     * @param actual   actual column item
     */
    private static void assertColumnItemDeepEquals(final ColumnItem expected, final ColumnItem actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertColumnDescriptionDeepEquals(expected.getColumnDescription(), actual.getColumnDescription());
        assertEquals(expected.getValue(), actual.getValue());
    }

    /**
     * Asserts deep column descriptions for equality.
     *
     * @param expected expected column description
     * @param actual   actual column description
     */
    private static void assertColumnDescriptionDeepEquals(final ColumnDescription expected, final ColumnDescription actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getType(), actual.getType());
    }

    /**
     * Returns SQL command.
     *
     * @param index command index
     * @return SQL command
     */
    private static String getSqlCommand(final int index) {
        final char[] columns = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' };
        final StringBuilder sql = new StringBuilder(1000);
        sql.append("INSERT INTO TAB (");
        for (final char column : columns) {
            sql.append(column);
            if (column != columns[columns.length - 1]) {
                sql.append(", ");
            }
        }
        sql.append(") VALUES (true, ");
        sql.append(index);
        sql.append(", 'Hello', -7.88, 'Bye', '2000-12-20', '12:20:50', '2000-12-20 12:20:50');");
        return sql.toString();
    }

}
