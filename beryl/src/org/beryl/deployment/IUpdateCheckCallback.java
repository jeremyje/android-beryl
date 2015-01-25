package org.beryl.deployment;

public interface IUpdateCheckCallback {
	void onUpdateAvailable(UpdateManifestApplication response);
	void onUpdateNotAvailable(UpdateManifestApplication response);
	void onError(Exception e);
}
