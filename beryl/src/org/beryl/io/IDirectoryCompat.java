package org.beryl.io;

import java.io.File;

import android.content.Context;

interface IDirectoryCompat {
	
	File getPublicApplication(final Context context);
	File getPublicApplication(final Context context, final String directoryName);
	File getPublicMusic(final boolean autoCreate);
	File getPublicPodcasts(final boolean autoCreate);
	File getPublicRingtones(final boolean autoCreate);
	File getPublicAlarms(final boolean autoCreate);
	File getPublicNotifications(final boolean autoCreate);
	File getPublicPictures(final boolean autoCreate);
	File getPublicMovies(final boolean autoCreate);
	File getPublicDownloads(final boolean autoCreate);

	File getPublicPictureLibrary(final String libraryName);
}
