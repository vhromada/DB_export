package cz.vhromada.export.api.entities;

/**
 * A class represents column description.
 *
 * @author Vladimir Hromada
 */
public class ColumnDescription {

	/** Name */
	private String name;

	/** Type */
	private ColumnType type;

	/** Creates a new instance of ColumnDescription. */
	public ColumnDescription() {
	}

	/**
	 * Creates a new instance of ColumnDescription.
	 *
	 * @param name name
	 * @param type type
	 */
	public ColumnDescription(final String name, final ColumnType type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Returns name.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets a new value to name.
	 *
	 * @param name name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns type.
	 *
	 * @return type
	 */
	public ColumnType getType() {
		return type;
	}

	/**
	 * Sets a new value to type.
	 *
	 * @param type type
	 */
	public void setType(final ColumnType type) {
		this.type = type;
	}

}
