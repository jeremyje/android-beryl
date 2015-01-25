package org.beryl.intents.android;

import org.beryl.app.AndroidVersion;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Settings {
	public static final String APP_PKG_NAME = "pkg";
	public static final String APP_PKG_NAME_GINGERBREAD = "package";
    private static final String ComponentName_InstalledAppDetails = "com.android.settings/.InstalledAppDetails";
    public static final String APP_PKG_PREFIX_CUPCAKE = "com.android.settings.";
    public static final String APP_PKG_NAME_CUPCAKE = APP_PKG_PREFIX_CUPCAKE + "ApplicationPkgName";

	public static final Intent viewInstalledAppSettings(final Context context, final String package_name)
	{
		final Intent intent;

		if(AndroidVersion.isGingerbreadOrHigher()) {
			intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
					 Uri.fromParts(APP_PKG_NAME_GINGERBREAD, package_name, null));
		} else if(AndroidVersion.isEclairOrHigher()) {
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setComponent(ComponentName.unflattenFromString(ComponentName_InstalledAppDetails));
			intent.putExtra(APP_PKG_NAME, package_name);
		} else {
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setComponent(ComponentName.unflattenFromString(ComponentName_InstalledAppDetails));
			intent.putExtra(APP_PKG_NAME_CUPCAKE, package_name);
		}

		return intent;
	}
}
