package org.beryl.database;

import org.beryl.diagnostics.Log;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/** Holder class for parameters that are used to perform a database update.
 * This object should not be instantiated directly. It will be provided by the OpenHelper class. */
public class DatabaseUpdateParameters {

	public final Context context;
	public final SQLiteDatabase db;
	public final Log log;

	DatabaseUpdateParameters(Context context, SQLiteDatabase db, Log log) {
		this.context = context;
		this.db = db;
		this.log = log;
	}
}
