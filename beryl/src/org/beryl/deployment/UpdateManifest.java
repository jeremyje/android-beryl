package org.beryl.deployment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class UpdateManifest {

	private JSONObject jsonManifest = null;

	public UpdateManifest() {
	}

	public void loadManifest(String jsonString) throws JSONException {
		jsonManifest = new JSONObject(jsonString);
	}

	public UpdateManifestApplication findApplication(String applicationPackage, String type) throws JSONException {
		UpdateManifestApplication manifestApplication = new UpdateManifestApplication();

		JSONArray jsonAppList = jsonManifest.getJSONArray("applications");
		final int numApps = jsonAppList.length();
		for(int i = 0; i < numApps; i++) {
			JSONObject jsonApp = jsonAppList.getJSONObject(i);
			if(UpdateManifestApplication.isMatch(jsonApp, applicationPackage, type)) {
				manifestApplication.load(jsonApp);
				break;
			}
		}

		return manifestApplication;
	}
}
