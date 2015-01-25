package org.beryl.database;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.beryl.diagnostics.ExceptionReporter;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

/** Utility methods for database operations. */
public class DatabaseUtils {

	public static InputStream readAsset(final Context context, final String path) throws IOException {
		final AssetManager assets = context.getAssets();
		return assets.open(path, AssetManager.ACCESS_BUFFER);
	}

	public static BufferedReader readBufferedAsset(final Context context, final String path) throws IOException {
		final InputStream is = readAsset(context, path);
		final InputStreamReader isr = new InputStreamReader(new BufferedInputStream(is));
		return new BufferedReader(isr);
	}

	public static boolean runSqlScriptFromAsset(final Context context,
			final SQLiteDatabase db, final String schemaFilePath) {
		boolean success = false;
		try {

			SqlScriptReader script = new SqlScriptReader(readAsset(context, schemaFilePath));
			SqlScriptRunner sqlRunner = new SqlScriptRunner(db, script);
			success = sqlRunner.run();
		} catch(Exception e) {
			ExceptionReporter.report(e);
			success = false;
		}

		return success;
	}
}
