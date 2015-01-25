package org.beryl.io;

import java.io.File;

import android.content.Context;
import android.os.Environment;

class CupcakeDirectoryCompat implements IDirectoryCompat {

	public File getPublicMusic(final boolean autoCreate) {
		File directory = getPublicDirectoryByName("Music", autoCreate);
		return directory;
	}

	public File getPublicPodcasts(final boolean autoCreate) {
		File directory = getPublicDirectoryByName("Podcasts", autoCreate);
		return directory;
	}
	
	public File getPublicRingtones(final boolean autoCreate) {
		File directory = getPublicDirectoryByName("Ringtones", autoCreate);
		return directory;
	}
	
	public File getPublicAlarms(final boolean autoCreate) {
		File directory = getPublicDirectoryByName("Alarms", autoCreate);
		return directory;
	}
	
	public File getPublicNotifications(final boolean autoCreate) {
		File directory = getPublicDirectoryByName("Notifications", autoCreate);
		return directory;
	}
	
	public File getPublicPictures(final boolean autoCreate) {
		File directory = getPublicDirectoryByName("Pictures", autoCreate);
		return directory;
	}
	
	public File getPublicMovies(final boolean autoCreate) {
		File directory = getPublicDirectoryByName("Movies", autoCreate);
		return directory;
	}
	
	public File getPublicDownloads(final boolean autoCreate) {
		File directory = getPublicDirectoryByName("Download", autoCreate);
		return directory;
	}

	public File getPublicApplication(final Context context) {
		File directory;
		StringBuilder sb = new StringBuilder();
		sb.append("/Android/data/");
		sb.append(context.getPackageName());
		sb.append("/files/");
		
		String privateDataDirectory = sb.toString();
		File baseDirectory = Environment.getExternalStorageDirectory();
		directory = appendDirectoryName(baseDirectory, privateDataDirectory);
		
		return directory;
	}
	
	public File getPublicApplication(final Context context, final String directoryName) {
		File baseDirectory = getPublicApplication(context);
		File directory = appendDirectoryName(baseDirectory, directoryName);
		attemptAutoCreate(true, directory);
		return directory;
	}
	
	public File getPublicPictureLibrary(final String libraryName) {
		final File basePicturesPath = getPublicPictures(true);
		final File libraryPath = appendDirectoryName(basePicturesPath, libraryName);
		attemptAutoCreate(true, libraryPath);
		return libraryPath;
	}

	public File getPublicDirectoryByName(String name, boolean autoCreate) {
		File directory = null;
		File baseDirectory = Environment.getExternalStorageDirectory();
		directory = appendDirectoryName(baseDirectory, name);
		attemptAutoCreate(autoCreate, directory);

		return directory;
	}
	
	public File appendDirectoryName(File baseDir, String appendDirName) {
		File appendedDir = new File(baseDir, appendDirName);
		return appendedDir;
	}

	public void attemptAutoCreate(boolean autoCreate, File directory) {
		if(autoCreate || directory != null) {
			FileUtils.createDirectory(directory);
		}
	}
}
