package org.beryl.cache;

import java.io.Closeable;
import java.io.IOException;

import org.beryl.Lazy;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class FileCacheStoreImpl implements Closeable {

	private static final String DB_TABLE = "Cache";
	private static final String FIELD_KEY = "CacheKey";
	private static final String FIELD_METADATA = "MetaData";
	private static final String FIELD_DATACONTENT = "DataContent";
	private static final String WHERE_PRIMARYKEY = "CacheKey = ?";
	
	final private Context context;
	final private String name;
	final private Lazy<CacheDatabaseOpenHelper> dbOpen = new Lazy<CacheDatabaseOpenHelper>() {
		@Override
		protected CacheDatabaseOpenHelper onSet() {
			return new CacheDatabaseOpenHelper(context, name);
		}
	};
	private SQLiteDatabase db;
	
	public FileCacheStoreImpl(final Context context, final String name) {
		this.context = context;
		this.name = name;
	}
	
	public void open() {
		this.db = dbOpen.get().getWritableDatabase();
	}
	
	public void close() throws IOException {
		if(dbOpen.isValid()) {
			dbOpen.get().close();
		}
	}
	
	public void remove(String cacheKey) {
		this.db.delete(DB_TABLE, WHERE_PRIMARYKEY, new String [] { cacheKey });
	}
	
	public void updateValue(String cacheKey, int dataContent) {
		final ContentValues values = new ContentValues();
		values.put(FIELD_DATACONTENT, dataContent);
		this.db.update(DB_TABLE, values, WHERE_PRIMARYKEY, new String [] { cacheKey });
	}
	
	public void updateValue(String cacheKey, long dataContent) {
		final ContentValues values = new ContentValues();
		values.put(FIELD_DATACONTENT, dataContent);
		this.db.update(DB_TABLE, values, WHERE_PRIMARYKEY, new String [] { cacheKey });
	}
	
	public void updateValue(String cacheKey, double dataContent) {
		final ContentValues values = new ContentValues();
		values.put(FIELD_DATACONTENT, dataContent);
		this.db.update(DB_TABLE, values, WHERE_PRIMARYKEY, new String [] { cacheKey });
	}
	
	public void updateValue(String cacheKey, String dataContent) {
		final ContentValues values = new ContentValues();
		values.put(FIELD_DATACONTENT, dataContent);
		this.db.update(DB_TABLE, values, WHERE_PRIMARYKEY, new String [] { cacheKey });
	}
	
	public void updateValue(String cacheKey, byte [] dataContent) {
		final ContentValues values = new ContentValues();
		values.put(FIELD_DATACONTENT, dataContent);
		this.db.update(DB_TABLE, values, WHERE_PRIMARYKEY, new String [] { cacheKey });
	}
	
	public void updateMetaData(String cacheKey, String metaData) {
		final ContentValues values = new ContentValues();
		values.put(FIELD_METADATA, metaData);
		this.db.update(DB_TABLE, values, WHERE_PRIMARYKEY, new String [] { cacheKey });
	}
	
	public void insert(String cacheKey, int dataContent, String metaData) {
		final ContentValues values = setupContentValues(cacheKey, metaData);
		values.put(FIELD_DATACONTENT, dataContent);
		this.db.insert(DB_TABLE, null, values);
	}
	
	public void insert(String cacheKey, long dataContent, String metaData) {
		final ContentValues values = setupContentValues(cacheKey, metaData);
		values.put(FIELD_DATACONTENT, dataContent);
		this.db.insert(DB_TABLE, null, values);
	}
	
	public void insert(String cacheKey, double dataContent, String metaData) {
		final ContentValues values = setupContentValues(cacheKey, metaData);
		values.put(FIELD_DATACONTENT, dataContent);
		this.db.insert(DB_TABLE, null, values);
	}

	public void insert(String cacheKey, String dataContent, String metaData) {
		final ContentValues values = setupContentValues(cacheKey, metaData);
		values.put(FIELD_DATACONTENT, dataContent);
		this.db.insert(DB_TABLE, null, values);
	}
	
	public void insert(String cacheKey, byte [] dataContent, String metaData) {
		final ContentValues values = setupContentValues(cacheKey, metaData);
		values.put(FIELD_DATACONTENT, dataContent);
		this.db.insert(DB_TABLE, null, values);
	}
	
	private ContentValues setupContentValues(String cacheKey, String metaData) {
		final ContentValues values = new ContentValues();
		values.put(FIELD_KEY, cacheKey);
		values.put(FIELD_METADATA, metaData);
		return values;
	}
}
