package cz.vhromada.export.api.entities;

/**
 * A class represents column item.
 *
 * @author Vladimir Hromada
 */
public class ColumnItem {

    /** Column description */
    private ColumnDescription columnDescription;

    /** Value stored in column */
    private Object value;

    /** Creates a new instance of ColumnItem. */
    public ColumnItem() {
    }

    /**
     * Creates a new instance of ColumnItem.
     *
     * @param columnDescription column description
     * @param value             value stored in column
     */
    public ColumnItem(final ColumnDescription columnDescription, final Object value) {
        this.columnDescription = columnDescription;
        this.value = value;
    }

    /**
     * Returns column description.
     *
     * @return column description
     */
    public ColumnDescription getColumnDescription() {
        return columnDescription;
    }

    /**
     * Sets a new value to column description.
     *
     * @param columnDescription column description
     */
    public void setColumnDescription(final ColumnDescription columnDescription) {
        this.columnDescription = columnDescription;
    }

    /**
     * Returns value stored in column.
     *
     * @return value stored in column
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets a new value to value stored in column.
     *
     * @param value value stored in column
     */
    public void setValue(final Object value) {
        this.value = value;
    }

}
