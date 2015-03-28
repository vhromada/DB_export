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
import cz.vhromada.test.DeepAsserts;
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

    /**
     * Date format
     */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");

    /**
     * Time format
     */
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("HH:mm:ss");

    /**
     * Timestamp format
     */
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

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
            final ExtractData actualExtractData = sqlExporter.extract(database, connection);
            final ExtractData expectedExtractData = getExtractData();
            DeepAsserts.assertNotNull(actualExtractData);
            DeepAsserts.assertEquals(expectedExtractData, actualExtractData);
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
            final List<ColumnItem> columns = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                columns.add(getColumnItem(j, i + 1));
            }
            final RowItem rowItem = new RowItem();
            rowItem.setColumns(columns);
            rowItems.add(rowItem);
        }
        final Map<String, List<RowItem>> data = new HashMap<>();
        data.put("TAB", rowItems);

        final ExtractData extractData = new ExtractData();
        extractData.setData(data);

        return extractData;
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
        final ColumnDescription columnDescription = new ColumnDescription();
        columnDescription.setName(name);
        columnDescription.setType(type);

        final ColumnItem columnItem = new ColumnItem();
        columnItem.setDescription(columnDescription);
        columnItem.setValue(value);

        return columnItem;
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
