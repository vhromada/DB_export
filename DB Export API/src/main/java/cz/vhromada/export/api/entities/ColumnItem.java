package cz.vhromada.export.api.entities;

/**
 * A class represents column item.
 *
 * @author Vladimir Hromada
 */
public class ColumnItem {

    /**
     * Description
     */
    private ColumnDescription description;

    /**
     * Value stored in column
     */
    private Object value;

    /**
     * Returns description.
     *
     * @return description
     */
    public ColumnDescription getDescription() {
        return description;
    }

    /**
     * Sets a new value to description.
     *
     * @param description new value
     */
    public void setDescription(final ColumnDescription description) {
        this.description = description;
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
     * @param value new value
     */
    public void setValue(final Object value) {
        this.value = value;
    }

}
