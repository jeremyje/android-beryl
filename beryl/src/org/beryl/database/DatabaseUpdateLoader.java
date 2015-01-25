package org.beryl.database;

import org.beryl.diagnostics.Log;

/** Loads database update scripts and en-queues them in the DatabaseUpdateRunner to be run. */
class DatabaseUpdateLoader {

	private final DatabaseUpdateRunner runner;
	private final DatabaseUpdateParameters updateParams;

	public DatabaseUpdateLoader(DatabaseUpdateRunner runner, DatabaseUpdateParameters params) {
		this.runner = runner;
		this.updateParams = params;
	}

	@SuppressWarnings("unchecked")
	public void queueScriptFromClassName(String className){
		final Log log = updateParams.log;
		try {
			final ClassLoader loader = this.getClass().getClassLoader();
			Class<?> scriptClass = loader.loadClass(className);

			if(! IDatabaseUpdateScript.class.isAssignableFrom(scriptClass)) {
				throw new ClassCastException(String.format("While attempting to load update script [%s] it does not implement the %s interface and cannot be loaded.",
						scriptClass, IDatabaseUpdateScript.class.getName()));
			}

			queueScriptFromClass((Class<? extends IDatabaseUpdateScript>)scriptClass);
		} catch(Exception e) {
			log.e(e);
			throw new DatabaseUpdateFailedException(e);
		}
	}

	public void queueScriptFromClass(Class<? extends IDatabaseUpdateScript> createScriptClass) {
		IDatabaseUpdateScript script;
		final Log log = updateParams.log;
		final String scriptName = createScriptClass.getName();

		try {
			log.i("UpdateLoader: Enqueue script, " + scriptName);
			script = createScriptClass.newInstance();
			DatabaseUpdateScriptRunner scriptRunner = new DatabaseUpdateScriptRunner(updateParams, script);
			runner.add(scriptRunner);
		} catch(Exception e) {
			log.e(e);
			throw new DatabaseUpdateFailedException(e);
		}
	}

	public void queueScriptFromClassNameTemplate(String updateScriptClassNameTemplate, int oldVersion, int newVersion) {
		for(int i = oldVersion + 1; i <= newVersion; i++) {
			String className = String.format(updateScriptClassNameTemplate, i);
			queueScriptFromClassName(className);
		}
	}
}
