package org.beryl.database;

/** Database Update Script that only requires a single SQL script file to be run.
 * Chances are this is the file you'll want to extend to perform a database update. */
public abstract class SqlOnlyUpdateScript extends DatabaseUpdateScript {

	@Override
	public final void onUpdateSchema(DatabaseUpdateParameters params) {}

	public final void onAfterSchemaUpdate(DatabaseUpdateParameters params) {}

	public final void onBeforeSchemaUpdate(DatabaseUpdateParameters params) {}
}
