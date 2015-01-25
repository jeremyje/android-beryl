package org.beryl.database;

/** Contract for database update routines.
 * Update scripts will only be called when the database detects that it needs to be upgraded.
 * Scripts are guaranteed to run in sequential order and only need to assume that they will run from the previous
 * version of the database schema. IE: Version 5 script will run only against version 4 databases.
 * An exception is the create script which will run from an empty database.
 *
 * Typically this interface is not implemented directly.
 * Extend from either {@link org.beryl.database.DatabaseCreateScript} or {@link org.beryl.database.DatabaseUpdateScript}. */
public interface IDatabaseUpdateScript {

	/** Event handler for when a database is about to be upgraded from the previous version.
	 * Called before the update schema patch is applied.
	 * Typically used to prepare or migrate data that would otherwise be corrupt when the new schema is applied. */
	void onBeforeSchemaUpdate(DatabaseUpdateParameters params);

	/** Handles the actual update of the database schema.
	 * Typically the use of a {@link org.beryl.database.SqlScriptReader} and {@link org.beryl.database.SqlScriptRunner} are used to apply the new schema from a sql file. */
	void updateSchema(DatabaseUpdateParameters params);

	/** Event handler for when the database's schema has been updated to this version.
	 * Use this to populate new data pertinent to this new version.
	 */
	void onAfterSchemaUpdate(DatabaseUpdateParameters params);
}
