package org.beryl.content.webfeeds;

import org.beryl.content.webfeeds.rss.RssFeedConsumer;

public class FeedConsumer {
	
	public static final int TYPE_RSS = 1;

	public static IWebFeedConsumer CreateConsumer(String url, int type) {
		IWebFeedConsumer consumer = null;
		
		switch(type)
		{
		case TYPE_RSS: {
				consumer = new RssFeedConsumer();
			} break;
		}
		
		return consumer;
	}
}
