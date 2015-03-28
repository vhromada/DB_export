package cz.vhromada.export.extractor.impl;

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
     * Creates a new instance of ExtractorTestUtils.
     */
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

}
