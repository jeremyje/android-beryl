package org.beryl.database;

/** Database Create Script that only requires a single SQL script file to be run.
 * Chances are this is the file you'll want to extend to perform a database creation. */
public abstract class SqlOnlyCreateScript extends DatabaseCreateScript {

	@Override
	public void onUpdateSchema(DatabaseUpdateParameters params) {}

	public void onAfterSchemaUpdate(DatabaseUpdateParameters params) {}

}
