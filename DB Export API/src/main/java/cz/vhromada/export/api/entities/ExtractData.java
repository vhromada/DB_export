package cz.vhromada.export.api.entities;

import java.util.List;
import java.util.Map;

/**
 * A class represents extracted data.
 *
 * @author Vladimir Hromada
 */
public class ExtractData {

	/**
	 * Extracted data.
	 * <p/>
	 * Map: table name -> list of row items
	 */
	private Map<String, List<RowItem>> data;

	/**
	 * Returns extracted data.
	 * <p/>
	 * Map: table name -> list of row items
	 *
	 * @return extracted data
	 */
	public Map<String, List<RowItem>> getData() {
		return data;
	}

	/**
	 * Sets a new value to extracted data.
	 *
	 * @param data extracted data
	 */
	public void setData(final Map<String, List<RowItem>> data) {
		this.data = data;
	}

}
