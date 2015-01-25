package org.beryl.database;

/** Base script for creating databases from a sql script file. */
public abstract class DatabaseCreateScript extends DatabaseUpdateScript {

	/** Not available.
	 * Since there's no database yet, this method does nothing. */
	public final void onBeforeSchemaUpdate(DatabaseUpdateParameters params) {
		// Not allowed to use this method.
	}
}
