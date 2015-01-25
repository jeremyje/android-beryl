package org.beryl.deployment;

import org.beryl.util.TryParse;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateManifestApplication {

	private String packageName;
	private String applicationName;
	private int versionCode;
	private String versionName;
	private String changeLog;
	private String updateUrl;
	private boolean mandatory;
	private String type;

	static boolean isMatch(JSONObject jsonApp, String packageName, String type) throws JSONException {
		return packageName.equals(jsonApp.getString("packageName").toLowerCase()) &&
				type.equals(jsonApp.getString("type").toLowerCase());
	}

	void load(JSONObject jsonApp) {
		packageName = TryParse.toString(jsonApp, "packageName", "");
		applicationName = TryParse.toString(jsonApp, "applicationName", "");
		versionCode = TryParse.toInt(jsonApp, "versionCode", 0);
		versionName = TryParse.toString(jsonApp, "versionName", "0");
		changeLog = TryParse.toString(jsonApp, "changeLog", "");
		updateUrl = TryParse.toString(jsonApp, "updateUrl", "");
		mandatory = TryParse.toBoolean(jsonApp, "mandatory", false);
		type = TryParse.toString(jsonApp, "type", Constants.TYPE_TEST).toLowerCase();
	}

	public String getPackageName() {
		return packageName;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public String getChangeLog() {
		return changeLog;
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public boolean isTest() {
		return type.equals(Constants.TYPE_TEST);
	}

	public boolean isProduction() {
		return type.equals(Constants.TYPE_PRODUCTION);
	}
}
