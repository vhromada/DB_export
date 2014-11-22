package cz.vhromada.export.extractor.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
 * A class represents utility class for testing extractors.
 *
 * @author Vladimir Hromada
 */
public final class ExtractorTestUtils {

    /** Date format */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");

    /** Time format */
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("HH:mm:ss");

    /** Timestamp format */
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    /** Creates a new instance of ExtractorTestUtils. */
    private ExtractorTestUtils() {
    }

    /**
     * Returns expected extract data.
     *
     * @return expected extract data
     */
    public static ExtractData getExpectedExtractData() {
        final List<RowItem> tableARows = new ArrayList<>();
        tableARows.add(createRowA(true, 5, -2, 87L, 5.6, new BigDecimal("7.88"), "Hello", "2000-12-20", "12:20:50", "2000-12-20 12:20:50"));
        tableARows.add(createRowA(false, -57, 78, 52538L, 2785.578, new BigDecimal(-58), "GGG", "2013-01-01", "1:1:1", "2000-12-01 5:5:5"));

        final List<RowItem> tableBRows = new ArrayList<>();
        tableBRows.add(createRowB(1L, "name", 7));
        tableBRows.add(createRowB(5L, "fg", 71));
        tableBRows.add(createRowB(10L, "45h", 20));
        tableBRows.add(createRowB(20L, "ng", 97));
        tableBRows.add(createRowB(50L, "re", 572));

        final Map<String, List<RowItem>> extractData = new HashMap<>();
        extractData.put("A", tableARows);
        extractData.put("B", tableBRows);

        return new ExtractData(extractData);
    }

    /**
     * Asserts deep extract data for equality.
     *
     * @param expected expected extract data
     * @param actual   actual extract data
     */
    public static void assertExtractDataDeepEquals(final ExtractData expected, final ExtractData actual) {
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
        columnItems.add(new ColumnItem(new ColumnDescription("A", ColumnType.BOOLEAN), a));
        columnItems.add(new ColumnItem(new ColumnDescription("B", ColumnType.DECIMAL), new BigDecimal(b)));
        columnItems.add(new ColumnItem(new ColumnDescription("C", ColumnType.DECIMAL), new BigDecimal(c)));
        columnItems.add(new ColumnItem(new ColumnDescription("D", ColumnType.DECIMAL), new BigDecimal(d)));
        columnItems.add(new ColumnItem(new ColumnDescription("E", ColumnType.DECIMAL), new BigDecimal(e)));
        columnItems.add(new ColumnItem(new ColumnDescription("F", ColumnType.DECIMAL), f));
        columnItems.add(new ColumnItem(new ColumnDescription("G", ColumnType.STRING), g));
        columnItems.add(new ColumnItem(new ColumnDescription("H", ColumnType.DATE), DATE_FORMAT.parseDateTime(h)));
        columnItems.add(new ColumnItem(new ColumnDescription("I", ColumnType.TIME), TIME_FORMAT.parseDateTime(i)));
        columnItems.add(new ColumnItem(new ColumnDescription("J", ColumnType.TIMESTAMP), TIMESTAMP_FORMAT.parseDateTime(j)));
        return new RowItem(columnItems);
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
        columnItems.add(new ColumnItem(new ColumnDescription("ID", ColumnType.DECIMAL), id));
        columnItems.add(new ColumnItem(new ColumnDescription("NAME", ColumnType.STRING), name));
        columnItems.add(new ColumnItem(new ColumnDescription("AGE", ColumnType.DECIMAL), age));
        return new RowItem(columnItems);
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

}
