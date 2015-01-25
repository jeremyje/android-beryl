package org.beryl.io;

import java.io.File;

import android.content.Context;
import android.os.Environment;

class FroyoDirectoryCompat extends CupcakeDirectoryCompat {

	public File getPublicMusic(final boolean autoCreate) {
		File directory = getExternalStoragePublicDirectoryByType(Environment.DIRECTORY_MUSIC, autoCreate);
		return directory;
	}

	public File getPublicPodcasts(final boolean autoCreate) {
		File directory = getExternalStoragePublicDirectoryByType(Environment.DIRECTORY_PODCASTS, autoCreate);
		return directory;
	}
	
	public File getPublicRingtones(final boolean autoCreate) {
		File directory = getExternalStoragePublicDirectoryByType(Environment.DIRECTORY_RINGTONES, autoCreate);
		return directory;
	}
	
	public File getPublicAlarms(final boolean autoCreate) {
		File directory = getExternalStoragePublicDirectoryByType(Environment.DIRECTORY_ALARMS, autoCreate);
		return directory;
	}
	
	public File getPublicNotifications(final boolean autoCreate) {
		File directory = getExternalStoragePublicDirectoryByType(Environment.DIRECTORY_NOTIFICATIONS, autoCreate);
		return directory;
	}
	
	public File getPublicPictures(final boolean autoCreate) {
		File directory = getExternalStoragePublicDirectoryByType(Environment.DIRECTORY_PICTURES, autoCreate);
		return directory;
	}
	
	public File getPublicMovies(final boolean autoCreate) {
		File directory = getExternalStoragePublicDirectoryByType(Environment.DIRECTORY_MOVIES, autoCreate);
		return directory;
	}
	
	public File getPublicDownloads(final boolean autoCreate) {
		File directory = getExternalStoragePublicDirectoryByType(Environment.DIRECTORY_DOWNLOADS, autoCreate);
		return directory;
	}
	
	@Override
	public File getPublicApplication(final Context context) {
		File directory;
		directory = context.getExternalFilesDir(null);
		attemptAutoCreate(true, directory);
		return directory;
	}
	
	public File getExternalStoragePublicDirectoryByType(final String type, final boolean autoCreate) {
		File directory = null;
		directory = Environment.getExternalStoragePublicDirectory(type);
		attemptAutoCreate(autoCreate, directory);
		return directory;
	}
}
