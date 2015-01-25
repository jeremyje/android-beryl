package org.beryl.database;

import org.beryl.diagnostics.Log;

/**
 * Base class for database update scripts.
 * Assumes that the schema patch is in the assets/ directory as an .sql file.
 * This base class also provides convenience methods for performing database updates.
 */
public abstract class DatabaseUpdateScript implements IDatabaseUpdateScript {

	/** Handles the actual update of the database schema. This method cannot be overriden. Use onUpdateSchema instead. */
	public final void updateSchema(DatabaseUpdateParameters params) {
		final Log log = params.log;
		String sqlScript = getSchemaSqlScript();
		boolean scriptSuccess = true;

		if(sqlScript != null) {
			scriptSuccess = executeScript(params, sqlScript);
		} else {
			log.d("No schema script specified for this version.");
		}

		if(! scriptSuccess) {
			String failureMessage = sqlScript + " did not run successfully.";
			params.log.e(failureMessage);
			throw new DatabaseUpdateFailedException(failureMessage);
		}

		onUpdateSchema(params);
	}

	/** Executes an .sql script file that is stored in the application's asset directory. */
	protected boolean executeScript(DatabaseUpdateParameters params, String sqlScriptAssetFilePath) {
		boolean success = false;
		final Log log = params.log;
		log.d(String.format("Running sql script: %s", sqlScriptAssetFilePath));
		success = DatabaseUtils.runSqlScriptFromAsset(params.context, params.db, sqlScriptAssetFilePath);
		return success;
	}

	/** Handler for code that should be applied immediately after a schema update.
	 * Honestly, this is just here to allow the developer to put code that would otherwise be put in updateSchema as an override.
	 * It is preferable to use onAfterSchemaUpdate.
	 * @param params
	 */
	public abstract void onUpdateSchema(DatabaseUpdateParameters params);

	/** Specify the location of where the .sql script file is located.
	 * If this database version does not have a schema update then return null. */
	protected abstract String getSchemaSqlScript();
}
