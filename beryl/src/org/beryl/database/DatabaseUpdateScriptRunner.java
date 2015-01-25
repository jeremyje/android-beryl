package org.beryl.database;

import org.beryl.diagnostics.Log;

/** Wrappers a {@link org.beryl.database.DatabaseUpdateScript} so that it can be run.
 */
class DatabaseUpdateScriptRunner implements Runnable {

	private final IDatabaseUpdateScript script;
	private final DatabaseUpdateParameters params;

	public DatabaseUpdateScriptRunner(DatabaseUpdateParameters params, IDatabaseUpdateScript script) {
		this.params = params;
		this.script = script;
	}

	public void run() {
		final Log logger = params.log;
		logger.d("DatabaseUpdateScriptRunner: onBeforeSchemaUpdate");
		script.onBeforeSchemaUpdate(params);
		logger.d("DatabaseUpdateScriptRunner: updateSchema");
		script.updateSchema(params);
		logger.d("DatabaseUpdateScriptRunner: onAfterSchemaUpdate");
		script.onAfterSchemaUpdate(params);
	}
}
