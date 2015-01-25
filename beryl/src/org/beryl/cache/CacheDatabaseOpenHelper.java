package org.beryl.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class CacheDatabaseOpenHelper extends SQLiteOpenHelper {
	static final String DB_TABLE = "Cache";
	
	private static final String SCHEMA = "CREATE TABLE [Cache] ([CacheKey] VARCHAR(1024)  NOT NULL PRIMARY KEY,[DataContent] BLOB  NOT NULL,[MetaData] VARCHAR(1024)  NULL);";
	
	public CacheDatabaseOpenHelper(Context context, String name) {
		super(context, name, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SCHEMA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
