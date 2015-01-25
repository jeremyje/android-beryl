package org.beryl.database;

/**
 * Exception that indicates that the database update failed.
 */
public class DatabaseUpdateFailedException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1618583774440141160L;

	public DatabaseUpdateFailedException(String message) {
		super(message);
	}

	public DatabaseUpdateFailedException(Throwable t) {
		super(t);
	}
}
