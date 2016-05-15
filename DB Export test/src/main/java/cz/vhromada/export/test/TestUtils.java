package cz.vhromada.export.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.vhromada.export.api.entities.ColumnDescription;
import cz.vhromada.export.api.entities.ColumnItem;
import cz.vhromada.export.api.entities.ColumnType;
import cz.vhromada.export.api.entities.ExtractData;
import cz.vhromada.export.api.entities.RowItem;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * A class represents utility class for tests.
 *
 * @author Vladimir Hromada
 */
public final class TestUtils {

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
     * Creates a new instance of TestUtils.
     */
    private TestUtils() {
    }

    /**
     * Returns extract data for extracting.
     *
     * @return extract data for extracting
     */
    public static ExtractData getExtractDataForExtract() {
        final List<RowItem> tableARows = new ArrayList<>();
        tableARows.add(createRowA(true, 5, -2, 87L, 5.6, new BigDecimal("7.88"), "Hello", "2000-12-20", "12:20:50", "2000-12-20 12:20:50"));
        tableARows.add(createRowA(false, -57, 78, 52538L, 2785.578, new BigDecimal(-58), "GGG", "2013-01-01", "1:1:1", "2000-12-01 5:5:5"));

        final List<RowItem> tableBRows = new ArrayList<>();
        tableBRows.add(createRowB(1L, "name", 7));
        tableBRows.add(createRowB(5L, "fg", 71));
        tableBRows.add(createRowB(10L, "45h", 20));
        tableBRows.add(createRowB(20L, "ng", 97));
        tableBRows.add(createRowB(50L, "re", 572));

        final Map<String, List<RowItem>> data = new HashMap<>();
        data.put("A", tableARows);
        data.put("B", tableBRows);

        final ExtractData extractData = new ExtractData();
        extractData.setData(data);

        return extractData;
    }

    /**
     * Creates expected data in table A row.
     *
     * @param a column a
     * @param b column b
     * @param c column c
     * @param d column d
     * @param e column e
     * @param f column f
     * @param g column g
     * @param h column h
     * @param i column i
     * @param j column j
     * @return expected data in table A row
     */
    private static RowItem createRowA(final boolean a, final int b, final int c, final long d, final double e, final BigDecimal f, final String g,
            final String h, final String i, final String j) {
        final List<ColumnItem> columnItems = new ArrayList<>();
        columnItems.add(newColumnItem(newColumnDescription("A", ColumnType.BOOLEAN), a));
        columnItems.add(newColumnItem(newColumnDescription("B", ColumnType.DECIMAL), new BigDecimal(b)));
        columnItems.add(newColumnItem(newColumnDescription("C", ColumnType.DECIMAL), new BigDecimal(c)));
        columnItems.add(newColumnItem(newColumnDescription("D", ColumnType.DECIMAL), new BigDecimal(d)));
        columnItems.add(newColumnItem(newColumnDescription("E", ColumnType.DECIMAL), new BigDecimal(e)));
        columnItems.add(newColumnItem(newColumnDescription("F", ColumnType.DECIMAL), f));
        columnItems.add(newColumnItem(newColumnDescription("G", ColumnType.STRING), g));
        columnItems.add(newColumnItem(newColumnDescription("H", ColumnType.DATE), DATE_FORMAT.parseDateTime(h)));
        columnItems.add(newColumnItem(newColumnDescription("I", ColumnType.TIME), TIME_FORMAT.parseDateTime(i)));
        columnItems.add(newColumnItem(newColumnDescription("J", ColumnType.TIMESTAMP), TIMESTAMP_FORMAT.parseDateTime(j)));

        final RowItem rowItem = new RowItem();
        rowItem.setColumns(columnItems);

        return rowItem;
    }

    /**
     * Creates expected data in table B row.
     *
     * @param id   column id
     * @param name column name
     * @param age  column age
     * @return expected data in table B row
     */
    private static RowItem createRowB(final long id, final String name, final int age) {
        final List<ColumnItem> columnItems = new ArrayList<>();
        columnItems.add(newColumnItem(newColumnDescription("ID", ColumnType.DECIMAL), id));
        columnItems.add(newColumnItem(newColumnDescription("NAME", ColumnType.STRING), name));
        columnItems.add(newColumnItem(newColumnDescription("AGE", ColumnType.DECIMAL), age));

        final RowItem rowItem = new RowItem();
        rowItem.setColumns(columnItems);

        return rowItem;
    }

    /**
     * Returns {@link ColumnDescription} with specified name and type.
     *
     * @param name name
     * @param type type
     * @return {@link ColumnDescription} with specified name and type
     */
    private static ColumnDescription newColumnDescription(final String name, final ColumnType type) {
        final ColumnDescription columnDescription = new ColumnDescription();
        columnDescription.setName(name);
        columnDescription.setType(type);

        return columnDescription;
    }

    /**
     * Returns {@link ColumnItem} with specified description and value.
     *
     * @param description description
     * @param value       value
     * @return {@link ColumnItem} with specified description and value
     */
    private static ColumnItem newColumnItem(final ColumnDescription description, final Object value) {
        final ColumnItem columnItem = new ColumnItem();
        columnItem.setDescription(description);
        columnItem.setValue(value);

        return columnItem;
    }

    /**
     * Returns extracted data for exporting.
     *
     * @return extracted data for exporting
     */
    public static ExtractData getExtractDataForExport() {
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
    public static String getSqlCommand(final int index) {
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

    /**
     * Returns XML.
     *
     * @return XML
     */
    public static String getXml() {
        final StringBuilder xml = new StringBuilder(1000);
        xml.append("<export xmlns=\"http://vhromada.cz/export\">");
        xml.append("<table name=\"TAB\">");
        for (int i = 0; i < 5; i++) {
            xml.append("<row>");
            for (int j = 0; j < 8; j++) {
                final ColumnItem columnItem = getColumnItem(j, i + 1);
                xml.append("<column name=\"");
                xml.append(columnItem.getDescription().getName());
                xml.append("\" type=\"");
                xml.append(columnItem.getDescription().getType());
                xml.append("\">");
                xml.append(columnItem.getValue());
                xml.append("</column>");
            }
            xml.append("</row>");
        }
        xml.append("</table></export>");

        return xml.toString();
    }

    /**
     * Asserts extract data deep equals.
     *
     * @param expected expected extract data
     * @param actual   actual extract data
     */
    public static void assertExtractDataDeepEquals(final ExtractData expected, final ExtractData actual) {
        assertNotNull(actual);
        assertDataDeepEquals(expected.getData(), actual.getData());
    }

    /**
     * Asserts data deep equals.
     *
     * @param expected expected data
     * @param actual   actual data
     */
    private static void assertDataDeepEquals(final Map<String, List<RowItem>> expected, final Map<String, List<RowItem>> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (final Map.Entry<String, List<RowItem>> expectedEntry : expected.entrySet()) {
            assertRowItemsDeepEquals(expectedEntry.getValue(), actual.get(expectedEntry.getKey()));
        }
    }

    /**
     * Asserts row items deep equals.
     *
     * @param expected expected row items
     * @param actual   actual row items
     */
    private static void assertRowItemsDeepEquals(final List<RowItem> expected, final List<RowItem> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertRowItemDeepEquals(expected.get(i), actual.get(i));
        }
    }

    /**
     * Asserts row item deep equals.
     *
     * @param expected expected row item
     * @param actual   actual row item
     */
    private static void assertRowItemDeepEquals(final RowItem expected, final RowItem actual) {
        assertNotNull(actual);
        assertColumnItemsDeepEquals(expected.getColumns(), actual.getColumns());
    }

    /**
     * Asserts column items deep equals.
     *
     * @param expected expected column items
     * @param actual   actual column items
     */
    private static void assertColumnItemsDeepEquals(final List<ColumnItem> expected, final List<ColumnItem> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertColumnItemDeepEquals(expected.get(i), actual.get(i));
        }
    }

    /**
     * Asserts column item deep equals.
     *
     * @param expected expected column item
     * @param actual   actual column item
     */
    private static void assertColumnItemDeepEquals(final ColumnItem expected, final ColumnItem actual) {
        assertNotNull(actual);
        assertNotNull(actual.getDescription());
        assertEquals(expected.getDescription().getName(), actual.getDescription().getName());
        assertEquals(expected.getDescription().getType(), actual.getDescription().getType());
        if (expected.getValue() instanceof Number) {
            assertEquals(((Number) expected.getValue()).doubleValue(), ((Number) expected.getValue()).doubleValue(), 0.001);
        } else {
            assertEquals(expected.getValue(), actual.getValue());
        }
    }

}
