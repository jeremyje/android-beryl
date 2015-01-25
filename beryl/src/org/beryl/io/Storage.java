package org.beryl.io;

import android.os.Environment;

public class Storage {

	/** Checks to see if the external storage media is available and can be <b>read and written</b>.
	 * If you only require read-only access. Use isExternalStorageReadable() instead. */
	public static boolean isExternalStorageAvailable() {
		final String storageState = Environment.getExternalStorageState();
		if(storageState.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/** Checks to see if the external storage media is <b>available for reading</b>.
	 * To check for read-write access use isExternalStorageAvailable() instead. */
	public static boolean isExternalStorageReadable() {
		final String storageState = Environment.getExternalStorageState();
		if(storageState.equals(Environment.MEDIA_MOUNTED) || storageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			return true;
		} else {
			return false;
		}
	}
}
