package org.beryl.deployment;

import java.lang.ref.WeakReference;

import android.app.Activity;

/** Update Check Callback that is safe to use against an Activity. */
public class ActivityUpdateCheckCallback implements IUpdateCheckCallback {

	final private WeakReference<Activity> activityRef;
	final private IUpdateCheckCallback callback;

	public ActivityUpdateCheckCallback(Activity activity, IUpdateCheckCallback callback) {
		this.activityRef = new WeakReference<Activity>(activity);
		this.callback = callback;
	}

	public void onError(final Exception e) {
		final Activity activity = activityRef.get();
		if(activity != null) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					callback.onError(e);
				}
			});
		}
	}

	public void onUpdateAvailable(final UpdateManifestApplication response) {
		final Activity activity = activityRef.get();
		if(activity != null) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					callback.onUpdateAvailable(response);
				}
			});
		}
	}

	public void onUpdateNotAvailable(final UpdateManifestApplication response) {
		final Activity activity = activityRef.get();
		if(activity != null) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					callback.onUpdateNotAvailable(response);
				}
			});
		}
	}
}
