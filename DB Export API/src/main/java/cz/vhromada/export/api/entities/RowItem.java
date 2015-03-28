package cz.vhromada.export.api.entities;

import java.util.List;

/**
 * A class represents row item.
 *
 * @author Vladimir Hromada
 */
public class RowItem {

    /**
     * List of columns
     */
    private List<ColumnItem> columns;

    /**
     * Returns list of columns.
     *
     * @return list of columns
     */
    public List<ColumnItem> getColumns() {
        return columns;
    }

    /**
     * Sets a new value to list of columns.
     *
     * @param columns new value
     */
    public void setColumns(final List<ColumnItem> columns) {
        this.columns = columns;
    }

}
