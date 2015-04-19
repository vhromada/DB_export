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
     */
    private Map<String, List<RowItem>> data;

    /**
     * Returns extracted data.
     *
     * @return extracted data
     */
    public Map<String, List<RowItem>> getData() {
        return data;
    }

    /**
     * Sets a new value to extracted data.
     *
     * @param data new value
     */
    public void setData(final Map<String, List<RowItem>> data) {
        this.data = data;
    }

}
