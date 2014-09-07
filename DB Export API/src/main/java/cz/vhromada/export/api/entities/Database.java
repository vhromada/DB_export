package cz.vhromada.export.api.entities;

import javax.sql.DataSource;

/**
 * A class represents database description.
 *
 * @author Vladimir Hromada
 */
public class Database {

	/** Data source */
	private DataSource dataSource;

	/** Type */
	private Type type;

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
	 * @param dataSource data source
	 */
	public void setDataSource(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Returns type.
	 *
	 * @return type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Sets a new value to type.
	 *
	 * @param type type
	 */
	public void setType(final Type type) {
		this.type = type;
	}

}
