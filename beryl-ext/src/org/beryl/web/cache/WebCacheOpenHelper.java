package org.beryl.web.cache;

import org.beryl.database.IDatabaseUpdateScript;
import org.beryl.database.SqlOnlyCreateScript;
import org.beryl.database.SqlOnlyUpdateScript;
import org.beryl.database.UpdatableDatabaseOpenHelper;

import android.content.Context;

public class WebCacheOpenHelper extends UpdatableDatabaseOpenHelper {

	public static final String DB_NAME = "webcache";
	public static final int DB_VERSION = 1;
	
	public WebCacheOpenHelper(Context context) {
		super(context, DB_NAME, DB_VERSION);
	}
	
	/*
	 * @deprecated Only used for testing purposes. Remove this later.
	 */
	@Deprecated
	public WebCacheOpenHelper(Context context, int testVersion) {
		super(context, DB_NAME, testVersion);
	}

	protected Class<? extends IDatabaseUpdateScript> getCreateScript() {
		return WebCacheCreateDatabaseScript.class;
	}
	
	protected String getUpdateScriptClassPathTemplate() {
		return WebCacheUpdateScript_2.class.getName();
	}

	@Override
	public String getDbName() {
		return DB_NAME;
	}
	
	public static class WebCacheCreateDatabaseScript extends SqlOnlyCreateScript {

		protected String getSchemaSqlScript() {
			return "webcache/webcache.sql";
		}
	}
	
	public static class WebCacheUpdateScript_2 extends SqlOnlyUpdateScript {

		@Override
		protected String getSchemaSqlScript() {
			return "webcache/webcache_2.sql";
		}
	}
}
