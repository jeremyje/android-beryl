package org.beryl.content.webfeeds;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * Consumes a feed and stores it in a database with the specified name.
 * This operation occurs in a background service that runs on a separate thread.
 */
public class WebFeedConsumerService extends IntentService {

	private static final String EXTRA_URL = "EXTRA_URL";
	private static final String EXTRA_DATASTORENAME = "EXTRA_DATASTORENAME";
	
	public WebFeedConsumerService() {
		super("WebFeedConsumerService");
	}
	
	public WebFeedConsumerService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final String url = intent.getStringExtra(EXTRA_URL);
		final String datastoreName = intent.getStringExtra(EXTRA_DATASTORENAME);
		final IWebFeedConsumer consumer = FeedConsumer.CreateConsumer(url, FeedConsumer.TYPE_RSS);
		consumeFeed(consumer, datastoreName);
	}

	private void consumeFeed(final IWebFeedConsumer consumer, final String datastoreName) {
		// TODO Auto-generated method stub
		
	}

	public static Intent getConsumeFeedServiceIntent(final Context context, final String url, final String datastoreName) {
		final Intent intent = new Intent(context, WebFeedConsumerService.class);
		intent.putExtra(EXTRA_URL, url);
		intent.putExtra(EXTRA_DATASTORENAME, datastoreName);
		return intent;
	}
	public static void beginConsumeFeed(final Context context, final String url, final String datastoreName) {
		final Intent intent = getConsumeFeedServiceIntent(context, url, datastoreName);
		context.startService(intent);
	}
}
