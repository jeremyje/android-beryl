package org.beryl.deployment;

import org.beryl.diagnostics.ExceptionReporter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

class UpdateRequestParams {

	private final String manifestUrl;
	private final String applicationPackage;
	private final int versionCode;

	public final String type;
	private String versionName = null;

	private UpdateRequestParams(String manifestUrl, String packageName, int versionCode, String type) {
		this.manifestUrl = manifestUrl;
		this.applicationPackage = packageName;
		this.versionCode = versionCode;
		this.type = type;
	}

	public String getVersionName() {
		if(versionName != null && versionName.length() > 0) {
			return versionName;
		} else {
			return String.valueOf(this.versionCode);
		}
	}

	public String getManifestUrl() {
		return this.manifestUrl;
	}

	public String getApplicationPackage() {
		return this.applicationPackage;
	}

	public int getVersionCode() {
		return this.versionCode;
	}

	public String getType() {
		return this.type;
	}

	public static UpdateRequestParams create(final Context context, final String manifestUrl, final String type) {
		final PackageManager pm = context.getPackageManager();
		final String packageName = context.getPackageName();
		PackageInfo pkgInfo;
		int versionCode = 0;
		String versionName = null;

		try {
			pkgInfo = pm.getPackageInfo(packageName, 0);
			versionCode = pkgInfo.versionCode;
			versionName = pkgInfo.versionName;
		} catch (NameNotFoundException e) {
			ExceptionReporter.report(e);
		}

		final UpdateRequestParams params = new UpdateRequestParams(manifestUrl, packageName, versionCode, type);
		params.versionName = versionName;

		return params;
	}
}
