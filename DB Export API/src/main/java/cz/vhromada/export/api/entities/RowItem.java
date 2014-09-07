package cz.vhromada.export.api.entities;

import java.util.List;

/**
 * A class represents row item.
 *
 * @author Vladimir Hromada
 */
public class RowItem {

	/** List of column items */
	private List<ColumnItem> columnItems;

	/** Creates a new instance of RowItem. */
	public RowItem() {
	}

	/**
	 * Creates a new instance of RowItem.
	 *
	 * @param columnItems list of column items
	 */
	public RowItem(final List<ColumnItem> columnItems) {
		this.columnItems = columnItems;
	}

	/**
	 * Returns list of column items.
	 *
	 * @return list of column items
	 */
	public List<ColumnItem> getColumnItems() {
		return columnItems;
	}

	/**
	 * Sets a new value to list of column items.
	 *
	 * @param columnItems list of column items
	 */
	public void setColumnItems(final List<ColumnItem> columnItems) {
		this.columnItems = columnItems;
	}

}