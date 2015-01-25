package org.beryl.app;

import android.app.Service;
import android.content.Intent;

/**
 * Service implementation that provides compatibility to pre-Eclair versions of Android.
 * Use handleOnStartCommand rather than onStartCommand.
 */
public abstract class ServiceCompat extends Service {

	// This is the old onStart method that will be called on the pre-2.0
	// platform.  On 2.0 or later we override onStartCommand() so this
	// method will not be called.
	@SuppressWarnings("deprecation")
	@Override
	public final void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	    handleOnStartCommand(intent, 0, startId);
	}

	@Override
	public final int onStartCommand(Intent intent, int flags, int startId) {
	    return handleOnStartCommand(intent, flags, startId);
	}
	
	/** New hook function for onStartCommand. */
	protected abstract int handleOnStartCommand(Intent intent, int flags, int startId);
}
