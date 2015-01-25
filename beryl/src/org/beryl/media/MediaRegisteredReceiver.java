package org.beryl.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

public abstract class MediaRegisteredReceiver extends BroadcastReceiver {

	private static final String INTENT_ACTION = "org.beryl.media.intents.mediaregistered";
	private static final String EXTRA_PATH = "path";
	private static final String EXTRA_URI = "uri";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		if(action.equals(INTENT_ACTION)) {
			final String path = intent.getStringExtra(EXTRA_PATH);
			final Uri uri = intent.getParcelableExtra(EXTRA_URI);
			onScanCompleted(path, uri);
		}
	}

	static void sendBroadcast(Context context, String path, Uri uri) {
		final Intent intent = new Intent(INTENT_ACTION);
		intent.putExtra(EXTRA_PATH, path);
		intent.putExtra(EXTRA_URI, uri);
		
		context.sendBroadcast(intent);
	}
	
	private static IntentFilter createFilter() {
		final IntentFilter filter = new IntentFilter();
		filter.addAction(INTENT_ACTION);
		return filter;
	}
	public static void registerReceiver(Context context, MediaRegisteredReceiver receiver) {
		context.registerReceiver(receiver, createFilter());
	}
	
	protected abstract void onScanCompleted(String path, Uri uri);
}
