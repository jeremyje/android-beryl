package org.beryl.database;

import org.beryl.diagnostics.Log;
import org.beryl.diagnostics.Logger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/** Base class for providing functionality to have managed database upgrades.
 * Once subclassed the database will be upgraded to the latest version using DatabaseUpdateScript objects.
 *
<h3>These objects must be defined with the following criteria.</h3>
<ul>
	<li>All in the same package.</li>
	<li>Publicly accessible and have a public default constructor. Other constructors will never be used.</li>
	<li>Update Scripts must have the convention "ScriptName_VersionNumber" this is important.</li>
	<li>Otherwise the script will not be found and a failure exception will be thrown. (This does not apply to the create script.)</li>
	<li>There must be an update script for each incremental version. starting with _2.</li>
</ul>
<h3>Tips and Notes</h3>
<ul>
	<li>Never call getReadableDatabase() or getWritableDatabase() on the UI thread. Upgrades take a long time to run.</li>
	<li>This class is kinda weird. Check out the {@link org.beryl.web.cache.WebCacheOpenHelper} class implementation to see how upgrades are done. This class is a simple version.</li>
</ul>
 */
public abstract class UpdatableDatabaseOpenHelper extends SQLiteOpenHelper {
	private final Context context;
	private Log logger = null;

	public UpdatableDatabaseOpenHelper(Context context, String name, int version) {
		super(context, name, null, version);
		this.context = context;
	}

	public UpdatableDatabaseOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final DatabaseUpdateParameters params = createUpdateParams(db);
		final DatabaseUpdateRunner updateRunner = new DatabaseUpdateRunner();
		final DatabaseUpdateLoader updateLoader = new DatabaseUpdateLoader(updateRunner, params);
		final Log log = params.log;

		try {
			log.i("onCreate: Load create database scripts...");
			Class<? extends IDatabaseUpdateScript> createScript = getCreateScript();
			updateLoader.queueScriptFromClass(createScript);
			updateRunner.run();
		} catch(Exception e) {
			log.e("Create Database failed!!!");
			log.e(e);
		}
		finally {

		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		final DatabaseUpdateParameters params = createUpdateParams(db);
		final DatabaseUpdateRunner updateRunner = new DatabaseUpdateRunner();
		final DatabaseUpdateLoader updateLoader = new DatabaseUpdateLoader(updateRunner, params);
		final Log log = params.log;

		try {
			log.i("onUpgrade: Load update database scripts...");

			updateLoader.queueScriptFromClassNameTemplate(getUpdateScriptClassPathTemplate(), oldVersion, newVersion);
			updateRunner.run();
		} catch(Exception e) {
			log.e("Update Database failed!!!");
			log.e(e);
		}
		finally {

		}
	}

	private DatabaseUpdateParameters createUpdateParams(SQLiteDatabase db) {
		return new DatabaseUpdateParameters(context, db, getLogger());
	}

	private Log getLogger() {
		if(logger == null) {
			logger = Logger.newInstance("db_" + getDbName());
		}

		return logger;
	}

	protected String createUpdateScriptClassPathTemplate(Class<?> exampleUpdateClass) {
		String template = null;

		if(exampleUpdateClass != null) {
			String className = exampleUpdateClass.getName();
			template = className.replace("_2", "_%d");
		}

		return template;
	}

	/** Name of the database. When entries are logged against it, they will be tagged as db_DbName. */
	public abstract String getDbName();

	/** The DatabaseCreateScript that is used to create the database. */
	protected abstract Class<? extends IDatabaseUpdateScript> getCreateScript();

	/** Used by the Update Script Loader engine. Return the update script _2 class name.
	 * Example:
<pre class="code"><code class="java">
protected String getUpdateScriptClassPathTemplate() {
	return WebCacheUpdateScript_2.class.getName();
}
</code></pre> */
	protected abstract String getUpdateScriptClassPathTemplate();
}
