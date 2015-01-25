package org.beryl.intents;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.webkit.MimeTypeMap;

public class IntentHelper {

	public static boolean canHandleIntent(final Context context, final Intent intent) {

		if (intent != null) {
			final PackageManager pm = context.getPackageManager();
			if (pm != null) {
				if (pm.queryIntentActivities(intent, 0).size() > 0) {
					return true;
				}
			}
		}

		return false;
	}

	public static String getMimeTypeFromUrl(Uri url) {
		return getMimeTypeFromUrl(url.getPath());
	}

	public static String getMimeTypeFromUrl(String url) {
		final MimeTypeMap mimeMap = MimeTypeMap.getSingleton();
		final String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		return mimeMap.getMimeTypeFromExtension(extension);
	}

	public static final Intent getContentByType(String mimeType) {
		final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType(mimeType);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		return intent;
	}
}
