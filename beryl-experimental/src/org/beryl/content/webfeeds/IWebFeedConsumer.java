package org.beryl.content.webfeeds;

public interface IWebFeedConsumer {
	boolean consumeUrl(String url) throws WebFeedException;
	String getSourceUrl();
}
