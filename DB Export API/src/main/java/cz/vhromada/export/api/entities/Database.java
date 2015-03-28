package cz.vhromada.export.api.entities;

import javax.sql.DataSource;

/**
 * A class represents database description.
 *
 * @author Vladimir Hromada
 */
public class Database {

    /**
     * Data source
     */
    private DataSource dataSource;

    /**
     * Type
     */
    private DatabaseType type;

    /**
     * Returns data source.
     *
     * @return data source
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Sets a new value to data source.
     *
     * @param dataSource new value
     */
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Returns type.
     *
     * @return type
     */
    public DatabaseType getType() {
        return type;
    }

    /**
     * Sets a new value to type.
     *
     * @param type new value
     */
    public void setType(final DatabaseType type) {
        this.type = type;
    }

}
