package org.beryl.io;

import java.io.File;

import org.beryl.app.AndroidVersion;

import android.content.Context;

/** Collection of directory manipulation method that are compatible with all versions of Android. 
 * Reference implementation: http://developer.android.com/guide/topics/data/data-storage.html
 * */
public class DirectoryUtils {

	private static final IDirectoryCompat directoryCompat;

	static {
		if(AndroidVersion.isBeforeFroyo()) {
			directoryCompat = new CupcakeDirectoryCompat();
		} else {
			directoryCompat = new FroyoDirectoryCompat();
		}
	}
	
	/** Gets a reference to the public music directory. */
	public static File getPublicMusic() {
		return getPublicMusic(true);
	}
	public static File getPublicMusic(final boolean autoCreate) {
		return directoryCompat.getPublicMusic(autoCreate);
	}
	
	/** Gets a reference to the application specific directory. This directory is public and is stored on the sd card. */
	public static File getPublicApplication(final Context context) {
		return directoryCompat.getPublicApplication(context);
	}
	
	/** Gets a reference to the application specific directory. This directory is public and is stored on the sd card. */
	public static File getPublicApplication(final Context context, final String subdirectory) {
		return directoryCompat.getPublicApplication(context, subdirectory);
	}

	/* Special Folders */
	
	/** Gets a reference to the public pictures directory. This directory is monitored by the Gallery application. */
	public static File getPublicPictures() {
		return getPublicPictures(true);
	}

	/** Gets a reference to the public pictures directory. This directory is monitored by the Gallery application. */
	public static File getPublicPictures(final boolean autoCreate) {
		return directoryCompat.getPublicPictures(autoCreate);
	}

	/** Gets a reference to a directory within the public pictures directory. This will be displayed as a album within the Gallery application. */
	public static File getPublicPictureLibrary(final String libraryName) {
		return directoryCompat.getPublicPictureLibrary(libraryName);
	}
}
