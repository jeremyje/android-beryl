package org.beryl.deployment;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.beryl.diagnostics.ExceptionReporter;

import android.app.Activity;
import android.content.Context;

public class UpdateChecker {

	private final UpdateRequestParams params;

	UpdateChecker(UpdateRequestParams params) {
		this.params = params;
	}

	void begin(IUpdateCheckCallback callback) {
		Thread t = new Thread(new UpdateQuery(params, callback));
		t.start();
	}

	static class UpdateQuery implements Runnable {

		private final UpdateRequestParams params;
		private final IUpdateCheckCallback callback;

		UpdateQuery(UpdateRequestParams params, IUpdateCheckCallback callback) {
			this.params = params;
			this.callback = callback;
		}

		public void run() {
			try {
				final String url = params.getManifestUrl();
				final DefaultHttpClient client = new DefaultHttpClient();
				String response;

				HttpParams httpparams = client.getParams();
				HttpConnectionParams.setConnectionTimeout(httpparams, 10000);
				HttpConnectionParams.setSoTimeout(httpparams, 10000);
				client.setParams(httpparams);

				HttpGet getter = new HttpGet(url);
				response = client.execute(getter, new BasicResponseHandler());

				UpdateManifest manifest = new UpdateManifest();
				manifest.loadManifest(response);
				UpdateManifestApplication upToDateApplication = manifest.findApplication(params.getApplicationPackage(), params.getType());

				if(params.getVersionCode() < upToDateApplication.getVersionCode()) {
					callback.onUpdateAvailable(upToDateApplication);
				} else {
					callback.onUpdateNotAvailable(upToDateApplication);
				}
			} catch(Exception e) {
				ExceptionReporter.report(e);
				callback.onError(e);
			} finally {

			}
		}
	}

	public static UpdateChecker checkForNonProductionUpdate(String manifestUrl, Context context, IUpdateCheckCallback callback) {
		return checkForUpdate(manifestUrl, context, callback, Constants.TYPE_TEST);
	}

	public static UpdateChecker checkForProductionUpdate(String manifestUrl, Context context, IUpdateCheckCallback callback) {
		return checkForUpdate(manifestUrl, context, callback, Constants.TYPE_PRODUCTION);
	}

	public static UpdateChecker checkForNonProductionUpdate(String manifestUrl, Activity activity, IUpdateCheckCallback callback) {
		final ActivityUpdateCheckCallback contextAwareCallback = new ActivityUpdateCheckCallback(activity, callback);
		return checkForUpdate(manifestUrl, activity, contextAwareCallback, Constants.TYPE_TEST);
	}

	public static UpdateChecker checkForProductionUpdate(String manifestUrl, Activity activity, IUpdateCheckCallback callback) {
		final ActivityUpdateCheckCallback contextAwareCallback = new ActivityUpdateCheckCallback(activity, callback);
		return checkForUpdate(manifestUrl, activity, contextAwareCallback, Constants.TYPE_PRODUCTION);
	}

	// Hidden because no one should call this directly use checkForNonProductionUpdate or checkForProductionUpdate.
	static UpdateChecker checkForUpdate(String manifestUrl, Context context, IUpdateCheckCallback callback, String type) {
		UpdateChecker checker;
		UpdateRequestParams params = UpdateRequestParams.create(context, manifestUrl, type);
		checker = new UpdateChecker(params);

		checker.begin(callback);

		return checker;
	}
}
