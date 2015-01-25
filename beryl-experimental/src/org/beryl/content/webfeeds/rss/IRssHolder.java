package org.beryl.content.webfeeds.rss;

public interface IRssHolder {
	RssChannel getChannel();
	RssItem getRssItem();
	RssImage getImage();
	void sealImage();
	void sealItem();
}
