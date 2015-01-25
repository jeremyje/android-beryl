package org.beryl.content.webfeeds.rss;

public class BasicRssHolder implements IRssHolder {
	final private RssChannel Channel = new RssChannel();
	private RssItem CurrentItem = new RssItem();
	
	public RssChannel getChannel() {
		return Channel;
	}
	public RssItem getRssItem() {
		return CurrentItem;
	}
	public RssImage getImage() {
		return Channel.image;
	}
	public void sealImage() {
	}
	public void sealItem() {
		Channel.items.add(CurrentItem);
		CurrentItem = new RssItem();
	}
}
