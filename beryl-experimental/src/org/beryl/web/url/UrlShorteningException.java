package org.beryl.web.url;

/** Exception that indicates that something went wrong with the URL Shortening request. */
@SuppressWarnings("serial")
public class UrlShorteningException extends RuntimeException {

	static final String MESSAGE_NETWORK = "URL Shortening service could not be reached or it did not respond in a timely manner.";
	static final String MESSAGE_FORMAT = "URL Shortening service did not respond with a format that could be understood.";
	static final String MESSAGE_NOTSUPPORTED = "The URL used is not supported by this service.";
	
	public UrlShorteningException(String message, Exception e) {
		super(message, e);
	}
}
