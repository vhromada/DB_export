package cz.vhromada.export.api.exceptions;

/**
 * A class represents exception caused by exporting data.
 *
 * @author Vladimir Hromada
 */
public class ExportException extends Exception {

	/** SerialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of ExportException.
	 *
	 * @param message message
	 * @param cause   cause
	 */
	public ExportException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
